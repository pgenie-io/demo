-- Demonstrates static query equivalent of dynamic field selection.
-- Boolean flags control which fields are included in the result,
-- returning NULL for fields the caller opts out of.
SELECT
  album.id,
  CASE WHEN $include_name      THEN album.name      END AS name,
  CASE WHEN $include_released  THEN album.released  END AS released,
  CASE WHEN $include_format    THEN album.format    END AS format,
  CASE WHEN $include_recording THEN album.recording END AS recording,
  CASE WHEN $include_tracks    THEN album.tracks    END AS tracks,
  CASE WHEN $include_disc      THEN album.disc      END AS disc
FROM album
WHERE album.id = $id
