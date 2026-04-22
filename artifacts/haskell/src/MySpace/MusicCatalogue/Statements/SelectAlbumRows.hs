module MySpace.MusicCatalogue.Statements.SelectAlbumRows where

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
-- Parameters for the @select_album_rows@ query.
--
-- ==== SQL Template
--
-- > select (album.*)::album from album
--
-- ==== Source Path
--
-- > ./queries/select_album_rows.sql
--
data SelectAlbumRows = SelectAlbumRows
  deriving stock (Eq, Show)

-- | Result of the statement parameterised by 'SelectAlbumRows'.
type SelectAlbumRowsResult = Vector.Vector SelectAlbumRowsResultRow

-- | Row of 'SelectAlbumRowsResult'.
newtype SelectAlbumRowsResultRow = SelectAlbumRowsResultRow
  { -- | Maps to @album@.
    album :: Maybe (Types.Album)
  }
  deriving stock (Show, Eq)

instance IsStatement.IsStatement SelectAlbumRows where
  type Result SelectAlbumRows = SelectAlbumRowsResult

  statement = Statement.preparable sql encoder decoder
    where
      sql =
        "select (album.*)::album from album"

      encoder =
        mconcat
          [ 
          ]

      decoder =
        Decoders.rowVector do
          album <- Decoders.column (Decoders.nullable (IsScalar.decoder))
          pure SelectAlbumRowsResultRow {..}

