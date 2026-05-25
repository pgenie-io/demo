module MySpace.MusicCatalogue.Statements.UpdateAlbumReleased where

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
-- Parameters for the @update_album_released@ query.
--
-- ==== SQL Template
--
-- > update album
-- > set released = $released
-- > where id = $id
--
-- ==== Source Path
--
-- > ./queries/update_album_released.sql
--
data UpdateAlbumReleased = UpdateAlbumReleased
  { -- | Maps to @released@.
    released :: Maybe (PostgresqlTypes.Date),
    -- | Maps to @id@.
    id :: Int64
  }
  deriving stock (Eq, Show)

type UpdateAlbumReleasedResult = Int

instance Arbitrary UpdateAlbumReleased where
  arbitrary =
    UpdateAlbumReleased
      <$> (liftArbitrary arbitrary)
      <*> (PostgresqlTypes.Int8.toInt64 <$> arbitrary)
      
instance Hasql.Mapping.IsStatement.IsStatement UpdateAlbumReleased where
  type Result UpdateAlbumReleased = UpdateAlbumReleasedResult

  statement = Hasql.Statement.preparable sql encoder decoder
    where
      sql =
        "update album\n\
        \set released = $1\n\
        \where id = $2"

      encoder =
        mconcat
          [ (.released) >$< Hasql.Encoders.param (Hasql.Encoders.nullable (Hasql.Mapping.IsScalar.encoder)),
            (.id) >$< Hasql.Encoders.param (Hasql.Encoders.nonNullable (Hasql.Mapping.IsScalar.encoder))
          ]

      decoder =
        fromIntegral <$> Hasql.Decoders.rowsAffected
