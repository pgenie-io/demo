package io.pgenie.artifacts.myspace.musiccatalogue.statements;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import io.codemine.java.postgresql.codecs.Codec;
import io.pgenie.artifacts.myspace.musiccatalogue.JdbcCodec;
import io.pgenie.artifacts.myspace.musiccatalogue.Statement;
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
        Optional<RecordingInfo> recording,
        /**
         * Maps to {@code $id} in the template.
         */
        long id)
        implements Statement<UpdateAlbumRecordingReturning.Output> {

    // -------------------------------------------------------------------------
    // Result type
    // -------------------------------------------------------------------------
    /**
     * Result of the statement parameterised by {@link UpdateAlbumRecordingReturning}.
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
            Optional<RecordingInfo> recording,
            /**
             * Maps to the {@code tracks} result-set column. Nullable.
             */
            Optional<List<TrackInfo>> tracks,
            /**
             * Maps to the {@code disc} result-set column. Nullable.
             */
            Optional<DiscInfo> disc) {}

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
        new JdbcCodec<>(RecordingInfo.CODEC).bind(ps, 1, this.recording().orElse(null));
        ps.setLong(2, this.id());
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
            Optional<List<TrackInfo>> tracksCol = Optional.ofNullable(new JdbcCodec<>(TrackInfo.CODEC.inDim()).decodeNullable(rs, row, 6));
            Optional<DiscInfo> discCol = Optional.ofNullable(new JdbcCodec<>(DiscInfo.CODEC).decodeNullable(rs, row, 7));

            output.add(new OutputRow(idCol, nameCol, releasedCol, formatCol, recordingCol, tracksCol, discCol));
            row++;
        }

        return output;
    }

    @Override
    public UpdateAlbumRecordingReturning.Output decodeAffectedRows(long affectedRows) {
        throw new UnsupportedOperationException();
    }
}
