module Main where

import qualified Hasql.Connection.Settings
import qualified Hasql.Pool
import qualified Hasql.Pool.Config
import qualified Hasql.Session
import Test.Hspec
import qualified TestcontainersPostgresql
import qualified MySpace.MusicCatalogue.StatementsSpec

main :: IO ()
main = hspec do
  aroundAllWith
    ( \action () -> do
      TestcontainersPostgresql.run
        TestcontainersPostgresql.Config
          { TestcontainersPostgresql.tagName = "postgres:18"
          , TestcontainersPostgresql.auth = TestcontainersPostgresql.TrustAuth
          , TestcontainersPostgresql.forwardLogs = False
          }
        ( \(host, port) -> do
            let connectionSettings =
                  Hasql.Connection.Settings.hostAndPort host port
                    <> Hasql.Connection.Settings.user "postgres"
                    <> Hasql.Connection.Settings.dbname "postgres"

            pool <-
              Hasql.Pool.acquire
                ( Hasql.Pool.Config.settings
                    [ Hasql.Pool.Config.size 10
                    , Hasql.Pool.Config.staticConnectionSettings connectionSettings
                    ]
                )

            migrationResult <-
              Hasql.Pool.use
                pool
                ( do
                    Hasql.Session.script
                      "\n\
                        \create table \"genre\" (\n\
                        \  \"id\" int4 not null generated always as identity primary key,\n\
                        \  \"name\" text not null unique\n\
                        \);\n\
                        \\n\
                        \create table \"artist\" (\n\
                        \  \"id\" int4 not null generated always as identity primary key,\n\
                        \  \"name\" text not null\n\
                        \);\n\
                        \\n\
                        \create table \"album\" (\n\
                        \  \"id\" int4 not null generated always as identity primary key,\n\
                        \  -- Album name.\n\
                        \  \"name\" text not null,\n\
                        \  -- The date the album was first released.\n\
                        \  \"released\" date null\n\
                        \);\n\
                        \\n\
                        \create table \"album_genre\" (\n\
                        \  \"album\" int4 not null references \"album\",\n\
                        \  \"genre\" int4 not null references \"genre\"\n\
                        \);\n\
                        \\n\
                        \create table \"album_artist\" (\n\
                        \  \"album\" int4 not null references \"album\",\n\
                        \  \"artist\" int4 not null references \"artist\",\n\
                        \  -- Whether it is the primary artist\n\
                        \  \"primary\" bool not null,\n\
                        \  primary key (\"album\", \"artist\")\n\
                        \);\n\
                        \"
                    Hasql.Session.script
                      "-- In this migration we're changing the type of the album \"id\" column\n\
                        \-- from \"int4\" to \"int8\".\n\
                        \-- Since this column is referenced from other tables, we also update them.\n\
                        \\n\
                        \\n\
                        \alter table album\n\
                        \alter column id type int8;\n\
                        \\n\
                        \alter table album_genre\n\
                        \alter column album type int8;\n\
                        \\n\
                        \alter table album_artist\n\
                        \alter column album type int8;\n\
                        \"
                    Hasql.Session.script
                      "-- Add enumeration type for album formats\n\
                        \create type album_format as enum (\n\
                        \  'Vinyl',\n\
                        \  'CD',\n\
                        \  'Cassette',\n\
                        \  'Digital',\n\
                        \  'DVD-Audio',\n\
                        \  'SACD'\n\
                        \);\n\
                        \\n\
                        \-- Add composite type for recording session information\n\
                        \create type recording_info as (\n\
                        \  studio_name text,\n\
                        \  city text,\n\
                        \  country text,\n\
                        \  recorded_date date\n\
                        \);\n\
                        \\n\
                        \-- Add format column to album table\n\
                        \alter table album\n\
                        \add column format album_format null;\n\
                        \\n\
                        \-- Add recording information to album table\n\
                        \alter table album\n\
                        \add column recording recording_info null;\n\
                        \"
                    Hasql.Session.script
                      "-- Composite with array fields: a track in an album\n\
                        \create type track_info as (\n\
                        \  title text,\n\
                        \  duration_seconds int4,\n\
                        \  tags text[]\n\
                        \);\n\
                        \\n\
                        \-- Composite with composites: disc information referencing a recording\n\
                        \create type disc_info as (\n\
                        \  name text,\n\
                        \  recording recording_info\n\
                        \);\n\
                        \\n\
                        \-- Edge case: array of composites column\n\
                        \alter table album\n\
                        \add column tracks track_info[] null;\n\
                        \\n\
                        \-- Edge case: composite with composites column\n\
                        \alter table album\n\
                        \add column disc disc_info null;\n\
                        \"
                    Hasql.Session.script
                      "CREATE INDEX ON album (recording);\n\
                        \"
                    Hasql.Session.script
                      "-- Add support for hierarchical genre paths.\n\
                        \create extension if not exists ltree;\n\
                        \\n\
                        \alter table genre\n\
                        \add column path ltree not null;\n\
                        \\n\
                        \create index on genre using gist (path);"
                )

            case migrationResult of
              Left err ->
                fail ("Failed to apply migrations: " ++ show err)

              Right () ->
                pure ()

            action pool

            Hasql.Pool.release pool
        )
    )
    MySpace.MusicCatalogue.StatementsSpec.spec
