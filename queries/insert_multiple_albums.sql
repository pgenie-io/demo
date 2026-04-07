-- This is an example of a bulk-insert (batch-insert) technique.
-- We pass in all fields as arrays of the same size, and we unnest it to insert multiple rows at once.
insert into album (name, released, format)
select *
from unnest(
  $name::text[],
  $released::date[],
  $format::album_format[]
)
returning id
