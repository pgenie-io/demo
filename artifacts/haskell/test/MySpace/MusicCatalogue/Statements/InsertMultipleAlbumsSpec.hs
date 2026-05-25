module MySpace.MusicCatalogue.Statements.InsertMultipleAlbumsSpec (spec) where

import Test.Hspec
import Test.QuickCheck
import Test.QuickCheck.Instances ()

import qualified Data.Text
import qualified Data.Vector
import qualified Hasql.Pool
import qualified Hasql.Mapping.IsStatement
import qualified Hasql.Session
import qualified MySpace.MusicCatalogue.Statements

spec :: SpecWith Hasql.Pool.Pool
spec = do
  it "executes with arbitrary parameters" \pool ->
    property \(statementParams :: MySpace.MusicCatalogue.Statements.InsertMultipleAlbums) ->
      ioProperty do
        result <- Hasql.Pool.use pool (Hasql.Session.statement statementParams Hasql.Mapping.IsStatement.statement)
        case result of
          Left err -> fail (show err)
          Right _ -> pure True
