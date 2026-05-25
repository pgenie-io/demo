module MySpace.MusicCatalogue.Statements.SelectGenreByArtist where

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
-- Parameters for the @select_genre_by_artist@ query.
--
-- ==== SQL Template
--
-- > select id, genre.name
-- > from genre
-- > left join album_genre on album_genre.genre = genre.id
-- > left join album_artist on album_artist.album = album_genre.album
-- > where album_artist.artist = $artist
--
-- ==== Source Path
--
-- > ./queries/select_genre_by_artist.sql
--
newtype SelectGenreByArtist = SelectGenreByArtist
  { -- | Maps to @artist@.
    artist :: Int32
  }
  deriving stock (Eq, Show)

-- | Result of the statement parameterised by 'SelectGenreByArtist'.
type SelectGenreByArtistResult = Data.Vector.Vector SelectGenreByArtistResultRow

-- | Row of 'SelectGenreByArtistResult'.
data SelectGenreByArtistResultRow = SelectGenreByArtistResultRow
  { -- | Maps to @id@.
    id :: Int32,
    -- | Maps to @name@.
    name :: Text
  }
  deriving stock (Show, Eq)

instance Arbitrary SelectGenreByArtist where
  arbitrary =
    SelectGenreByArtist
      <$> (PostgresqlTypes.Int4.toInt32 <$> arbitrary)
      
instance Hasql.Mapping.IsStatement.IsStatement SelectGenreByArtist where
  type Result SelectGenreByArtist = SelectGenreByArtistResult

  statement = Hasql.Statement.preparable sql encoder decoder
    where
      sql =
        "select id, genre.name\n\
        \from genre\n\
        \left join album_genre on album_genre.genre = genre.id\n\
        \left join album_artist on album_artist.album = album_genre.album\n\
        \where album_artist.artist = $1"

      encoder =
        mconcat
          [ (.artist) >$< Hasql.Encoders.param (Hasql.Encoders.nonNullable (Hasql.Mapping.IsScalar.encoder))
          ]

      decoder =
        Hasql.Decoders.rowVector do
          id <- Hasql.Decoders.column (Hasql.Decoders.nonNullable (Hasql.Mapping.IsScalar.decoder))
          name <- Hasql.Decoders.column (Hasql.Decoders.nonNullable (Hasql.Mapping.IsScalar.decoder))
          pure SelectGenreByArtistResultRow {..}
