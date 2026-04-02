SELECT
  album.id,
  album.name,
  album.released,
  album.format
FROM album
LEFT JOIN album_artist ON album_artist.album = album.id
LEFT JOIN artist ON artist.id = album_artist.artist
LEFT JOIN album_genre ON album_genre.album = album.id
LEFT JOIN genre ON genre.id = album_genre.genre
WHERE
  ($artist_name::text IS NULL OR artist.name = $artist_name)
  AND ($genre_name::text IS NULL OR genre.name = $genre_name)
  AND ($format::album_format IS NULL OR album.format = $format)
  AND ($released_after::timestamp IS NULL OR album.released >= $released_after)
  AND ($name_like::text IS NULL OR album.name LIKE $name_like)
ORDER BY
  CASE WHEN $order_by_name THEN album.name END ASC,
  CASE WHEN $order_by_released THEN album.released END DESC
