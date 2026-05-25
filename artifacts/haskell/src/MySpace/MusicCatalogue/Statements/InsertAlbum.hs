module MySpace.MusicCatalogue.Statements.InsertAlbum where

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
-- Parameters for the @insert_album@ query.
--
-- ==== SQL Template
--
-- > insert into album (name, released, format, recording)
-- > values ($name, $released, $format, $recording)
-- > returning id
--
-- ==== Source Path
--
-- > ./queries/insert_album.sql
--
data InsertAlbum = InsertAlbum
  { -- | Maps to @name@.
    name :: Text,
    -- | Maps to @released@.
    released :: PostgresqlTypes.Date,
    -- | Maps to @format@.
    format :: AlbumFormat,
    -- | Maps to @recording@.
    recording :: RecordingInfo
  }
  deriving stock (Eq, Show)

-- | Result of the statement parameterised by 'InsertAlbum'.
type InsertAlbumResult = InsertAlbumResultRow

-- | Row of 'InsertAlbumResult'.
newtype InsertAlbumResultRow = InsertAlbumResultRow
  { -- | Maps to @id@.
    id :: Int64
  }
  deriving stock (Show, Eq)

instance Arbitrary InsertAlbum where
  arbitrary =
    InsertAlbum
      <$> (PostgresqlTypes.Text.toText <$> arbitrary)
      <*> arbitrary
      <*> arbitrary
      <*> arbitrary
      
instance Hasql.Mapping.IsStatement.IsStatement InsertAlbum where
  type Result InsertAlbum = InsertAlbumResult

  statement = Hasql.Statement.preparable sql encoder decoder
    where
      sql =
        "insert into album (name, released, format, recording)\n\
        \values ($1, $2, $3, $4)\n\
        \returning id"

      encoder =
        mconcat
          [ (.name) >$< Hasql.Encoders.param (Hasql.Encoders.nonNullable (Hasql.Mapping.IsScalar.encoder)),
            (.released) >$< Hasql.Encoders.param (Hasql.Encoders.nonNullable (Hasql.Mapping.IsScalar.encoder)),
            (.format) >$< Hasql.Encoders.param (Hasql.Encoders.nonNullable (Hasql.Mapping.IsScalar.encoder)),
            (.recording) >$< Hasql.Encoders.param (Hasql.Encoders.nonNullable (Hasql.Mapping.IsScalar.encoder))
          ]

      decoder =
        Hasql.Decoders.singleRow do
          id <- Hasql.Decoders.column (Hasql.Decoders.nonNullable (Hasql.Mapping.IsScalar.decoder))
          pure InsertAlbumResultRow {..}
