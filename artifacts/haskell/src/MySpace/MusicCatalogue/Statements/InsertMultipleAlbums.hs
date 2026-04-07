module MySpace.MusicCatalogue.Statements.InsertMultipleAlbums where

import MySpace.MusicCatalogue.Prelude
import qualified Hasql.Statement as Statement
import qualified Hasql.Decoders as Decoders
import qualified Hasql.Encoders as Encoders
import qualified Data.Aeson as Aeson
import qualified Data.Vector as Vector
import qualified Hasql.Mapping.IsStatement as IsStatement
import qualified Hasql.Mapping.IsScalar as IsScalar
import qualified MySpace.MusicCatalogue.Types as Types
import qualified PostgresqlTypes as Pt

-- |
-- Parameters for the @insert_multiple_albums@ query.
--
-- ==== SQL Template
--
-- > -- This is an example of a bulk-insert (batch-insert) technique.
-- > -- We pass in all fields as arrays of the same size, and we unnest it to insert multiple rows at once.
-- > insert into album (name, released, format)
-- > select *
-- > from unnest(
-- >   $name::text[],
-- >   $released::date[],
-- >   $format::album_format[]
-- > )
-- > returning id
--
-- ==== Source Path
--
-- > ./queries/insert_multiple_albums.sql
--
data InsertMultipleAlbums = InsertMultipleAlbums
  { -- | Maps to @name@.
    name :: Vector (Text),
    -- | Maps to @released@.
    released :: Vector (Pt.Date),
    -- | Maps to @format@.
    format :: Vector (Types.AlbumFormat)
  }
  deriving stock (Eq, Show)

-- | Result of the statement parameterised by 'InsertMultipleAlbums'.
type InsertMultipleAlbumsResult = Vector.Vector InsertMultipleAlbumsResultRow

-- | Row of 'InsertMultipleAlbumsResult'.
newtype InsertMultipleAlbumsResultRow = InsertMultipleAlbumsResultRow
  { -- | Maps to @id@.
    id :: Int64
  }
  deriving stock (Show, Eq)

instance IsStatement.IsStatement InsertMultipleAlbums where
  type Result InsertMultipleAlbums = InsertMultipleAlbumsResult

  statement = Statement.preparable sql encoder decoder
    where
      sql =
        "-- This is an example of a bulk-insert (batch-insert) technique.\n\
        \-- We pass in all fields as arrays of the same size, and we unnest it to insert multiple rows at once.\n\
        \insert into album (name, released, format)\n\
        \select *\n\
        \from unnest(\n\
        \  $1::text[],\n\
        \  $2::date[],\n\
        \  $3::album_format[]\n\
        \)\n\
        \returning id"

      encoder =
        mconcat
          [ (.name) >$< Encoders.param (Encoders.nonNullable (Encoders.array (Encoders.dimension Vector.foldl' (Encoders.element (Encoders.nonNullable IsScalar.encoder))))),
            (.released) >$< Encoders.param (Encoders.nonNullable (Encoders.array (Encoders.dimension Vector.foldl' (Encoders.element (Encoders.nonNullable IsScalar.encoder))))),
            (.format) >$< Encoders.param (Encoders.nonNullable (Encoders.array (Encoders.dimension Vector.foldl' (Encoders.element (Encoders.nonNullable IsScalar.encoder)))))
          ]

      decoder =
        Decoders.rowVector do
          id <- Decoders.column (Decoders.nonNullable (IsScalar.decoder))
          pure InsertMultipleAlbumsResultRow {..}

