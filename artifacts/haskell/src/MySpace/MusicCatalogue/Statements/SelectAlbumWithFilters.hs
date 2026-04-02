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
-- > SELECT
-- >   album.id,
-- >   album.name,
-- >   album.released,
-- >   album.format
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
  { -- | Maps to @artist_name@.
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
    name :: Text,
    -- | Maps to @released@.
    released :: Maybe (Pt.Date),
    -- | Maps to @format@.
    format :: Maybe (Types.AlbumFormat)
  }
  deriving stock (Show, Eq)

instance IsStatement.IsStatement SelectAlbumWithFilters where
  type Result SelectAlbumWithFilters = SelectAlbumWithFiltersResult

  statement = Statement.preparable sql encoder decoder
    where
      sql =
        "SELECT\n\
        \  album.id,\n\
        \  album.name,\n\
        \  album.released,\n\
        \  album.format\n\
        \FROM album\n\
        \LEFT JOIN album_artist ON album_artist.album = album.id\n\
        \LEFT JOIN artist ON artist.id = album_artist.artist\n\
        \LEFT JOIN album_genre ON album_genre.album = album.id\n\
        \LEFT JOIN genre ON genre.id = album_genre.genre\n\
        \WHERE\n\
        \  ($1::text IS NULL OR artist.name = $1)\n\
        \  AND ($2::text IS NULL OR genre.name = $2)\n\
        \  AND ($3::album_format IS NULL OR album.format = $3)\n\
        \  AND ($4::timestamp IS NULL OR album.released >= $4)\n\
        \  AND ($5::text IS NULL OR album.name LIKE $5)\n\
        \ORDER BY\n\
        \  CASE WHEN $6 THEN album.name END ASC,\n\
        \  CASE WHEN $7 THEN album.released END DESC"

      encoder =
        mconcat
          [ (.artistName) >$< Encoders.param (Encoders.nullable (IsScalar.encoder)),
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
          name <- Decoders.column (Decoders.nonNullable (IsScalar.decoder))
          released <- Decoders.column (Decoders.nullable (IsScalar.decoder))
          format <- Decoders.column (Decoders.nullable (IsScalar.decoder))
          pure SelectAlbumWithFiltersResultRow {..}

