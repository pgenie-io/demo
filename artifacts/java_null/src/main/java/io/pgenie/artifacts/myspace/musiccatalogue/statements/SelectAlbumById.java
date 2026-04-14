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
        Long id)
        implements Statement<SelectAlbumById.Result> {
    
    // -------------------------------------------------------------------------
    // Result type
    // -------------------------------------------------------------------------
    /**
     * Result of the statement parameterised by {@link SelectAlbumById}.
     */
    public record Result(
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
               -- Example of a query selecting 0 or 1 row.
               select *
               from album
               where id = ?
               limit 1
               """;
    }

    @Override
    public void bindParams(PreparedStatement ps) throws SQLException {
        Codec.INT8.bind(ps, 1, this.id());
    }

    @Override
    public boolean returnsRows() {
        return true;
    }

    @Override
    public Result decodeResultSet(ResultSet rs) throws SQLException {
        if (!rs.next()) {
            return null;
        }
        long idCol = Codec.INT8.decodeNonNullable(rs, 0, 1);
        String nameCol = Codec.TEXT.decodeNonNullable(rs, 0, 2);
        LocalDate releasedCol = Codec.DATE.decodeNullable(rs, 0, 3);
        AlbumFormat formatCol = AlbumFormat.CODEC.decodeNullable(rs, 0, 4);
        RecordingInfo recordingCol = RecordingInfo.CODEC.decodeNullable(rs, 0, 5);
        List<TrackInfo> tracksCol = TrackInfo.CODEC.inDim().decodeNullable(rs, 0, 6);
        DiscInfo discCol = DiscInfo.CODEC.decodeNullable(rs, 0, 7);

        return new Result(idCol, nameCol, releasedCol, formatCol, recordingCol, tracksCol, discCol);
    }

    @Override
    public SelectAlbumById.Result decodeAffectedRows(long affectedRows) {
        throw new UnsupportedOperationException();
    }
}
