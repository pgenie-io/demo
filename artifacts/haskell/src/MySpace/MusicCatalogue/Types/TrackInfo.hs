module MySpace.MusicCatalogue.Types.TrackInfo where

import MySpace.MusicCatalogue.Prelude
import qualified Data.Aeson as Aeson
import qualified Data.Vector as Vector
import qualified Hasql.Decoders as Decoders
import qualified Hasql.Encoders as Encoders
import qualified Hasql.Mapping.IsScalar as IsScalar
import qualified PostgresqlTypes as Pt


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

instance IsScalar.IsScalar TrackInfo where
  encoder =
    Encoders.composite
      (Just "public")
      "track_info"
      ( mconcat
          [ (.title) >$< Encoders.field (Encoders.nonNullable (IsScalar.encoder)),
            (.durationSeconds) >$< Encoders.field (Encoders.nonNullable (IsScalar.encoder)),
            (.tags) >$< Encoders.field (Encoders.nonNullable (Encoders.array (Encoders.dimension Vector.foldl' (Encoders.element (Encoders.nonNullable IsScalar.encoder)))))
          ]
      )
  
  decoder =
    Decoders.composite
      (Just "public")
      "track_info"
      ( TrackInfo
          <$> Decoders.field (Decoders.nonNullable (IsScalar.decoder))
          <*> Decoders.field (Decoders.nonNullable (IsScalar.decoder))
          <*> Decoders.field (Decoders.nonNullable (Decoders.array (Decoders.dimension Vector.replicateM (Decoders.element (Decoders.nonNullable IsScalar.decoder)))))
      )
  
