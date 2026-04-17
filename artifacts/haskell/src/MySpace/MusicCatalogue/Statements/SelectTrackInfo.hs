module MySpace.MusicCatalogue.Statements.SelectTrackInfo where

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
-- Parameters for the @select_track_info@ query.
--
-- ==== SQL Template
--
-- > select
-- >   (
-- >     'title',
-- >     180,
-- >     array['a', 'b']
-- >   )::track_info as track_info,
-- >   234 as id
--
-- ==== Source Path
--
-- > ./queries/select_track_info.sql
--
data SelectTrackInfo = SelectTrackInfo
  deriving stock (Eq, Show)

-- | Result of the statement parameterised by 'SelectTrackInfo'.
type SelectTrackInfoResult = SelectTrackInfoResultRow

-- | Row of 'SelectTrackInfoResult'.
data SelectTrackInfoResultRow = SelectTrackInfoResultRow
  { -- | Maps to @track_info@.
    trackInfo :: Maybe (Types.TrackInfo),
    -- | Maps to @id@.
    id :: Maybe (Int32)
  }
  deriving stock (Show, Eq)

instance IsStatement.IsStatement SelectTrackInfo where
  type Result SelectTrackInfo = SelectTrackInfoResult

  statement = Statement.preparable sql encoder decoder
    where
      sql =
        "select\n\
        \  (\n\
        \    'title',\n\
        \    180,\n\
        \    array['a', 'b']\n\
        \  )::track_info as track_info,\n\
        \  234 as id"

      encoder =
        mconcat
          [ 
          ]

      decoder =
        Decoders.singleRow do
          trackInfo <- Decoders.column (Decoders.nullable (IsScalar.decoder))
          id <- Decoders.column (Decoders.nullable (IsScalar.decoder))
          pure SelectTrackInfoResultRow {..}

