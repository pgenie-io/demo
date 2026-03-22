module MySpace.MusicCatalogue.Statements.SelectGenreByArtist where

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
type SelectGenreByArtistResult = Vector.Vector SelectGenreByArtistResultRow

-- | Row of 'SelectGenreByArtistResult'.
data SelectGenreByArtistResultRow = SelectGenreByArtistResultRow
  { -- | Maps to @id@.
    id :: Int32,
    -- | Maps to @name@.
    name :: Text
  }
  deriving stock (Show, Eq)

instance IsStatement.IsStatement SelectGenreByArtist where
  type Result SelectGenreByArtist = SelectGenreByArtistResult

  statement = Statement.preparable sql encoder decoder
    where
      sql =
        "select id, genre.name\n\
        \from genre\n\
        \left join album_genre on album_genre.genre = genre.id\n\
        \left join album_artist on album_artist.album = album_genre.album\n\
        \where album_artist.artist = $1"

      encoder =
        mconcat
          [ (.artist) >$< Encoders.param (Encoders.nonNullable (IsScalar.encoder))
          ]

      decoder =
        Decoders.rowVector do
          id <- Decoders.column (Decoders.nonNullable (IsScalar.decoder))
          name <- Decoders.column (Decoders.nonNullable (IsScalar.decoder))
          pure SelectGenreByArtistResultRow {..}

