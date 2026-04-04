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
import java.util.Optional;

/**
 * Type-safe binding for the {@code select_album_fields} query.
 *
 * <h2>SQL Template</h2>
 *
 * <pre>{@code
 * -- Demonstrates static query equivalent of dynamic field selection.
 * -- Boolean flags control which fields are included in the result,
 * -- returning NULL for fields the caller opts out of.
 * SELECT
 *   album.id,
 *   CASE WHEN $include_name      THEN album.name      END AS name,
 *   CASE WHEN $include_released  THEN album.released  END AS released,
 *   CASE WHEN $include_format    THEN album.format    END AS format,
 *   CASE WHEN $include_recording THEN album.recording END AS recording,
 *   CASE WHEN $include_tracks    THEN album.tracks    END AS tracks,
 *   CASE WHEN $include_disc      THEN album.disc      END AS disc
 * FROM album
 * WHERE album.id = $id
 * }</pre>
 *
 * <h2>Source Path</h2> {@code ./queries/select_album_fields.sql}
 *
 * <p>
 * Generated from SQL queries using the
 * <a href="https://pgenie.io">pGenie</a> code generator.
 */
public record SelectAlbumFields(
        /**
         * Maps to {@code $include_name} in the template.
         */
        boolean includeName,
        /**
         * Maps to {@code $include_released} in the template.
         */
        boolean includeReleased,
        /**
         * Maps to {@code $include_format} in the template.
         */
        boolean includeFormat,
        /**
         * Maps to {@code $include_recording} in the template.
         */
        boolean includeRecording,
        /**
         * Maps to {@code $include_tracks} in the template.
         */
        boolean includeTracks,
        /**
         * Maps to {@code $include_disc} in the template.
         */
        boolean includeDisc,
        /**
         * Maps to {@code $id} in the template.
         */
        long id)
        implements Statement<SelectAlbumFields.Output> {

    // -------------------------------------------------------------------------
    // Result type
    // -------------------------------------------------------------------------
    /**
     * Result of the statement parameterised by {@link SelectAlbumFields}.
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
             * Maps to the {@code name} result-set column. Nullable.
             */
            Optional<String> name,
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
            Optional<DiscInfo> disc) {

    }

    // -------------------------------------------------------------------------
    // Statement implementation
    // -------------------------------------------------------------------------
    @Override
    public String sql() {
        return """
               -- Demonstrates static query equivalent of dynamic field selection.
               -- Boolean flags control which fields are included in the result,
               -- returning NULL for fields the caller opts out of.
               SELECT
                 album.id,
                 CASE WHEN ?      THEN album.name      END AS name,
                 CASE WHEN ?  THEN album.released  END AS released,
                 CASE WHEN ?    THEN album.format    END AS format,
                 CASE WHEN ? THEN album.recording END AS recording,
                 CASE WHEN ?    THEN album.tracks    END AS tracks,
                 CASE WHEN ?      THEN album.disc      END AS disc
               FROM album
               WHERE album.id = ?
               """;
    }

    @Override
    public void bindParams(PreparedStatement ps) throws SQLException {
        ps.setBoolean(1, this.includeName());
        ps.setBoolean(2, this.includeReleased());
        ps.setBoolean(3, this.includeFormat());
        ps.setBoolean(4, this.includeRecording());
        ps.setBoolean(5, this.includeTracks());
        ps.setBoolean(6, this.includeDisc());
        ps.setLong(7, this.id());
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
                Optional<String> name = Optional.ofNullable(rs.getString(2));
                Date releasedSql = rs.getDate(3);
                Optional<LocalDate> released = Optional.ofNullable(releasedSql != null ? releasedSql.toLocalDate() : null);
                String formatStr = rs.getString(4);
                Optional<AlbumFormat> format = Optional.ofNullable(formatStr != null ? AlbumFormat.CODEC.decodeInTextFromString(formatStr) : null);
                String recordingStr = rs.getString(5);
                Optional<RecordingInfo> recording = Optional.ofNullable(recordingStr != null ? RecordingInfo.CODEC.decodeInTextFromString(recordingStr) : null);
                String tracksStr = rs.getString(6);
                Optional<List<TrackInfo>> tracks = Optional.ofNullable(tracksStr != null ? TrackInfo.CODEC.inDim().decodeInTextFromString(tracksStr) : null);
                String discStr = rs.getString(7);
                Optional<DiscInfo> disc = Optional.ofNullable(discStr != null ? DiscInfo.CODEC.decodeInTextFromString(discStr) : null);
                output.add(new OutputRow(id, name, released, format, recording, tracks, disc));
            } catch (io.codemine.java.postgresql.codecs.Codec.DecodingException e) {
                throw new IllegalStateException(e);
            }
        }
        return output;
    }

    @Override
    public SelectAlbumFields.Output decodeAffectedRows(long affectedRows) {
        throw new UnsupportedOperationException();
    }
}
