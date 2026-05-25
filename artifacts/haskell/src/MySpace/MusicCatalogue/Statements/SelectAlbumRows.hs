module MySpace.MusicCatalogue.Statements.SelectAlbumRows where

import MySpace.MusicCatalogue.Prelude
import Test.QuickCheck
import qualified Hasql.Statement
import qualified Hasql.Decoders
import qualified Hasql.Encoders
import qualified Data.Aeson
import qualified Data.Vector
import qualified Hasql.Mapping.IsStatement
import qualified Hasql.Mapping.IsScalar
import MySpace.MusicCatalogue.Types
import qualified PostgresqlTypes
import qualified PostgresqlTypes.Date
import qualified PostgresqlTypes.Bytea
import qualified PostgresqlTypes.Numeric
import qualified PostgresqlTypes.Float4
import qualified PostgresqlTypes.Float8
import qualified PostgresqlTypes.Int2
import qualified PostgresqlTypes.Int4
import qualified PostgresqlTypes.Int8
import qualified PostgresqlTypes.Text
import qualified PostgresqlTypes.Uuid

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
type SelectAlbumRowsResult = Data.Vector.Vector SelectAlbumRowsResultRow

-- | Row of 'SelectAlbumRowsResult'.
newtype SelectAlbumRowsResultRow = SelectAlbumRowsResultRow
  { -- | Maps to @album@.
    album :: Maybe (Album)
  }
  deriving stock (Show, Eq)

instance Arbitrary SelectAlbumRows where
  arbitrary =
    pure SelectAlbumRows
instance Hasql.Mapping.IsStatement.IsStatement SelectAlbumRows where
  type Result SelectAlbumRows = SelectAlbumRowsResult

  statement = Hasql.Statement.preparable sql encoder decoder
    where
      sql =
        "select (album.*)::album from album"

      encoder =
        mconcat
          [ 
          ]

      decoder =
        Hasql.Decoders.rowVector do
          album <- Hasql.Decoders.column (Hasql.Decoders.nullable (Hasql.Mapping.IsScalar.decoder))
          pure SelectAlbumRowsResultRow {..}
