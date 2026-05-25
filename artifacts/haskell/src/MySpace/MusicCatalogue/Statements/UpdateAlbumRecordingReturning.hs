module MySpace.MusicCatalogue.Statements.UpdateAlbumRecordingReturning where

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
-- Parameters for the @update_album_recording_returning@ query.
--
-- ==== SQL Template
--
-- > -- Update album recording information
-- > update album
-- > set recording = $recording
-- > where id = $id
-- > returning *
--
-- ==== Source Path
--
-- > ./queries/update_album_recording_returning.sql
--
data UpdateAlbumRecordingReturning = UpdateAlbumRecordingReturning
  { -- | Maps to @recording@.
    recording :: Maybe (RecordingInfo),
    -- | Maps to @id@.
    id :: Int64
  }
  deriving stock (Eq, Show)

-- | Result of the statement parameterised by 'UpdateAlbumRecordingReturning'.
type UpdateAlbumRecordingReturningResult = Data.Vector.Vector UpdateAlbumRecordingReturningResultRow

-- | Row of 'UpdateAlbumRecordingReturningResult'.
data UpdateAlbumRecordingReturningResultRow = UpdateAlbumRecordingReturningResultRow
  { -- | Maps to @id@.
    id :: Int64,
    -- | Maps to @name@.
    name :: Text,
    -- | Maps to @released@.
    released :: Maybe (PostgresqlTypes.Date),
    -- | Maps to @format@.
    format :: Maybe (AlbumFormat),
    -- | Maps to @recording@.
    recording :: Maybe (RecordingInfo),
    -- | Maps to @tracks@.
    tracks :: Maybe (Vector (TrackInfo)),
    -- | Maps to @disc@.
    disc :: Maybe (DiscInfo)
  }
  deriving stock (Show, Eq)

instance Arbitrary UpdateAlbumRecordingReturning where
  arbitrary =
    UpdateAlbumRecordingReturning
      <$> (liftArbitrary arbitrary)
      <*> (PostgresqlTypes.Int8.toInt64 <$> arbitrary)
      
instance Hasql.Mapping.IsStatement.IsStatement UpdateAlbumRecordingReturning where
  type Result UpdateAlbumRecordingReturning = UpdateAlbumRecordingReturningResult

  statement = Hasql.Statement.preparable sql encoder decoder
    where
      sql =
        "-- Update album recording information\n\
        \update album\n\
        \set recording = $1\n\
        \where id = $2\n\
        \returning *"

      encoder =
        mconcat
          [ (.recording) >$< Hasql.Encoders.param (Hasql.Encoders.nullable (Hasql.Mapping.IsScalar.encoder)),
            (.id) >$< Hasql.Encoders.param (Hasql.Encoders.nonNullable (Hasql.Mapping.IsScalar.encoder))
          ]

      decoder =
        Hasql.Decoders.rowVector do
          id <- Hasql.Decoders.column (Hasql.Decoders.nonNullable (Hasql.Mapping.IsScalar.decoder))
          name <- Hasql.Decoders.column (Hasql.Decoders.nonNullable (Hasql.Mapping.IsScalar.decoder))
          released <- Hasql.Decoders.column (Hasql.Decoders.nullable (Hasql.Mapping.IsScalar.decoder))
          format <- Hasql.Decoders.column (Hasql.Decoders.nullable (Hasql.Mapping.IsScalar.decoder))
          recording <- Hasql.Decoders.column (Hasql.Decoders.nullable (Hasql.Mapping.IsScalar.decoder))
          tracks <- Hasql.Decoders.column (Hasql.Decoders.nullable (Hasql.Decoders.array (Hasql.Decoders.dimension Data.Vector.replicateM (Hasql.Decoders.element (Hasql.Decoders.nonNullable Hasql.Mapping.IsScalar.decoder)))))
          disc <- Hasql.Decoders.column (Hasql.Decoders.nullable (Hasql.Mapping.IsScalar.decoder))
          pure UpdateAlbumRecordingReturningResultRow {..}
