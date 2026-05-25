module MySpace.MusicCatalogue.Statements.SelectAlbumByName where

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
-- Parameters for the @select_album_by_name@ query.
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
-- > where name = $name
--
-- ==== Source Path
--
-- > ./queries/select_album_by_name.sql
--
newtype SelectAlbumByName = SelectAlbumByName
  { -- | Maps to @name@.
    name :: Text
  }
  deriving stock (Eq, Show)

-- | Result of the statement parameterised by 'SelectAlbumByName'.
type SelectAlbumByNameResult = Data.Vector.Vector SelectAlbumByNameResultRow

-- | Row of 'SelectAlbumByNameResult'.
data SelectAlbumByNameResultRow = SelectAlbumByNameResultRow
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

instance Arbitrary SelectAlbumByName where
  arbitrary =
    SelectAlbumByName
      <$> (PostgresqlTypes.Text.toText <$> arbitrary)
      
instance Hasql.Mapping.IsStatement.IsStatement SelectAlbumByName where
  type Result SelectAlbumByName = SelectAlbumByNameResult

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
        \where name = $1"

      encoder =
        mconcat
          [ (.name) >$< Hasql.Encoders.param (Hasql.Encoders.nonNullable (Hasql.Mapping.IsScalar.encoder))
          ]

      decoder =
        Hasql.Decoders.rowVector do
          id <- Hasql.Decoders.column (Hasql.Decoders.nonNullable (Hasql.Mapping.IsScalar.decoder))
          name <- Hasql.Decoders.column (Hasql.Decoders.nonNullable (Hasql.Mapping.IsScalar.decoder))
          released <- Hasql.Decoders.column (Hasql.Decoders.nullable (Hasql.Mapping.IsScalar.decoder))
          format <- Hasql.Decoders.column (Hasql.Decoders.nullable (Hasql.Mapping.IsScalar.decoder))
          recording <- Hasql.Decoders.column (Hasql.Decoders.nullable (Hasql.Mapping.IsScalar.decoder))
          pure SelectAlbumByNameResultRow {..}
