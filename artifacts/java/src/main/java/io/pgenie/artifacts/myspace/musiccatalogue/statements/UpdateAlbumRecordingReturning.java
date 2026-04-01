package io.pgenie.artifacts.myspace.musiccatalogue.statements;

import io.pgenie.artifacts.myspace.musiccatalogue.Statement;
import io.pgenie.artifacts.myspace.musiccatalogue.codecs.Jdbc;
import io.pgenie.artifacts.myspace.musiccatalogue.types.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.time.*;

import java.util.ArrayList;
import java.util.List;

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
        implements Statement<UpdateAlbumRecordingReturning.Output> {

    // -------------------------------------------------------------------------
    // Result type
    // -------------------------------------------------------------------------
    /**
     * Result of the statement parameterised by {@link UpdateAlbumRecordingReturning}.
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
            DiscInfo disc) {

    }

    // -------------------------------------------------------------------------
    // Statement implementation
    // -------------------------------------------------------------------------
    @Override
    public String sql() {
        return """
               -- Update album recording information
               update album
               set recording = ?::public.recording_info
               where id = ?
               returning *
               """;
    }

    @Override
    public void bindParams(PreparedStatement ps) throws SQLException {
        Jdbc.bind(ps, 1, RecordingInfo.CODEC, this.recording());
        ps.setLong(2, this.id());
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
                LocalDate released = releasedSql != null ? releasedSql.toLocalDate() : null;
                String formatStr = rs.getString(4);
                AlbumFormat format = formatStr != null ? AlbumFormat.CODEC.decodeInTextFromString(formatStr) : null;
                String recordingStr = rs.getString(5);
                RecordingInfo recording = recordingStr != null ? RecordingInfo.CODEC.decodeInTextFromString(recordingStr) : null;
                String tracksStr = rs.getString(6);
                List<TrackInfo> tracks = tracksStr != null ? TrackInfo.CODEC.inDim().decodeInTextFromString(tracksStr) : null;
                String discStr = rs.getString(7);
                DiscInfo disc = discStr != null ? DiscInfo.CODEC.decodeInTextFromString(discStr) : null;
                output.add(new OutputRow(id, name, released, format, recording, tracks, disc));
            } catch (io.codemine.java.postgresql.codecs.Codec.DecodingException e) {
                throw new IllegalStateException(e);
            }
        }
        return output;
    }

    @Override
    public UpdateAlbumRecordingReturning.Output decodeAffectedRows(long affectedRows) {
        throw new UnsupportedOperationException();
    }
}
