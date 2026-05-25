module MySpace.MusicCatalogue.Types.RecordingInfo where

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
-- Representation of the @recording_info@ user-declared PostgreSQL record type.
data RecordingInfo = RecordingInfo
  { -- | Maps to @studio_name@.
    studioName :: Text,
    -- | Maps to @city@.
    city :: Text,
    -- | Maps to @country@.
    country :: Text,
    -- | Maps to @recorded_date@.
    recordedDate :: PostgresqlTypes.Date
  }
  deriving stock (Show, Eq, Ord)

instance Arbitrary RecordingInfo where
  arbitrary =
    RecordingInfo <$> arbitrary <*> arbitrary <*> arbitrary <*> arbitrary

instance Hasql.Mapping.IsScalar.IsScalar RecordingInfo where
  encoder =
    Hasql.Encoders.composite
      (Just "public")
      "recording_info"
      ( mconcat
          [ (.studioName) >$< Hasql.Encoders.field (Hasql.Encoders.nonNullable (Hasql.Mapping.IsScalar.encoder)),
            (.city) >$< Hasql.Encoders.field (Hasql.Encoders.nonNullable (Hasql.Mapping.IsScalar.encoder)),
            (.country) >$< Hasql.Encoders.field (Hasql.Encoders.nonNullable (Hasql.Mapping.IsScalar.encoder)),
            (.recordedDate) >$< Hasql.Encoders.field (Hasql.Encoders.nonNullable (Hasql.Mapping.IsScalar.encoder))
          ]
      )
  
  decoder =
    Hasql.Decoders.composite
      (Just "public")
      "recording_info"
      ( RecordingInfo
          <$> Hasql.Decoders.field (Hasql.Decoders.nonNullable (Hasql.Mapping.IsScalar.decoder))
          <*> Hasql.Decoders.field (Hasql.Decoders.nonNullable (Hasql.Mapping.IsScalar.decoder))
          <*> Hasql.Decoders.field (Hasql.Decoders.nonNullable (Hasql.Mapping.IsScalar.decoder))
          <*> Hasql.Decoders.field (Hasql.Decoders.nonNullable (Hasql.Mapping.IsScalar.decoder))
      )
