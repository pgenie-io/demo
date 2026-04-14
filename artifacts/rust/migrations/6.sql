-- Add support for hierarchical genre paths.
create extension if not exists ltree;

alter table genre
add column path ltree not null;

create index on genre using gist (path);