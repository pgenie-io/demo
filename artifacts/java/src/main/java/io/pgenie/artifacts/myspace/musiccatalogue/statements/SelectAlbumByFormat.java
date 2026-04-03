package io.pgenie.artifacts.myspace.musiccatalogue.statements;

import io.pgenie.artifacts.myspace.musiccatalogue.Statement;
import io.pgenie.artifacts.myspace.musiccatalogue.codecs.Jdbc;
import io.pgenie.artifacts.myspace.musiccatalogue.types.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.time.*;
import io.codemine.java.postgresql.codecs.Codec;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Type-safe binding for the {@code select_album_by_format} query.
 *
 * <h2>SQL Template</h2>
 *
 * <pre>{@code
 * select 
 *   id,
 *   name,
 *   released,
 *   format,
 *   recording
 * from album
 * where format = $format
 * }</pre>
 *
 * <h2>Source Path</h2> {@code ./queries/select_album_by_format.sql}
 *
 * <p>
 * Generated from SQL queries using the
 * <a href="https://pgenie.io">pGenie</a> code generator.
 */
public record SelectAlbumByFormat(
        /**
         * Maps to {@code $format} in the template.
         */
        AlbumFormat format)
        implements Statement<SelectAlbumByFormat.Output> {

    // -------------------------------------------------------------------------
    // Result type
    // -------------------------------------------------------------------------
    /**
     * Result of the statement parameterised by {@link SelectAlbumByFormat}.
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
            Optional<AlbumFormat> format,
            /**
             * Maps to the {@code recording} result-set column. Nullable.
             */
            Optional<RecordingInfo> recording) {

    }

    // -------------------------------------------------------------------------
    // Statement implementation
    // -------------------------------------------------------------------------
    @Override
    public String sql() {
        return """
               select 
                 id,
                 name,
                 released,
                 format,
                 recording
               from album
               where format = ?::public.album_format
               """;
    }

    @Override
    public void bindParams(PreparedStatement ps) throws SQLException {
        Jdbc.bind(ps, 1, AlbumFormat.CODEC, this.format());
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
                String recordingStr = rs.getString(5);
                Optional<RecordingInfo> recording = Optional.ofNullable(recordingStr != null ? RecordingInfo.CODEC.decodeInTextFromString(recordingStr) : null);
                output.add(new OutputRow(id, name, released, format, recording));
            } catch (io.codemine.java.postgresql.codecs.Codec.DecodingException e) {
                throw new IllegalStateException(e);
            }
        }
        return output;
    }

    @Override
    public SelectAlbumByFormat.Output decodeAffectedRows(long affectedRows) {
        throw new UnsupportedOperationException();
    }
}
