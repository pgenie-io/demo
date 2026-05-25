module MySpace.MusicCatalogue.Statements.SelectAlbumByFormat where

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
-- Parameters for the @select_album_by_format@ query.
--
-- ==== SQL Template
--
-- > select 
-- >   id,
-- >   name,
-- >   released,
-- >   format,
-- >   recording
-- > from album
-- > where format = $format
--
-- ==== Source Path
--
-- > ./queries/select_album_by_format.sql
--
newtype SelectAlbumByFormat = SelectAlbumByFormat
  { -- | Maps to @format@.
    format :: AlbumFormat
  }
  deriving stock (Eq, Show)

-- | Result of the statement parameterised by 'SelectAlbumByFormat'.
type SelectAlbumByFormatResult = Data.Vector.Vector SelectAlbumByFormatResultRow

-- | Row of 'SelectAlbumByFormatResult'.
data SelectAlbumByFormatResultRow = SelectAlbumByFormatResultRow
  { -- | Maps to @id@.
    id :: Int64,
    -- | Maps to @name@.
    name :: Text,
    -- | Maps to @released@.
    released :: Maybe (PostgresqlTypes.Date),
    -- | Maps to @format@.
    format :: Maybe (AlbumFormat),
    -- | Maps to @recording@.
    recording :: Maybe (RecordingInfo)
  }
  deriving stock (Show, Eq)

instance Arbitrary SelectAlbumByFormat where
  arbitrary =
    SelectAlbumByFormat
      <$> arbitrary
      
instance Hasql.Mapping.IsStatement.IsStatement SelectAlbumByFormat where
  type Result SelectAlbumByFormat = SelectAlbumByFormatResult

  statement = Hasql.Statement.preparable sql encoder decoder
    where
      sql =
        "select \n\
        \  id,\n\
        \  name,\n\
        \  released,\n\
        \  format,\n\
        \  recording\n\
        \from album\n\
        \where format = $1"

      encoder =
        mconcat
          [ (.format) >$< Hasql.Encoders.param (Hasql.Encoders.nonNullable (Hasql.Mapping.IsScalar.encoder))
          ]

      decoder =
        Hasql.Decoders.rowVector do
          id <- Hasql.Decoders.column (Hasql.Decoders.nonNullable (Hasql.Mapping.IsScalar.decoder))
          name <- Hasql.Decoders.column (Hasql.Decoders.nonNullable (Hasql.Mapping.IsScalar.decoder))
          released <- Hasql.Decoders.column (Hasql.Decoders.nullable (Hasql.Mapping.IsScalar.decoder))
          format <- Hasql.Decoders.column (Hasql.Decoders.nullable (Hasql.Mapping.IsScalar.decoder))
          recording <- Hasql.Decoders.column (Hasql.Decoders.nullable (Hasql.Mapping.IsScalar.decoder))
          pure SelectAlbumByFormatResultRow {..}
