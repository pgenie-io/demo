module MySpace.MusicCatalogue.Statements.InsertAlbum where

import MySpace.MusicCatalogue.Prelude
import qualified Hasql.Statement as Statement
import qualified Hasql.Decoders as Decoders
import qualified Hasql.Encoders as Encoders
import qualified Data.Aeson as Aeson
import qualified Data.Vector as Vector
import qualified Hasql.Mapping.IsStatement as IsStatement
import qualified Hasql.Mapping.IsScalar as IsScalar
import qualified MySpace.MusicCatalogue.Types as Types
import qualified PostgresqlTypes as Pt

-- |
-- Parameters for the @insert_album@ query.
--
-- ==== SQL Template
--
-- > insert into album (name, released, format, recording)
-- > values ($name, $released, $format, $recording)
-- > returning id
--
-- ==== Source Path
--
-- > ./queries/insert_album.sql
--
data InsertAlbum = InsertAlbum
  { -- | Maps to @name@.
    name :: Text,
    -- | Maps to @released@.
    released :: Pt.Date,
    -- | Maps to @format@.
    format :: Types.AlbumFormat,
    -- | Maps to @recording@.
    recording :: Types.RecordingInfo
  }
  deriving stock (Eq, Show)

-- | Result of the statement parameterised by 'InsertAlbum'.
type InsertAlbumResult = InsertAlbumResultRow

-- | Row of 'InsertAlbumResult'.
newtype InsertAlbumResultRow = InsertAlbumResultRow
  { -- | Maps to @id@.
    id :: Int64
  }
  deriving stock (Show, Eq)

instance IsStatement.IsStatement InsertAlbum where
  type Result InsertAlbum = InsertAlbumResult

  statement = Statement.preparable sql encoder decoder
    where
      sql =
        "insert into album (name, released, format, recording)\n\
        \values ($1, $2, $3, $4)\n\
        \returning id"

      encoder =
        mconcat
          [ (.name) >$< Encoders.param (Encoders.nonNullable (IsScalar.encoder)),
            (.released) >$< Encoders.param (Encoders.nonNullable (IsScalar.encoder)),
            (.format) >$< Encoders.param (Encoders.nonNullable (IsScalar.encoder)),
            (.recording) >$< Encoders.param (Encoders.nonNullable (IsScalar.encoder))
          ]

      decoder =
        Decoders.singleRow do
          id <- Decoders.column (Decoders.nonNullable (IsScalar.decoder))
          pure InsertAlbumResultRow {..}

