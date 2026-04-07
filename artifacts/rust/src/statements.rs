//! Mappings to all queries in the project.
//!
//! Each sub-module exposes a parameter struct that implements [`crate::mapping::Statement`].

pub mod insert_album;
pub mod insert_multiple_albums;
pub mod select_album_by_format;
pub mod select_album_by_id;
pub mod select_album_by_name;
pub mod select_album_fields;
pub mod select_album_with_filters;
pub mod select_album_with_tracks;
pub mod select_genre_by_artist;
pub mod update_album_recording_returning;
pub mod update_album_released;
