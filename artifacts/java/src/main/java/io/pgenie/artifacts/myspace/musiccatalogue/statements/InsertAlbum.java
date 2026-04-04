package io.pgenie.artifacts.myspace.musiccatalogue.statements;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.sql.Types;
import java.time.*;
import java.util.List;
import io.codemine.java.postgresql.codecs.Codec;
import io.pgenie.artifacts.myspace.musiccatalogue.JdbcCodec;
import io.pgenie.artifacts.myspace.musiccatalogue.Statement;
import io.pgenie.artifacts.myspace.musiccatalogue.types.*;

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
            long id) {}

    // -------------------------------------------------------------------------
    // Statement implementation
    // -------------------------------------------------------------------------
    @Override
    public String sql() {
        return """
               insert into album (name, released, format, recording)
               values (?, ?, ?::album_format, ?::recording_info)
               returning id
               """;
    }

    @Override
    public void bindParams(PreparedStatement ps) throws SQLException {
        ps.setString(1, this.name());
        ps.setDate(2, Date.valueOf(this.released()));
        new JdbcCodec<>(AlbumFormat.CODEC).bind(ps, 3, this.format());
        new JdbcCodec<>(RecordingInfo.CODEC).bind(ps, 4, this.recording());
    }

    @Override
    public boolean returnsRows() {
        return true;
    }

    @Override
    public Output decodeResultSet(ResultSet rs) throws SQLException {
        rs.next();

        long idCol = rs.getLong(1);

        return new Output(idCol);
    }

    @Override
    public InsertAlbum.Output decodeAffectedRows(long affectedRows) {
        throw new UnsupportedOperationException();
    }
}
