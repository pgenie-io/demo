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
 * Type-safe binding for the {@code update_album_recording_returning} query.
 *
 * <h2>SQL Template</h2>
 *
 * <pre>{@code
 * -- Update album recording information
 * update album
 * set recording = $recording
 * where id = $id
 * returning *
 * }</pre>
 *
 * <h2>Source Path</h2> {@code ./queries/update_album_recording_returning.sql}
 *
 * <p>
 * Generated from SQL queries using the
 * <a href="https://pgenie.io">pGenie</a> code generator.
 */
public record UpdateAlbumRecordingReturning(
        /**
         * Maps to {@code $recording} in the template. Nullable.
         */
        RecordingInfo recording,
        /**
         * Maps to {@code $id} in the template.
         */
        long id)
        implements Statement<UpdateAlbumRecordingReturning.Result> {
    
    // -------------------------------------------------------------------------
    // Result type
    // -------------------------------------------------------------------------
    /**
     * Result of the statement parameterised by {@link UpdateAlbumRecordingReturning}.
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
               -- Update album recording information
               update album
               set recording = ?::recording_info
               where id = ?
               returning *
               """;
    }

    @Override
    public void bindParams(PreparedStatement ps) throws SQLException {
        RecordingInfo.CODEC.bind(ps, 1, this.recording());
        Codec.INT8.bind(ps, 2, this.id());
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
            RecordingInfo recordingCol = RecordingInfo.CODEC.decodeNullable(rs, row, 5);
            List<TrackInfo> tracksCol = TrackInfo.CODEC.inDim().decodeNullable(rs, row, 6);
            DiscInfo discCol = DiscInfo.CODEC.decodeNullable(rs, row, 7);

            output.add(new ResultRow(idCol, nameCol, releasedCol, formatCol, recordingCol, tracksCol, discCol));
            row++;
        }

        return output;
    }

    @Override
    public UpdateAlbumRecordingReturning.Result decodeAffectedRows(long affectedRows) {
        throw new UnsupportedOperationException();
    }
}
