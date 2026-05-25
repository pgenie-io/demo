module MySpace.MusicCatalogue.Types.TrackInfo where

import MySpace.MusicCatalogue.Prelude
import Test.QuickCheck (Arbitrary (..))
import Test.QuickCheck.Instances ()

import qualified Data.Aeson
import qualified Data.Vector
import qualified Hasql.Decoders
import qualified Hasql.Encoders
import qualified Hasql.Mapping.IsScalar
import qualified PostgresqlTypes


-- |
-- Representation of the @track_info@ user-declared PostgreSQL record type.
data TrackInfo = TrackInfo
  { -- | Maps to @title@.
    title :: Text,
    -- | Maps to @duration_seconds@.
    durationSeconds :: Int32,
    -- | Maps to @tags@.
    tags :: Vector (Text)
  }
  deriving stock (Show, Eq, Ord)

instance Arbitrary TrackInfo where
  arbitrary =
    TrackInfo <$> arbitrary <*> arbitrary <*> arbitrary

instance Hasql.Mapping.IsScalar.IsScalar TrackInfo where
  encoder =
    Hasql.Encoders.composite
      (Just "public")
      "track_info"
      ( mconcat
          [ (.title) >$< Hasql.Encoders.field (Hasql.Encoders.nonNullable (Hasql.Mapping.IsScalar.encoder)),
            (.durationSeconds) >$< Hasql.Encoders.field (Hasql.Encoders.nonNullable (Hasql.Mapping.IsScalar.encoder)),
            (.tags) >$< Hasql.Encoders.field (Hasql.Encoders.nonNullable (Hasql.Encoders.array (Hasql.Encoders.dimension Data.Vector.foldl' (Hasql.Encoders.element (Hasql.Encoders.nonNullable Hasql.Mapping.IsScalar.encoder)))))
          ]
      )
  
  decoder =
    Hasql.Decoders.composite
      (Just "public")
      "track_info"
      ( TrackInfo
          <$> Hasql.Decoders.field (Hasql.Decoders.nonNullable (Hasql.Mapping.IsScalar.decoder))
          <*> Hasql.Decoders.field (Hasql.Decoders.nonNullable (Hasql.Mapping.IsScalar.decoder))
          <*> Hasql.Decoders.field (Hasql.Decoders.nonNullable (Hasql.Decoders.array (Hasql.Decoders.dimension Data.Vector.replicateM (Hasql.Decoders.element (Hasql.Decoders.nonNullable Hasql.Mapping.IsScalar.decoder)))))
      )
