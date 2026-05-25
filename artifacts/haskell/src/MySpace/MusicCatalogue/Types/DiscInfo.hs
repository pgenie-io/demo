module MySpace.MusicCatalogue.Types.DiscInfo where

import MySpace.MusicCatalogue.Prelude
import Test.QuickCheck (Arbitrary (..))
import Test.QuickCheck.Instances ()

import qualified Data.Aeson
import qualified Data.Vector
import qualified Hasql.Decoders
import qualified Hasql.Encoders
import qualified Hasql.Mapping.IsScalar
import qualified PostgresqlTypes
import MySpace.MusicCatalogue.Types.RecordingInfo

-- |
-- Representation of the @disc_info@ user-declared PostgreSQL record type.
data DiscInfo = DiscInfo
  { -- | Maps to @name@.
    name :: Maybe (Text),
    -- | Maps to @recording@.
    recording :: Maybe (RecordingInfo)
  }
  deriving stock (Show, Eq, Ord)

instance Arbitrary DiscInfo where
  arbitrary =
    DiscInfo <$> arbitrary <*> arbitrary

instance Hasql.Mapping.IsScalar.IsScalar DiscInfo where
  encoder =
    Hasql.Encoders.composite
      (Just "public")
      "disc_info"
      ( mconcat
          [ (.name) >$< Hasql.Encoders.field (Hasql.Encoders.nullable (Hasql.Mapping.IsScalar.encoder)),
            (.recording) >$< Hasql.Encoders.field (Hasql.Encoders.nullable (Hasql.Mapping.IsScalar.encoder))
          ]
      )
  
  decoder =
    Hasql.Decoders.composite
      (Just "public")
      "disc_info"
      ( DiscInfo
          <$> Hasql.Decoders.field (Hasql.Decoders.nullable (Hasql.Mapping.IsScalar.decoder))
          <*> Hasql.Decoders.field (Hasql.Decoders.nullable (Hasql.Mapping.IsScalar.decoder))
      )
