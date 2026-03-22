module MySpace.MusicCatalogue.Statements.SelectAlbumByName where

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
-- Parameters for the @select_album_by_name@ query.
--
-- ==== SQL Template
--
-- > select 
-- >   id,
-- >   name,
-- >   released,
-- >   format,
-- >   recording
-- > from album
-- > where name = $name
--
-- ==== Source Path
--
-- > ./queries/select_album_by_name.sql
--
newtype SelectAlbumByName = SelectAlbumByName
  { -- | Maps to @name@.
    name :: Text
  }
  deriving stock (Eq, Show)

-- | Result of the statement parameterised by 'SelectAlbumByName'.
type SelectAlbumByNameResult = Vector.Vector SelectAlbumByNameResultRow

-- | Row of 'SelectAlbumByNameResult'.
data SelectAlbumByNameResultRow = SelectAlbumByNameResultRow
  { -- | Maps to @id@.
    id :: Int64,
    -- | Maps to @name@.
    name :: Text,
    -- | Maps to @released@.
    released :: Maybe (Pt.Date),
    -- | Maps to @format@.
    format :: Maybe (Types.AlbumFormat),
    -- | Maps to @recording@.
    recording :: Maybe (Types.RecordingInfo)
  }
  deriving stock (Show, Eq)

instance IsStatement.IsStatement SelectAlbumByName where
  type Result SelectAlbumByName = SelectAlbumByNameResult

  statement = Statement.preparable sql encoder decoder
    where
      sql =
        "select \n\
        \  id,\n\
        \  name,\n\
        \  released,\n\
        \  format,\n\
        \  recording\n\
        \from album\n\
        \where name = $1"

      encoder =
        mconcat
          [ (.name) >$< Encoders.param (Encoders.nonNullable (IsScalar.encoder))
          ]

      decoder =
        Decoders.rowVector do
          id <- Decoders.column (Decoders.nonNullable (IsScalar.decoder))
          name <- Decoders.column (Decoders.nonNullable (IsScalar.decoder))
          released <- Decoders.column (Decoders.nullable (IsScalar.decoder))
          format <- Decoders.column (Decoders.nullable (IsScalar.decoder))
          recording <- Decoders.column (Decoders.nullable (IsScalar.decoder))
          pure SelectAlbumByNameResultRow {..}

