module MySpace.MusicCatalogue.Statements.UpdateAlbumReleased where

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
    released :: Maybe (Pt.Date),
    -- | Maps to @id@.
    id :: Int64
  }
  deriving stock (Eq, Show)

type UpdateAlbumReleasedResult = Int

instance IsStatement.IsStatement UpdateAlbumReleased where
  type Result UpdateAlbumReleased = UpdateAlbumReleasedResult

  statement = Statement.preparable sql encoder decoder
    where
      sql =
        "update album\n\
        \set released = $1\n\
        \where id = $2"

      encoder =
        mconcat
          [ (.released) >$< Encoders.param (Encoders.nullable (IsScalar.encoder)),
            (.id) >$< Encoders.param (Encoders.nonNullable (IsScalar.encoder))
          ]

      decoder =
        fromIntegral <$> Decoders.rowsAffected

