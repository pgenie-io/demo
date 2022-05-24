select id, genre.name
from artist_genre
left join genre on genre.id = artist_genre.genre
where artist = $artist
