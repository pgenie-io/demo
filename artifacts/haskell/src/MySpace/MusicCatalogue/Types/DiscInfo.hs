module MySpace.MusicCatalogue.Types.DiscInfo where

import MySpace.MusicCatalogue.Prelude
import qualified Data.Aeson as Aeson
import qualified Data.Vector as Vector
import qualified Hasql.Decoders as Decoders
import qualified Hasql.Encoders as Encoders
import qualified Hasql.Mapping.IsScalar as IsScalar
import qualified PostgresqlTypes as Pt
import qualified MySpace.MusicCatalogue.Types.RecordingInfo as Types

-- |
-- Representation of the @disc_info@ user-declared PostgreSQL record type.
data DiscInfo = DiscInfo
  { -- | Maps to @name@.
    name :: Maybe (Text),
    -- | Maps to @recording@.
    recording :: Maybe (Types.RecordingInfo)
  }
  deriving stock (Show, Eq, Ord)

instance IsScalar.IsScalar DiscInfo where
  encoder =
    Encoders.composite
      (Just "public")
      "disc_info"
      ( mconcat
          [ (.name) >$< Encoders.field (Encoders.nullable (IsScalar.encoder)),
            (.recording) >$< Encoders.field (Encoders.nullable (IsScalar.encoder))
          ]
      )
  
  decoder =
    Decoders.composite
      (Just "public")
      "disc_info"
      ( DiscInfo
          <$> Decoders.field (Decoders.nullable (IsScalar.decoder))
          <*> Decoders.field (Decoders.nullable (IsScalar.decoder))
      )
  
