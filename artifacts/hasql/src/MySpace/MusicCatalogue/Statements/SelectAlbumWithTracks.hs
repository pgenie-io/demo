module MySpace.MusicCatalogue.Statements.SelectAlbumWithTracks where

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
-- Parameters for the @select_album_with_tracks@ query.
--
-- ==== SQL Template
--
-- > select id, name, tracks, disc
-- > from album
-- > where id = $id
--
-- ==== Source Path
--
-- > ./queries/select_album_with_tracks.sql
--
newtype SelectAlbumWithTracks = SelectAlbumWithTracks
  { -- | Maps to @id@.
    id :: Int64
  }
  deriving stock (Eq, Show)

-- | Result of the statement parameterised by 'SelectAlbumWithTracks'.
type SelectAlbumWithTracksResult = Vector.Vector SelectAlbumWithTracksResultRow

-- | Row of 'SelectAlbumWithTracksResult'.
data SelectAlbumWithTracksResultRow = SelectAlbumWithTracksResultRow
  { -- | Maps to @id@.
    id :: Int64,
    -- | Maps to @name@.
    name :: Text,
    -- | Maps to @tracks@.
    tracks :: Maybe (Vector (Types.TrackInfo)),
    -- | Maps to @disc@.
    disc :: Maybe (Types.DiscInfo)
  }
  deriving stock (Show, Eq)

instance IsStatement.IsStatement SelectAlbumWithTracks where
  type Result SelectAlbumWithTracks = SelectAlbumWithTracksResult

  statement = Statement.preparable sql encoder decoder
    where
      sql =
        "select id, name, tracks, disc\n\
        \from album\n\
        \where id = $1"

      encoder =
        mconcat
          [ (.id) >$< Encoders.param (Encoders.nonNullable (IsScalar.encoder))
          ]

      decoder =
        Decoders.rowVector do
          id <- Decoders.column (Decoders.nonNullable (IsScalar.decoder))
          name <- Decoders.column (Decoders.nonNullable (IsScalar.decoder))
          tracks <- Decoders.column (Decoders.nullable (Decoders.array (Decoders.dimension Vector.replicateM (Decoders.element (Decoders.nonNullable IsScalar.decoder)))))
          disc <- Decoders.column (Decoders.nullable (IsScalar.decoder))
          pure SelectAlbumWithTracksResultRow {..}

