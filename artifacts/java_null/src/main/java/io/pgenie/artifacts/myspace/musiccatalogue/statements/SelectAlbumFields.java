package io.pgenie.artifacts.myspace.musiccatalogue.statements;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.*;
import java.util.ArrayList;
import java.util.List;
import io.codemine.java.postgresql.jdbc.Codec;
import io.codemine.java.postgresql.jdbc.Statement;
import io.pgenie.artifacts.myspace.musiccatalogue.types.*;

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
        implements Statement<SelectAlbumFields.Result> {
    
    // -------------------------------------------------------------------------
    // Result type
    // -------------------------------------------------------------------------
    /**
     * Result of the statement parameterised by {@link SelectAlbumFields}.
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
             * Maps to the {@code name} result-set column. Nullable.
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
        Codec.BOOL.bind(ps, 1, this.includeName());
        Codec.BOOL.bind(ps, 2, this.includeReleased());
        Codec.BOOL.bind(ps, 3, this.includeFormat());
        Codec.BOOL.bind(ps, 4, this.includeRecording());
        Codec.BOOL.bind(ps, 5, this.includeTracks());
        Codec.BOOL.bind(ps, 6, this.includeDisc());
        Codec.INT8.bind(ps, 7, this.id());
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
            String nameCol = Codec.TEXT.decodeNullable(rs, row, 2);
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
    public SelectAlbumFields.Result decodeAffectedRows(long affectedRows) {
        throw new UnsupportedOperationException();
    }
}
