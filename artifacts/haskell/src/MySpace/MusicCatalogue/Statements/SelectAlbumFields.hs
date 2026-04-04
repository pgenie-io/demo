module MySpace.MusicCatalogue.Statements.SelectAlbumFields where

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
-- Parameters for the @select_album_fields@ query.
--
-- ==== SQL Template
--
-- > -- Demonstrates static query equivalent of dynamic field selection.
-- > -- Boolean flags control which fields are included in the result,
-- > -- returning NULL for fields the caller opts out of.
-- > SELECT
-- >   album.id,
-- >   CASE WHEN $include_name      THEN album.name      END AS name,
-- >   CASE WHEN $include_released  THEN album.released  END AS released,
-- >   CASE WHEN $include_format    THEN album.format    END AS format,
-- >   CASE WHEN $include_recording THEN album.recording END AS recording,
-- >   CASE WHEN $include_tracks    THEN album.tracks    END AS tracks,
-- >   CASE WHEN $include_disc      THEN album.disc      END AS disc
-- > FROM album
-- > WHERE album.id = $id
--
-- ==== Source Path
--
-- > ./queries/select_album_fields.sql
--
data SelectAlbumFields = SelectAlbumFields
  { -- | Maps to @include_name@.
    includeName :: Bool,
    -- | Maps to @include_released@.
    includeReleased :: Bool,
    -- | Maps to @include_format@.
    includeFormat :: Bool,
    -- | Maps to @include_recording@.
    includeRecording :: Bool,
    -- | Maps to @include_tracks@.
    includeTracks :: Bool,
    -- | Maps to @include_disc@.
    includeDisc :: Bool,
    -- | Maps to @id@.
    id :: Int64
  }
  deriving stock (Eq, Show)

-- | Result of the statement parameterised by 'SelectAlbumFields'.
type SelectAlbumFieldsResult = Vector.Vector SelectAlbumFieldsResultRow

-- | Row of 'SelectAlbumFieldsResult'.
data SelectAlbumFieldsResultRow = SelectAlbumFieldsResultRow
  { -- | Maps to @id@.
    id :: Int64,
    -- | Maps to @name@.
    name :: Maybe (Text),
    -- | Maps to @released@.
    released :: Maybe (Pt.Date),
    -- | Maps to @format@.
    format :: Maybe (Types.AlbumFormat),
    -- | Maps to @recording@.
    recording :: Maybe (Types.RecordingInfo),
    -- | Maps to @tracks@.
    tracks :: Maybe (Vector (Types.TrackInfo)),
    -- | Maps to @disc@.
    disc :: Maybe (Types.DiscInfo)
  }
  deriving stock (Show, Eq)

instance IsStatement.IsStatement SelectAlbumFields where
  type Result SelectAlbumFields = SelectAlbumFieldsResult

  statement = Statement.preparable sql encoder decoder
    where
      sql =
        "-- Demonstrates static query equivalent of dynamic field selection.\n\
        \-- Boolean flags control which fields are included in the result,\n\
        \-- returning NULL for fields the caller opts out of.\n\
        \SELECT\n\
        \  album.id,\n\
        \  CASE WHEN $1      THEN album.name      END AS name,\n\
        \  CASE WHEN $2  THEN album.released  END AS released,\n\
        \  CASE WHEN $3    THEN album.format    END AS format,\n\
        \  CASE WHEN $4 THEN album.recording END AS recording,\n\
        \  CASE WHEN $5    THEN album.tracks    END AS tracks,\n\
        \  CASE WHEN $6      THEN album.disc      END AS disc\n\
        \FROM album\n\
        \WHERE album.id = $7"

      encoder =
        mconcat
          [ (.includeName) >$< Encoders.param (Encoders.nonNullable (IsScalar.encoder)),
            (.includeReleased) >$< Encoders.param (Encoders.nonNullable (IsScalar.encoder)),
            (.includeFormat) >$< Encoders.param (Encoders.nonNullable (IsScalar.encoder)),
            (.includeRecording) >$< Encoders.param (Encoders.nonNullable (IsScalar.encoder)),
            (.includeTracks) >$< Encoders.param (Encoders.nonNullable (IsScalar.encoder)),
            (.includeDisc) >$< Encoders.param (Encoders.nonNullable (IsScalar.encoder)),
            (.id) >$< Encoders.param (Encoders.nonNullable (IsScalar.encoder))
          ]

      decoder =
        Decoders.rowVector do
          id <- Decoders.column (Decoders.nonNullable (IsScalar.decoder))
          name <- Decoders.column (Decoders.nullable (IsScalar.decoder))
          released <- Decoders.column (Decoders.nullable (IsScalar.decoder))
          format <- Decoders.column (Decoders.nullable (IsScalar.decoder))
          recording <- Decoders.column (Decoders.nullable (IsScalar.decoder))
          tracks <- Decoders.column (Decoders.nullable (Decoders.array (Decoders.dimension Vector.replicateM (Decoders.element (Decoders.nonNullable IsScalar.decoder)))))
          disc <- Decoders.column (Decoders.nullable (IsScalar.decoder))
          pure SelectAlbumFieldsResultRow {..}

