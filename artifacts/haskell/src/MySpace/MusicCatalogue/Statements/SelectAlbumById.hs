module MySpace.MusicCatalogue.Statements.SelectAlbumById where

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
-- Parameters for the @select_album_by_id@ query.
--
-- ==== SQL Template
--
-- > -- Example of a query selecting 0 or 1 row.
-- > select *
-- > from album
-- > where id = $id
-- > limit 1
--
-- ==== Source Path
--
-- > ./queries/select_album_by_id.sql
--
newtype SelectAlbumById = SelectAlbumById
  { -- | Maps to @id@.
    id :: Maybe (Int64)
  }
  deriving stock (Eq, Show)

-- | Result of the statement parameterised by 'SelectAlbumById'.
type SelectAlbumByIdResult = Maybe SelectAlbumByIdResultRow

-- | Row of 'SelectAlbumByIdResult'.
data SelectAlbumByIdResultRow = SelectAlbumByIdResultRow
  { -- | Maps to @id@.
    id :: Int64,
    -- | Maps to @name@.
    name :: Text,
    -- | Maps to @released@.
    released :: Maybe (Pt.Date),
    -- | Maps to @format@.
    format :: Maybe (Types.AlbumFormat),
    -- | Maps to @recording@.
    recording :: Maybe (Types.RecordingInfo),
    -- | Maps to @tracks@.
    tracks :: Maybe (Vector (Maybe Types.TrackInfo)),
    -- | Maps to @disc@.
    disc :: Maybe (Types.DiscInfo)
  }
  deriving stock (Show, Eq)

instance IsStatement.IsStatement SelectAlbumById where
  type Result SelectAlbumById = SelectAlbumByIdResult

  statement = Statement.preparable sql encoder decoder
    where
      sql =
        "-- Example of a query selecting 0 or 1 row.\n\
        \select *\n\
        \from album\n\
        \where id = $1\n\
        \limit 1"

      encoder =
        mconcat
          [ (.id) >$< Encoders.param (Encoders.nullable (IsScalar.encoder))
          ]

      decoder =
        Decoders.rowMaybe do
          id <- Decoders.column (Decoders.nonNullable (IsScalar.decoder))
          name <- Decoders.column (Decoders.nonNullable (IsScalar.decoder))
          released <- Decoders.column (Decoders.nullable (IsScalar.decoder))
          format <- Decoders.column (Decoders.nullable (IsScalar.decoder))
          recording <- Decoders.column (Decoders.nullable (IsScalar.decoder))
          tracks <- Decoders.column (Decoders.nullable (Decoders.array (Decoders.dimension Vector.replicateM (Decoders.element (Decoders.nullable IsScalar.decoder)))))
          disc <- Decoders.column (Decoders.nullable (IsScalar.decoder))
          pure SelectAlbumByIdResultRow {..}

