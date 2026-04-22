module MySpace.MusicCatalogue.Types.Album where

import MySpace.MusicCatalogue.Prelude
import qualified Data.Aeson as Aeson
import qualified Data.Vector as Vector
import qualified Hasql.Decoders as Decoders
import qualified Hasql.Encoders as Encoders
import qualified Hasql.Mapping.IsScalar as IsScalar
import qualified PostgresqlTypes as Pt
import qualified MySpace.MusicCatalogue.Types.AlbumFormat as Types
import qualified MySpace.MusicCatalogue.Types.RecordingInfo as Types
import qualified MySpace.MusicCatalogue.Types.TrackInfo as Types
import qualified MySpace.MusicCatalogue.Types.DiscInfo as Types

-- |
-- Representation of the @album@ user-declared PostgreSQL record type.
data Album = Album
  { -- | Maps to @id@.
    id :: Maybe (Int64),
    -- | Maps to @name@.
    name :: Maybe (Text),
    -- | Maps to @released@.
    released :: Maybe (Pt.Date),
    -- | Maps to @format@.
    format :: Maybe (Types.AlbumFormat),
    -- | Maps to @recording@.
    recording :: Maybe (Types.RecordingInfo),
    -- | Maps to @tracks@.
    tracks :: Maybe (Vector (Maybe Types.TrackInfo)),
    -- | Maps to @disc@.
    disc :: Maybe (Types.DiscInfo)
  }
  deriving stock (Show, Eq, Ord)

instance IsScalar.IsScalar Album where
  encoder =
    Encoders.composite
      (Just "public")
      "album"
      ( mconcat
          [ (.id) >$< Encoders.field (Encoders.nullable (IsScalar.encoder)),
            (.name) >$< Encoders.field (Encoders.nullable (IsScalar.encoder)),
            (.released) >$< Encoders.field (Encoders.nullable (IsScalar.encoder)),
            (.format) >$< Encoders.field (Encoders.nullable (IsScalar.encoder)),
            (.recording) >$< Encoders.field (Encoders.nullable (IsScalar.encoder)),
            (.tracks) >$< Encoders.field (Encoders.nullable (Encoders.array (Encoders.dimension Vector.foldl' (Encoders.element (Encoders.nullable IsScalar.encoder))))),
            (.disc) >$< Encoders.field (Encoders.nullable (IsScalar.encoder))
          ]
      )
  
  decoder =
    Decoders.composite
      (Just "public")
      "album"
      ( Album
          <$> Decoders.field (Decoders.nullable (IsScalar.decoder))
          <*> Decoders.field (Decoders.nullable (IsScalar.decoder))
          <*> Decoders.field (Decoders.nullable (IsScalar.decoder))
          <*> Decoders.field (Decoders.nullable (IsScalar.decoder))
          <*> Decoders.field (Decoders.nullable (IsScalar.decoder))
          <*> Decoders.field (Decoders.nullable (Decoders.array (Decoders.dimension Vector.replicateM (Decoders.element (Decoders.nullable IsScalar.decoder)))))
          <*> Decoders.field (Decoders.nullable (IsScalar.decoder))
      )
  
