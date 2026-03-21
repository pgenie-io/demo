module MySpace.MusicCatalogue.Types.RecordingInfo where

import MySpace.MusicCatalogue.Prelude
import qualified Data.Aeson as Aeson
import qualified Data.Vector as Vector
import qualified Hasql.Decoders as Decoders
import qualified Hasql.Encoders as Encoders
import qualified Hasql.Mapping.IsScalar as IsScalar
import qualified PostgresqlTypes as Pt


-- |
-- Representation of the @recording_info@ user-declared PostgreSQL record type.
data RecordingInfo = RecordingInfo
  { -- | Maps to @studio_name@.
    studioName :: Maybe (Text),
    -- | Maps to @city@.
    city :: Maybe (Text),
    -- | Maps to @country@.
    country :: Maybe (Text),
    -- | Maps to @recorded_date@.
    recordedDate :: Maybe (Pt.Date)
  }
  deriving stock (Show, Eq, Ord)

instance IsScalar.IsScalar RecordingInfo where
  encoder =
    Encoders.composite
      (Just "public")
      "recording_info"
      ( mconcat
          [ (.studioName) >$< Encoders.field (Encoders.nullable (IsScalar.encoder)),
            (.city) >$< Encoders.field (Encoders.nullable (IsScalar.encoder)),
            (.country) >$< Encoders.field (Encoders.nullable (IsScalar.encoder)),
            (.recordedDate) >$< Encoders.field (Encoders.nullable (IsScalar.encoder))
          ]
      )
  
  decoder =
    Decoders.composite
      (Just "public")
      "recording_info"
      ( RecordingInfo
          <$> Decoders.field (Decoders.nullable (IsScalar.decoder))
          <*> Decoders.field (Decoders.nullable (IsScalar.decoder))
          <*> Decoders.field (Decoders.nullable (IsScalar.decoder))
          <*> Decoders.field (Decoders.nullable (IsScalar.decoder))
      )
  
