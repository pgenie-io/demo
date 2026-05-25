module MySpace.MusicCatalogue.Statements.InsertMultipleAlbums where

import MySpace.MusicCatalogue.Prelude
import Test.QuickCheck
import qualified Hasql.Statement
import qualified Hasql.Decoders
import qualified Hasql.Encoders
import qualified Data.Aeson
import qualified Data.Vector
import qualified Hasql.Mapping.IsStatement
import qualified Hasql.Mapping.IsScalar
import MySpace.MusicCatalogue.Types
import qualified PostgresqlTypes
import qualified PostgresqlTypes.Date
import qualified PostgresqlTypes.Bytea
import qualified PostgresqlTypes.Numeric
import qualified PostgresqlTypes.Float4
import qualified PostgresqlTypes.Float8
import qualified PostgresqlTypes.Int2
import qualified PostgresqlTypes.Int4
import qualified PostgresqlTypes.Int8
import qualified PostgresqlTypes.Text
import qualified PostgresqlTypes.Uuid

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
    released :: Vector (PostgresqlTypes.Date),
    -- | Maps to @format@.
    format :: Vector (AlbumFormat)
  }
  deriving stock (Eq, Show)

-- | Result of the statement parameterised by 'InsertMultipleAlbums'.
type InsertMultipleAlbumsResult = Data.Vector.Vector InsertMultipleAlbumsResultRow

-- | Row of 'InsertMultipleAlbumsResult'.
newtype InsertMultipleAlbumsResultRow = InsertMultipleAlbumsResultRow
  { -- | Maps to @id@.
    id :: Int64
  }
  deriving stock (Show, Eq)

instance Arbitrary InsertMultipleAlbums where
  arbitrary =
    InsertMultipleAlbums
      <$> (liftArbitrary (PostgresqlTypes.Text.toText <$> arbitrary))
      <*> (liftArbitrary arbitrary)
      <*> (liftArbitrary arbitrary)
      
instance Hasql.Mapping.IsStatement.IsStatement InsertMultipleAlbums where
  type Result InsertMultipleAlbums = InsertMultipleAlbumsResult

  statement = Hasql.Statement.preparable sql encoder decoder
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
          [ (.name) >$< Hasql.Encoders.param (Hasql.Encoders.nonNullable (Hasql.Encoders.array (Hasql.Encoders.dimension Data.Vector.foldl' (Hasql.Encoders.element (Hasql.Encoders.nonNullable Hasql.Mapping.IsScalar.encoder))))),
            (.released) >$< Hasql.Encoders.param (Hasql.Encoders.nonNullable (Hasql.Encoders.array (Hasql.Encoders.dimension Data.Vector.foldl' (Hasql.Encoders.element (Hasql.Encoders.nonNullable Hasql.Mapping.IsScalar.encoder))))),
            (.format) >$< Hasql.Encoders.param (Hasql.Encoders.nonNullable (Hasql.Encoders.array (Hasql.Encoders.dimension Data.Vector.foldl' (Hasql.Encoders.element (Hasql.Encoders.nonNullable Hasql.Mapping.IsScalar.encoder)))))
          ]

      decoder =
        Hasql.Decoders.rowVector do
          id <- Hasql.Decoders.column (Hasql.Decoders.nonNullable (Hasql.Mapping.IsScalar.decoder))
          pure InsertMultipleAlbumsResultRow {..}
