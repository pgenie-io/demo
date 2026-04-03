package io.pgenie.artifacts.myspace.musiccatalogue.statements;

import io.pgenie.artifacts.myspace.musiccatalogue.Statement;
import io.pgenie.artifacts.myspace.musiccatalogue.codecs.Jdbc;
import io.pgenie.artifacts.myspace.musiccatalogue.types.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.sql.Types;
import java.time.*;
import io.codemine.java.postgresql.codecs.Codec;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        Optional<String> artistName,
        /**
         * Maps to {@code $genre_name} in the template. Nullable.
         */
        Optional<String> genreName,
        /**
         * Maps to {@code $format} in the template. Nullable.
         */
        Optional<AlbumFormat> format,
        /**
         * Maps to {@code $released_after} in the template. Nullable.
         */
        Optional<LocalDateTime> releasedAfter,
        /**
         * Maps to {@code $name_like} in the template. Nullable.
         */
        Optional<String> nameLike,
        /**
         * Maps to {@code $order_by_name} in the template.
         */
        boolean orderByName,
        /**
         * Maps to {@code $order_by_released} in the template.
         */
        boolean orderByReleased)
        implements Statement<SelectAlbumWithFilters.Output> {

    // -------------------------------------------------------------------------
    // Result type
    // -------------------------------------------------------------------------
    /**
     * Result of the statement parameterised by {@link SelectAlbumWithFilters}.
     */
    public static final class Output extends ArrayList<OutputRow> {

        Output() {
        }
    }

    /**
     * Row of {@link Output}.
     */
    public record OutputRow(
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
            Optional<LocalDate> released,
            /**
             * Maps to the {@code format} result-set column. Nullable.
             */
            Optional<AlbumFormat> format) {

    }

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
                 AND (?::public.album_format::album_format IS NULL OR album.format = ?::public.album_format)
                 AND (?::timestamp IS NULL OR album.released >= ?)
                 AND (?::text IS NULL OR album.name LIKE ?)
               ORDER BY
                 CASE WHEN ? THEN album.name END ASC,
                 CASE WHEN ? THEN album.released END DESC
               """;
    }

    @Override
    public void bindParams(PreparedStatement ps) throws SQLException {
        if (this.artistName().isPresent()) {
            ps.setString(1, this.artistName().get());
        } else {
            ps.setNull(1, Types.VARCHAR);
        }
        if (this.artistName().isPresent()) {
            ps.setString(2, this.artistName().get());
        } else {
            ps.setNull(2, Types.VARCHAR);
        }
        if (this.genreName().isPresent()) {
            ps.setString(3, this.genreName().get());
        } else {
            ps.setNull(3, Types.VARCHAR);
        }
        if (this.genreName().isPresent()) {
            ps.setString(4, this.genreName().get());
        } else {
            ps.setNull(4, Types.VARCHAR);
        }
        Jdbc.bind(ps, 5, AlbumFormat.CODEC, this.format().orElse(null));
        Jdbc.bind(ps, 6, AlbumFormat.CODEC, this.format().orElse(null));
        Jdbc.bind(ps, 7, Codec.TIMESTAMP, this.releasedAfter().orElse(null));
        Jdbc.bind(ps, 8, Codec.TIMESTAMP, this.releasedAfter().orElse(null));
        if (this.nameLike().isPresent()) {
            ps.setString(9, this.nameLike().get());
        } else {
            ps.setNull(9, Types.VARCHAR);
        }
        if (this.nameLike().isPresent()) {
            ps.setString(10, this.nameLike().get());
        } else {
            ps.setNull(10, Types.VARCHAR);
        }
        ps.setBoolean(11, this.orderByName());
        ps.setBoolean(12, this.orderByReleased());
    }

    @Override
    public boolean returnsRows() {
        return true;
    }

    @Override
    public Output decodeResultSet(ResultSet rs) throws SQLException {
        Output output = new Output();
        while (rs.next()) {
            try {
                long id = rs.getLong(1);
                String name = rs.getString(2);
                Date releasedSql = rs.getDate(3);
                Optional<LocalDate> released = Optional.ofNullable(releasedSql != null ? releasedSql.toLocalDate() : null);
                String formatStr = rs.getString(4);
                Optional<AlbumFormat> format = Optional.ofNullable(formatStr != null ? AlbumFormat.CODEC.decodeInTextFromString(formatStr) : null);
                output.add(new OutputRow(id, name, released, format));
            } catch (io.codemine.java.postgresql.codecs.Codec.DecodingException e) {
                throw new IllegalStateException(e);
            }
        }
        return output;
    }

    @Override
    public SelectAlbumWithFilters.Output decodeAffectedRows(long affectedRows) {
        throw new UnsupportedOperationException();
    }
}
