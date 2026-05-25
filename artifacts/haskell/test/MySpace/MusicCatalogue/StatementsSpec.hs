module MySpace.MusicCatalogue.StatementsSpec (spec) where

import qualified Hasql.Pool
import Test.Hspec
import qualified MySpace.MusicCatalogue.Statements.InsertAlbumSpec
import qualified MySpace.MusicCatalogue.Statements.InsertMultipleAlbumsSpec
import qualified MySpace.MusicCatalogue.Statements.SelectAlbumByFormatSpec
import qualified MySpace.MusicCatalogue.Statements.SelectAlbumByIdSpec
import qualified MySpace.MusicCatalogue.Statements.SelectAlbumByNameSpec
import qualified MySpace.MusicCatalogue.Statements.SelectAlbumRowsSpec
import qualified MySpace.MusicCatalogue.Statements.SelectAlbumWithFiltersSpec
import qualified MySpace.MusicCatalogue.Statements.SelectAlbumWithTracksSpec
import qualified MySpace.MusicCatalogue.Statements.SelectGenreByArtistSpec
import qualified MySpace.MusicCatalogue.Statements.UpdateAlbumRecordingReturningSpec
import qualified MySpace.MusicCatalogue.Statements.UpdateAlbumReleasedSpec

spec :: SpecWith Hasql.Pool.Pool
spec = parallel $ describe "Statements" $ do
  describe "InsertAlbum" MySpace.MusicCatalogue.Statements.InsertAlbumSpec.spec
  describe "InsertMultipleAlbums" MySpace.MusicCatalogue.Statements.InsertMultipleAlbumsSpec.spec
  describe "SelectAlbumByFormat" MySpace.MusicCatalogue.Statements.SelectAlbumByFormatSpec.spec
  describe "SelectAlbumById" MySpace.MusicCatalogue.Statements.SelectAlbumByIdSpec.spec
  describe "SelectAlbumByName" MySpace.MusicCatalogue.Statements.SelectAlbumByNameSpec.spec
  describe "SelectAlbumRows" MySpace.MusicCatalogue.Statements.SelectAlbumRowsSpec.spec
  describe "SelectAlbumWithFilters" MySpace.MusicCatalogue.Statements.SelectAlbumWithFiltersSpec.spec
  describe "SelectAlbumWithTracks" MySpace.MusicCatalogue.Statements.SelectAlbumWithTracksSpec.spec
  describe "SelectGenreByArtist" MySpace.MusicCatalogue.Statements.SelectGenreByArtistSpec.spec
  describe "UpdateAlbumRecordingReturning" MySpace.MusicCatalogue.Statements.UpdateAlbumRecordingReturningSpec.spec
  describe "UpdateAlbumReleased" MySpace.MusicCatalogue.Statements.UpdateAlbumReleasedSpec.spec
