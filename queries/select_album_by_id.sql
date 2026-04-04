-- Example of a query selecting 0 or 1 row.
select *
from album
where id = $id
limit 1
