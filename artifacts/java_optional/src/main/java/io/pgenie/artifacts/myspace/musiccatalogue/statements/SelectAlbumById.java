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
 * Type-safe binding for the {@code select_album_by_id} query.
 *
 * <h2>SQL Template</h2>
 *
 * <pre>{@code
 * -- Example of a query selecting 0 or 1 row.
 * select *
 * from album
 * where id = $id
 * limit 1
 * }</pre>
 *
 * <h2>Source Path</h2> {@code ./queries/select_album_by_id.sql}
 *
 * <p>
 * Generated from SQL queries using the
 * <a href="https://pgenie.io">pGenie</a> code generator.
 */
public record SelectAlbumById(
        /**
         * Maps to {@code $id} in the template. Nullable.
         */
        Optional<Long> id)
        implements Statement<Optional<SelectAlbumById.ResultRow>> {
    
    // -------------------------------------------------------------------------
    // Result type
    // -------------------------------------------------------------------------
    /**
     * Result of the statement parameterised by {@link SelectAlbumById}.
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
            Optional<RecordingInfo> recording,
            /**
             * Maps to the {@code tracks} result-set column. Nullable.
             */
            Optional<List<Optional<TrackInfo>>> tracks,
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
               -- Example of a query selecting 0 or 1 row.
               select *
               from album
               where id = ?
               limit 1
               """;
    }

    @Override
    public void bindParams(PreparedStatement ps) throws SQLException {
        Codec.INT8.bind(ps, 1, this.id().orElse(null));
    }

    @Override
    public boolean returnsRows() {
        return true;
    }

    @Override
    public Optional<ResultRow> decodeResultSet(ResultSet rs) throws SQLException {
        if (!rs.next()) {
            return Optional.empty();
        }
        long idCol = Codec.INT8.decodeNonNullable(rs, 0, 1);
        String nameCol = Codec.TEXT.decodeNonNullable(rs, 0, 2);
        Optional<LocalDate> releasedCol = Codec.DATE.decodeOptional(rs, 0, 3);
        Optional<AlbumFormat> formatCol = AlbumFormat.CODEC.decodeOptional(rs, 0, 4);
        Optional<RecordingInfo> recordingCol = RecordingInfo.CODEC.decodeOptional(rs, 0, 5);
        Optional<List<Optional<TrackInfo>>> tracksCol = TrackInfo.CODEC.inDim().decodeOptional(rs, 0, 6).map(list1 -> list1.stream().map(Optional::ofNullable).toList());
        Optional<DiscInfo> discCol = DiscInfo.CODEC.decodeOptional(rs, 0, 7);

        return Optional.of(new ResultRow(idCol, nameCol, releasedCol, formatCol, recordingCol, tracksCol, discCol));
    }

    @Override
    public Optional<SelectAlbumById.ResultRow> decodeAffectedRows(long affectedRows) {
        throw new UnsupportedOperationException();
    }
}
