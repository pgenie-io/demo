module MySpace.MusicCatalogue.Types.Album where

import MySpace.MusicCatalogue.Prelude
import Test.QuickCheck (Arbitrary (..))
import Test.QuickCheck.Instances ()

import qualified Data.Aeson
import qualified Data.Vector
import qualified Hasql.Decoders
import qualified Hasql.Encoders
import qualified Hasql.Mapping.IsScalar
import qualified PostgresqlTypes
import MySpace.MusicCatalogue.Types.AlbumFormat
import MySpace.MusicCatalogue.Types.RecordingInfo
import MySpace.MusicCatalogue.Types.TrackInfo
import MySpace.MusicCatalogue.Types.DiscInfo

-- |
-- Representation of the @album@ user-declared PostgreSQL record type.
data Album = Album
  { -- | Maps to @id@.
    id :: Maybe (Int64),
    -- | Maps to @name@.
    name :: Maybe (Text),
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
  deriving stock (Show, Eq, Ord)

instance Arbitrary Album where
  arbitrary =
    Album <$> arbitrary <*> arbitrary <*> arbitrary <*> arbitrary <*> arbitrary <*> arbitrary <*> arbitrary

instance Hasql.Mapping.IsScalar.IsScalar Album where
  encoder =
    Hasql.Encoders.composite
      (Just "public")
      "album"
      ( mconcat
          [ (.id) >$< Hasql.Encoders.field (Hasql.Encoders.nullable (Hasql.Mapping.IsScalar.encoder)),
            (.name) >$< Hasql.Encoders.field (Hasql.Encoders.nullable (Hasql.Mapping.IsScalar.encoder)),
            (.released) >$< Hasql.Encoders.field (Hasql.Encoders.nullable (Hasql.Mapping.IsScalar.encoder)),
            (.format) >$< Hasql.Encoders.field (Hasql.Encoders.nullable (Hasql.Mapping.IsScalar.encoder)),
            (.recording) >$< Hasql.Encoders.field (Hasql.Encoders.nullable (Hasql.Mapping.IsScalar.encoder)),
            (.tracks) >$< Hasql.Encoders.field (Hasql.Encoders.nullable (Hasql.Encoders.array (Hasql.Encoders.dimension Data.Vector.foldl' (Hasql.Encoders.element (Hasql.Encoders.nullable Hasql.Mapping.IsScalar.encoder))))),
            (.disc) >$< Hasql.Encoders.field (Hasql.Encoders.nullable (Hasql.Mapping.IsScalar.encoder))
          ]
      )
  
  decoder =
    Hasql.Decoders.composite
      (Just "public")
      "album"
      ( Album
          <$> Hasql.Decoders.field (Hasql.Decoders.nullable (Hasql.Mapping.IsScalar.decoder))
          <*> Hasql.Decoders.field (Hasql.Decoders.nullable (Hasql.Mapping.IsScalar.decoder))
          <*> Hasql.Decoders.field (Hasql.Decoders.nullable (Hasql.Mapping.IsScalar.decoder))
          <*> Hasql.Decoders.field (Hasql.Decoders.nullable (Hasql.Mapping.IsScalar.decoder))
          <*> Hasql.Decoders.field (Hasql.Decoders.nullable (Hasql.Mapping.IsScalar.decoder))
          <*> Hasql.Decoders.field (Hasql.Decoders.nullable (Hasql.Decoders.array (Hasql.Decoders.dimension Data.Vector.replicateM (Hasql.Decoders.element (Hasql.Decoders.nullable Hasql.Mapping.IsScalar.decoder)))))
          <*> Hasql.Decoders.field (Hasql.Decoders.nullable (Hasql.Mapping.IsScalar.decoder))
      )
