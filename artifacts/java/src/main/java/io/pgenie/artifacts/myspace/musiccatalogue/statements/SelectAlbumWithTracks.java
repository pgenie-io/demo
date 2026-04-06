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
 * Type-safe binding for the {@code select_album_with_tracks} query.
 *
 * <h2>SQL Template</h2>
 *
 * <pre>{@code
 * select id, name, tracks, disc
 * from album
 * where id = $id
 * }</pre>
 *
 * <h2>Source Path</h2> {@code ./queries/select_album_with_tracks.sql}
 *
 * <p>
 * Generated from SQL queries using the
 * <a href="https://pgenie.io">pGenie</a> code generator.
 */
public record SelectAlbumWithTracks(
        /**
         * Maps to {@code $id} in the template.
         */
        long id)
        implements Statement<SelectAlbumWithTracks.Result> {
    
    // -------------------------------------------------------------------------
    // Result type
    // -------------------------------------------------------------------------
    /**
     * Result of the statement parameterised by {@link SelectAlbumWithTracks}.
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
             * Maps to the {@code tracks} result-set column.
             */
            List<TrackInfo> tracks,
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
               select id, name, tracks, disc
               from album
               where id = ?
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
        Result output = new Result();
        int row = 0;
        
        while (rs.next()) {
            long idCol = Codec.INT8.decodeNonNullable(rs, row, 1);
            String nameCol = Codec.TEXT.decodeNonNullable(rs, row, 2);
            List<TrackInfo> tracksCol = TrackInfo.CODEC.inDim().decodeNonNullable(rs, row, 3);
            Optional<DiscInfo> discCol = DiscInfo.CODEC.decodeOptional(rs, row, 4);

            output.add(new ResultRow(idCol, nameCol, tracksCol, discCol));
            row++;
        }

        return output;
    }

    @Override
    public SelectAlbumWithTracks.Result decodeAffectedRows(long affectedRows) {
        throw new UnsupportedOperationException();
    }
}
