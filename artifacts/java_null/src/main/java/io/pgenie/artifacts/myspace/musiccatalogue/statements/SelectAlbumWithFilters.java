package io.pgenie.artifacts.myspace.musiccatalogue.statements;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import io.codemine.java.postgresql.jdbc.Codec;
import io.codemine.java.postgresql.jdbc.Statement;
import io.pgenie.artifacts.myspace.musiccatalogue.types.*;

/**
 * Type-safe binding for the {@code select_album_with_filters} query.
 *
 * <h2>SQL Template</h2>
 *
 * <pre>{@code
 * -- Demonstrates static query equivalent of dynamic field selection.
 * -- Boolean flags control which fields are included in the result,
 * -- returning NULL for fields the caller opts out of.
 * -- Also demonstrates optional filters and ordering criteria.
 * SELECT
 *   album.id,
 *   CASE WHEN $include_name THEN album.name END AS name,
 *   CASE WHEN $include_released THEN album.released END AS released,
 *   CASE WHEN $include_format THEN album.format END AS format,
 *   CASE WHEN $include_recording THEN album.recording END AS recording,
 *   CASE WHEN $include_tracks THEN album.tracks END AS tracks,
 *   CASE WHEN $include_disc THEN album.disc END AS disc
 * FROM album
 * LEFT JOIN album_artist ON album_artist.album = album.id
 * LEFT JOIN artist ON artist.id = album_artist.artist
 * LEFT JOIN album_genre ON album_genre.album = album.id
 * LEFT JOIN genre ON genre.id = album_genre.genre
 * WHERE
 *   ($artist_name::text IS NULL OR artist.name = $artist_name)
 *   AND ($genre_name::text IS NULL OR genre.name = $genre_name)
 *   AND ($format::album_format IS NULL OR album.format = $format)
 *   AND ($released_after::timestamp IS NULL OR album.released >= $released_after)
 *   AND ($name_like::text IS NULL OR album.name LIKE $name_like)
 * ORDER BY
 *   CASE WHEN $order_by_name THEN album.name END ASC,
 *   CASE WHEN $order_by_released THEN album.released END DESC
 * }</pre>
 *
 * <h2>Source Path</h2> {@code ./queries/select_album_with_filters.sql}
 *
 * <p>
 * Generated from SQL queries using the
 * <a href="https://pgenie.io">pGenie</a> code generator.
 */
public record SelectAlbumWithFilters(
        /**
         * Maps to {@code $include_name} in the template.
         */
        boolean includeName,
        /**
         * Maps to {@code $include_released} in the template.
         */
        boolean includeReleased,
        /**
         * Maps to {@code $include_format} in the template.
         */
        boolean includeFormat,
        /**
         * Maps to {@code $include_recording} in the template.
         */
        boolean includeRecording,
        /**
         * Maps to {@code $include_tracks} in the template.
         */
        boolean includeTracks,
        /**
         * Maps to {@code $include_disc} in the template.
         */
        boolean includeDisc,
        /**
         * Maps to {@code $artist_name} in the template. Nullable.
         */
        String artistName,
        /**
         * Maps to {@code $genre_name} in the template. Nullable.
         */
        String genreName,
        /**
         * Maps to {@code $format} in the template. Nullable.
         */
        AlbumFormat format,
        /**
         * Maps to {@code $released_after} in the template. Nullable.
         */
        LocalDateTime releasedAfter,
        /**
         * Maps to {@code $name_like} in the template. Nullable.
         */
        String nameLike,
        /**
         * Maps to {@code $order_by_name} in the template.
         */
        boolean orderByName,
        /**
         * Maps to {@code $order_by_released} in the template.
         */
        boolean orderByReleased)
        implements Statement<SelectAlbumWithFilters.Result> {
    
    // -------------------------------------------------------------------------
    // Result type
    // -------------------------------------------------------------------------
    /**
     * Result of the statement parameterised by {@link SelectAlbumWithFilters}.
     */
    public static final class Result extends ArrayList<ResultRow> {
        Result() {}
    }

    /**
     * Row of {@link Result}.
     */
    public record ResultRow(
            /**
             * Maps to the {@code id} result-set column.
             */
            long id,
            /**
             * Maps to the {@code name} result-set column. Nullable.
             */
            String name,
            /**
             * Maps to the {@code released} result-set column. Nullable.
             */
            LocalDate released,
            /**
             * Maps to the {@code format} result-set column. Nullable.
             */
            AlbumFormat format,
            /**
             * Maps to the {@code recording} result-set column. Nullable.
             */
            RecordingInfo recording,
            /**
             * Maps to the {@code tracks} result-set column. Nullable.
             */
            List<TrackInfo> tracks,
            /**
             * Maps to the {@code disc} result-set column. Nullable.
             */
            DiscInfo disc) {}
    
    // -------------------------------------------------------------------------
    // Statement implementation
    // -------------------------------------------------------------------------
    @Override
    public String sql() {
        return """
               -- Demonstrates static query equivalent of dynamic field selection.
               -- Boolean flags control which fields are included in the result,
               -- returning NULL for fields the caller opts out of.
               -- Also demonstrates optional filters and ordering criteria.
               SELECT
                 album.id,
                 CASE WHEN ? THEN album.name END AS name,
                 CASE WHEN ? THEN album.released END AS released,
                 CASE WHEN ? THEN album.format END AS format,
                 CASE WHEN ? THEN album.recording END AS recording,
                 CASE WHEN ? THEN album.tracks END AS tracks,
                 CASE WHEN ? THEN album.disc END AS disc
               FROM album
               LEFT JOIN album_artist ON album_artist.album = album.id
               LEFT JOIN artist ON artist.id = album_artist.artist
               LEFT JOIN album_genre ON album_genre.album = album.id
               LEFT JOIN genre ON genre.id = album_genre.genre
               WHERE
                 (?::text IS NULL OR artist.name = ?)
                 AND (?::text IS NULL OR genre.name = ?)
                 AND (?::album_format::album_format IS NULL OR album.format = ?::album_format)
                 AND (?::timestamp IS NULL OR album.released >= ?)
                 AND (?::text IS NULL OR album.name LIKE ?)
               ORDER BY
                 CASE WHEN ? THEN album.name END ASC,
                 CASE WHEN ? THEN album.released END DESC
               """;
    }

    @Override
    public void bindParams(PreparedStatement ps) throws SQLException {
        Codec.BOOL.bind(ps, 1, this.includeName());
        Codec.BOOL.bind(ps, 2, this.includeReleased());
        Codec.BOOL.bind(ps, 3, this.includeFormat());
        Codec.BOOL.bind(ps, 4, this.includeRecording());
        Codec.BOOL.bind(ps, 5, this.includeTracks());
        Codec.BOOL.bind(ps, 6, this.includeDisc());
        Codec.TEXT.bind(ps, 7, this.artistName());
        Codec.TEXT.bind(ps, 8, this.artistName());
        Codec.TEXT.bind(ps, 9, this.genreName());
        Codec.TEXT.bind(ps, 10, this.genreName());
        AlbumFormat.CODEC.bind(ps, 11, this.format());
        AlbumFormat.CODEC.bind(ps, 12, this.format());
        Codec.TIMESTAMP.bind(ps, 13, this.releasedAfter());
        Codec.TIMESTAMP.bind(ps, 14, this.releasedAfter());
        Codec.TEXT.bind(ps, 15, this.nameLike());
        Codec.TEXT.bind(ps, 16, this.nameLike());
        Codec.BOOL.bind(ps, 17, this.orderByName());
        Codec.BOOL.bind(ps, 18, this.orderByReleased());
    }

    @Override
    public boolean returnsRows() {
        return true;
    }

    @Override
    public Result decodeResultSet(ResultSet rs) throws SQLException {
        Result output = new Result();
        int row = 0;
        
        while (rs.next()) {
            long idCol = Codec.INT8.decodeNonNullable(rs, row, 1);
            String nameCol = Codec.TEXT.decodeNullable(rs, row, 2);
            LocalDate releasedCol = Codec.DATE.decodeNullable(rs, row, 3);
            AlbumFormat formatCol = AlbumFormat.CODEC.decodeNullable(rs, row, 4);
            RecordingInfo recordingCol = RecordingInfo.CODEC.decodeNullable(rs, row, 5);
            List<TrackInfo> tracksCol = TrackInfo.CODEC.inDim().decodeNullable(rs, row, 6);
            DiscInfo discCol = DiscInfo.CODEC.decodeNullable(rs, row, 7);

            output.add(new ResultRow(idCol, nameCol, releasedCol, formatCol, recordingCol, tracksCol, discCol));
            row++;
        }

        return output;
    }

    @Override
    public SelectAlbumWithFilters.Result decodeAffectedRows(long affectedRows) {
        throw new UnsupportedOperationException();
    }
}
