
create table "genre" (
  "id" int4 not null generated always as identity primary key,
  "name" text not null unique
);

create table "artist" (
  "id" int4 not null generated always as identity primary key,
  "name" text not null unique
);

create table "artist_genre" (
  "artist" int4 not null references "artist",
  "genre" int4 not null references "genre"
);

create table "album" (
  "id" int4 not null generated always as identity primary key,
  -- Album name.
  "name" text not null unique,
  -- The date the album was first released.
  "released" date not null
);

create table "album_artist" (
  "album" int4 not null references "album",
  "artist" int4 not null references "artist",
  -- Whether it is the primary artist
  "primary" bool not null,
  primary key ("album", "artist")
);
