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
 * SELECT
 *   album.id,
 *   album.name,
 *   album.released,
 *   album.format
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
             * Maps to the {@code name} result-set column.
             */
            String name,
            /**
             * Maps to the {@code released} result-set column. Nullable.
             */
            LocalDate released,
            /**
             * Maps to the {@code format} result-set column. Nullable.
             */
            AlbumFormat format) {}
    
    // -------------------------------------------------------------------------
    // Statement implementation
    // -------------------------------------------------------------------------
    @Override
    public String sql() {
        return """
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
        Codec.TEXT.bind(ps, 1, this.artistName());
        Codec.TEXT.bind(ps, 2, this.artistName());
        Codec.TEXT.bind(ps, 3, this.genreName());
        Codec.TEXT.bind(ps, 4, this.genreName());
        AlbumFormat.CODEC.bind(ps, 5, this.format());
        AlbumFormat.CODEC.bind(ps, 6, this.format());
        Codec.TIMESTAMP.bind(ps, 7, this.releasedAfter());
        Codec.TIMESTAMP.bind(ps, 8, this.releasedAfter());
        Codec.TEXT.bind(ps, 9, this.nameLike());
        Codec.TEXT.bind(ps, 10, this.nameLike());
        Codec.BOOL.bind(ps, 11, this.orderByName());
        Codec.BOOL.bind(ps, 12, this.orderByReleased());
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
            String nameCol = Codec.TEXT.decodeNonNullable(rs, row, 2);
            LocalDate releasedCol = Codec.DATE.decodeNullable(rs, row, 3);
            AlbumFormat formatCol = AlbumFormat.CODEC.decodeNullable(rs, row, 4);

            output.add(new ResultRow(idCol, nameCol, releasedCol, formatCol));
            row++;
        }

        return output;
    }

    @Override
    public SelectAlbumWithFilters.Result decodeAffectedRows(long affectedRows) {
        throw new UnsupportedOperationException();
    }
}
