package io.pgenie.artifacts.myspace.musiccatalogue.statements;

import java.sql.Date;
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
        implements Statement<SelectAlbumByFormat.Result> {
    
    // -------------------------------------------------------------------------
    // Result type
    // -------------------------------------------------------------------------
    /**
     * Result of the statement parameterised by {@link SelectAlbumByFormat}.
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
               where format = ?::album_format
               """;
    }

    @Override
    public void bindParams(PreparedStatement ps) throws SQLException {
        AlbumFormat.CODEC.bind(ps, 1, this.format());
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
            Optional<LocalDate> releasedCol = Codec.DATE.decodeOptional(rs, row, 3);
            Optional<AlbumFormat> formatCol = AlbumFormat.CODEC.decodeOptional(rs, row, 4);
            Optional<RecordingInfo> recordingCol = RecordingInfo.CODEC.decodeOptional(rs, row, 5);

            output.add(new ResultRow(idCol, nameCol, releasedCol, formatCol, recordingCol));
            row++;
        }

        return output;
    }

    @Override
    public SelectAlbumByFormat.Result decodeAffectedRows(long affectedRows) {
        throw new UnsupportedOperationException();
    }
}
