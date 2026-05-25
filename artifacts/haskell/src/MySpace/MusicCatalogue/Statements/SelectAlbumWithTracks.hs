module MySpace.MusicCatalogue.Statements.SelectAlbumWithTracks where

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
type SelectAlbumWithTracksResult = Data.Vector.Vector SelectAlbumWithTracksResultRow

-- | Row of 'SelectAlbumWithTracksResult'.
data SelectAlbumWithTracksResultRow = SelectAlbumWithTracksResultRow
  { -- | Maps to @id@.
    id :: Int64,
    -- | Maps to @name@.
    name :: Text,
    -- | Maps to @tracks@.
    tracks :: Vector (TrackInfo),
    -- | Maps to @disc@.
    disc :: Maybe (DiscInfo)
  }
  deriving stock (Show, Eq)

instance Arbitrary SelectAlbumWithTracks where
  arbitrary =
    SelectAlbumWithTracks
      <$> (PostgresqlTypes.Int8.toInt64 <$> arbitrary)
      
instance Hasql.Mapping.IsStatement.IsStatement SelectAlbumWithTracks where
  type Result SelectAlbumWithTracks = SelectAlbumWithTracksResult

  statement = Hasql.Statement.preparable sql encoder decoder
    where
      sql =
        "select id, name, tracks, disc\n\
        \from album\n\
        \where id = $1"

      encoder =
        mconcat
          [ (.id) >$< Hasql.Encoders.param (Hasql.Encoders.nonNullable (Hasql.Mapping.IsScalar.encoder))
          ]

      decoder =
        Hasql.Decoders.rowVector do
          id <- Hasql.Decoders.column (Hasql.Decoders.nonNullable (Hasql.Mapping.IsScalar.decoder))
          name <- Hasql.Decoders.column (Hasql.Decoders.nonNullable (Hasql.Mapping.IsScalar.decoder))
          tracks <- Hasql.Decoders.column (Hasql.Decoders.nonNullable (Hasql.Decoders.array (Hasql.Decoders.dimension Data.Vector.replicateM (Hasql.Decoders.element (Hasql.Decoders.nonNullable Hasql.Mapping.IsScalar.decoder)))))
          disc <- Hasql.Decoders.column (Hasql.Decoders.nullable (Hasql.Mapping.IsScalar.decoder))
          pure SelectAlbumWithTracksResultRow {..}
