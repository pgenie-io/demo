select id, name, path
from genre
where path <@ $path