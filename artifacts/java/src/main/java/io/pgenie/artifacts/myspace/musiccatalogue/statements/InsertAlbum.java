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

import java.util.List;

/**
 * Type-safe binding for the {@code insert_album} query.
 *
 * <h2>SQL Template</h2>
 *
 * <pre>{@code
 * insert into album (name, released, format, recording)
 * values ($name, $released, $format, $recording)
 * returning id
 * }</pre>
 *
 * <h2>Source Path</h2> {@code ./queries/insert_album.sql}
 *
 * <p>
 * Generated from SQL queries using the
 * <a href="https://pgenie.io">pGenie</a> code generator.
 */
public record InsertAlbum(
        /**
         * Maps to {@code $name} in the template.
         */
        String name,
        /**
         * Maps to {@code $released} in the template.
         */
        LocalDate released,
        /**
         * Maps to {@code $format} in the template.
         */
        AlbumFormat format,
        /**
         * Maps to {@code $recording} in the template.
         */
        RecordingInfo recording)
        implements Statement<InsertAlbum.Output> {

    // -------------------------------------------------------------------------
    // Result type
    // -------------------------------------------------------------------------
    /**
     * Result of the statement parameterised by {@link InsertAlbum}.
     */
    public record Output(
            /**
             * Maps to the {@code id} result-set column.
             */
            long id) {

    }

    // -------------------------------------------------------------------------
    // Statement implementation
    // -------------------------------------------------------------------------
    @Override
    public String sql() {
        return """
               insert into album (name, released, format, recording)
               values (?, ?, ?::public.album_format, ?::public.recording_info)
               returning id
               """;
    }

    @Override
    public void bindParams(PreparedStatement ps) throws SQLException {
        ps.setString(1, this.name());
        ps.setDate(2, Date.valueOf(this.released()));
        Jdbc.bind(ps, 3, AlbumFormat.CODEC, this.format());
        Jdbc.bind(ps, 4, RecordingInfo.CODEC, this.recording());
    }

    @Override
    public boolean returnsRows() {
        return true;
    }

    @Override
    public Output decodeResultSet(ResultSet rs) throws SQLException {
        rs.next();
        long id = rs.getLong(1);
        return new Output(id);
    }

    @Override
    public InsertAlbum.Output decodeAffectedRows(long affectedRows) {
        throw new UnsupportedOperationException();
    }
}
