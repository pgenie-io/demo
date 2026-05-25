module MySpace.MusicCatalogue.Statements.SelectAlbumById where

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
    released :: Maybe (PostgresqlTypes.Date),
    -- | Maps to @format@.
    format :: Maybe (AlbumFormat),
    -- | Maps to @recording@.
    recording :: Maybe (RecordingInfo),
    -- | Maps to @tracks@.
    tracks :: Maybe (Vector (Maybe (TrackInfo))),
    -- | Maps to @disc@.
    disc :: Maybe (DiscInfo)
  }
  deriving stock (Show, Eq)

instance Arbitrary SelectAlbumById where
  arbitrary =
    SelectAlbumById
      <$> (liftArbitrary (PostgresqlTypes.Int8.toInt64 <$> arbitrary))
      
instance Hasql.Mapping.IsStatement.IsStatement SelectAlbumById where
  type Result SelectAlbumById = SelectAlbumByIdResult

  statement = Hasql.Statement.preparable sql encoder decoder
    where
      sql =
        "-- Example of a query selecting 0 or 1 row.\n\
        \select *\n\
        \from album\n\
        \where id = $1\n\
        \limit 1"

      encoder =
        mconcat
          [ (.id) >$< Hasql.Encoders.param (Hasql.Encoders.nullable (Hasql.Mapping.IsScalar.encoder))
          ]

      decoder =
        Hasql.Decoders.rowMaybe do
          id <- Hasql.Decoders.column (Hasql.Decoders.nonNullable (Hasql.Mapping.IsScalar.decoder))
          name <- Hasql.Decoders.column (Hasql.Decoders.nonNullable (Hasql.Mapping.IsScalar.decoder))
          released <- Hasql.Decoders.column (Hasql.Decoders.nullable (Hasql.Mapping.IsScalar.decoder))
          format <- Hasql.Decoders.column (Hasql.Decoders.nullable (Hasql.Mapping.IsScalar.decoder))
          recording <- Hasql.Decoders.column (Hasql.Decoders.nullable (Hasql.Mapping.IsScalar.decoder))
          tracks <- Hasql.Decoders.column (Hasql.Decoders.nullable (Hasql.Decoders.array (Hasql.Decoders.dimension Data.Vector.replicateM (Hasql.Decoders.element (Hasql.Decoders.nullable Hasql.Mapping.IsScalar.decoder)))))
          disc <- Hasql.Decoders.column (Hasql.Decoders.nullable (Hasql.Mapping.IsScalar.decoder))
          pure SelectAlbumByIdResultRow {..}
