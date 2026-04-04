package io.pgenie.artifacts.myspace.musiccatalogue.statements;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import io.pgenie.artifacts.myspace.musiccatalogue.JdbcCodec;
import io.pgenie.artifacts.myspace.musiccatalogue.Statement;
import io.pgenie.artifacts.myspace.musiccatalogue.types.*;

/**
 * Type-safe binding for the {@code select_album_by_name} query.
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
 * where name = $name
 * }</pre>
 *
 * <h2>Source Path</h2> {@code ./queries/select_album_by_name.sql}
 *
 * <p>
 * Generated from SQL queries using the
 * <a href="https://pgenie.io">pGenie</a> code generator.
 */
public record SelectAlbumByName(
        /**
         * Maps to {@code $name} in the template.
         */
        String name)
        implements Statement<SelectAlbumByName.Output> {

    // -------------------------------------------------------------------------
    // Result type
    // -------------------------------------------------------------------------
    /**
     * Result of the statement parameterised by {@link SelectAlbumByName}.
     */
    public static final class Output extends ArrayList<OutputRow> {
        Output() {}
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
            Optional<RecordingInfo> recording) {}

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
               where name = ?
               """;
    }

    @Override
    public void bindParams(PreparedStatement ps) throws SQLException {
        ps.setString(1, this.name());
    }

    @Override
    public boolean returnsRows() {
        return true;
    }

    @Override
    public Output decodeResultSet(ResultSet rs) throws SQLException {
        Output output = new Output();
        int row = 0;
        
        while (rs.next()) {
            long idCol = rs.getLong(1);
            String nameCol = rs.getString(2);
            Optional<LocalDate> releasedCol;
            {
                Date releasedColBase = rs.getDate(3);
                if (releasedColBase != null) {
                    releasedCol = Optional.of(releasedColBase.toLocalDate());
                } else {
                    releasedCol = Optional.empty();
                }
            }
            Optional<AlbumFormat> formatCol = Optional.ofNullable(new JdbcCodec<>(AlbumFormat.CODEC).decodeNullable(rs, row, 4));
            Optional<RecordingInfo> recordingCol = Optional.ofNullable(new JdbcCodec<>(RecordingInfo.CODEC).decodeNullable(rs, row, 5));

            output.add(new OutputRow(idCol, nameCol, releasedCol, formatCol, recordingCol));
            row++;
        }

        return output;
    }

    @Override
    public SelectAlbumByName.Output decodeAffectedRows(long affectedRows) {
        throw new UnsupportedOperationException();
    }
}
