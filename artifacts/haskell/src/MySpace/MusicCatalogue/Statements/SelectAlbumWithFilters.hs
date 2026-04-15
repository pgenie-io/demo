module MySpace.MusicCatalogue.Statements.SelectAlbumWithFilters where

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
-- Parameters for the @select_album_with_filters@ query.
--
-- ==== SQL Template
--
-- > -- Demonstrates static query equivalent of dynamic field selection.
-- > -- Boolean flags control which fields are included in the result,
-- > -- returning NULL for fields the caller opts out of.
-- > -- Also demonstrates optional filters and ordering criteria.
-- > SELECT
-- >   album.id,
-- >   CASE WHEN $include_name THEN album.name END AS name,
-- >   CASE WHEN $include_released THEN album.released END AS released,
-- >   CASE WHEN $include_format THEN album.format END AS format,
-- >   CASE WHEN $include_recording THEN album.recording END AS recording,
-- >   CASE WHEN $include_tracks THEN album.tracks END AS tracks,
-- >   CASE WHEN $include_disc THEN album.disc END AS disc
-- > FROM album
-- > LEFT JOIN album_artist ON album_artist.album = album.id
-- > LEFT JOIN artist ON artist.id = album_artist.artist
-- > LEFT JOIN album_genre ON album_genre.album = album.id
-- > LEFT JOIN genre ON genre.id = album_genre.genre
-- > WHERE
-- >   ($artist_name::text IS NULL OR artist.name = $artist_name)
-- >   AND ($genre_name::text IS NULL OR genre.name = $genre_name)
-- >   AND ($format::album_format IS NULL OR album.format = $format)
-- >   AND ($released_after::timestamp IS NULL OR album.released >= $released_after)
-- >   AND ($name_like::text IS NULL OR album.name LIKE $name_like)
-- > ORDER BY
-- >   CASE WHEN $order_by_name THEN album.name END ASC,
-- >   CASE WHEN $order_by_released THEN album.released END DESC
--
-- ==== Source Path
--
-- > ./queries/select_album_with_filters.sql
--
data SelectAlbumWithFilters = SelectAlbumWithFilters
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
    -- | Maps to @artist_name@.
    artistName :: Maybe (Text),
    -- | Maps to @genre_name@.
    genreName :: Maybe (Text),
    -- | Maps to @format@.
    format :: Maybe (Types.AlbumFormat),
    -- | Maps to @released_after@.
    releasedAfter :: Maybe (Pt.Timestamp),
    -- | Maps to @name_like@.
    nameLike :: Maybe (Text),
    -- | Maps to @order_by_name@.
    orderByName :: Bool,
    -- | Maps to @order_by_released@.
    orderByReleased :: Bool
  }
  deriving stock (Eq, Show)

-- | Result of the statement parameterised by 'SelectAlbumWithFilters'.
type SelectAlbumWithFiltersResult = Vector.Vector SelectAlbumWithFiltersResultRow

-- | Row of 'SelectAlbumWithFiltersResult'.
data SelectAlbumWithFiltersResultRow = SelectAlbumWithFiltersResultRow
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

instance IsStatement.IsStatement SelectAlbumWithFilters where
  type Result SelectAlbumWithFilters = SelectAlbumWithFiltersResult

  statement = Statement.preparable sql encoder decoder
    where
      sql =
        "-- Demonstrates static query equivalent of dynamic field selection.\n\
        \-- Boolean flags control which fields are included in the result,\n\
        \-- returning NULL for fields the caller opts out of.\n\
        \-- Also demonstrates optional filters and ordering criteria.\n\
        \SELECT\n\
        \  album.id,\n\
        \  CASE WHEN $1 THEN album.name END AS name,\n\
        \  CASE WHEN $2 THEN album.released END AS released,\n\
        \  CASE WHEN $3 THEN album.format END AS format,\n\
        \  CASE WHEN $4 THEN album.recording END AS recording,\n\
        \  CASE WHEN $5 THEN album.tracks END AS tracks,\n\
        \  CASE WHEN $6 THEN album.disc END AS disc\n\
        \FROM album\n\
        \LEFT JOIN album_artist ON album_artist.album = album.id\n\
        \LEFT JOIN artist ON artist.id = album_artist.artist\n\
        \LEFT JOIN album_genre ON album_genre.album = album.id\n\
        \LEFT JOIN genre ON genre.id = album_genre.genre\n\
        \WHERE\n\
        \  ($7::text IS NULL OR artist.name = $7)\n\
        \  AND ($8::text IS NULL OR genre.name = $8)\n\
        \  AND ($9::album_format IS NULL OR album.format = $9)\n\
        \  AND ($10::timestamp IS NULL OR album.released >= $10)\n\
        \  AND ($11::text IS NULL OR album.name LIKE $11)\n\
        \ORDER BY\n\
        \  CASE WHEN $12 THEN album.name END ASC,\n\
        \  CASE WHEN $13 THEN album.released END DESC"

      encoder =
        mconcat
          [ (.includeName) >$< Encoders.param (Encoders.nonNullable (IsScalar.encoder)),
            (.includeReleased) >$< Encoders.param (Encoders.nonNullable (IsScalar.encoder)),
            (.includeFormat) >$< Encoders.param (Encoders.nonNullable (IsScalar.encoder)),
            (.includeRecording) >$< Encoders.param (Encoders.nonNullable (IsScalar.encoder)),
            (.includeTracks) >$< Encoders.param (Encoders.nonNullable (IsScalar.encoder)),
            (.includeDisc) >$< Encoders.param (Encoders.nonNullable (IsScalar.encoder)),
            (.artistName) >$< Encoders.param (Encoders.nullable (IsScalar.encoder)),
            (.genreName) >$< Encoders.param (Encoders.nullable (IsScalar.encoder)),
            (.format) >$< Encoders.param (Encoders.nullable (IsScalar.encoder)),
            (.releasedAfter) >$< Encoders.param (Encoders.nullable (IsScalar.encoder)),
            (.nameLike) >$< Encoders.param (Encoders.nullable (IsScalar.encoder)),
            (.orderByName) >$< Encoders.param (Encoders.nonNullable (IsScalar.encoder)),
            (.orderByReleased) >$< Encoders.param (Encoders.nonNullable (IsScalar.encoder))
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
          pure SelectAlbumWithFiltersResultRow {..}

