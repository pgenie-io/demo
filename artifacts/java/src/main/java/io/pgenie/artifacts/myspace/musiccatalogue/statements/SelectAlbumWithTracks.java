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
        implements Statement<SelectAlbumWithTracks.Output> {

    // -------------------------------------------------------------------------
    // Result type
    // -------------------------------------------------------------------------
    /**
     * Result of the statement parameterised by {@link SelectAlbumWithTracks}.
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
        ps.setLong(1, this.id());
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
            List<TrackInfo> tracksCol = new JdbcCodec<>(TrackInfo.CODEC.inDim()).decodeNonNullable(rs, row, 3);
            Optional<DiscInfo> discCol = Optional.ofNullable(new JdbcCodec<>(DiscInfo.CODEC).decodeNullable(rs, row, 4));

            output.add(new OutputRow(idCol, nameCol, tracksCol, discCol));
            row++;
        }

        return output;
    }

    @Override
    public SelectAlbumWithTracks.Output decodeAffectedRows(long affectedRows) {
        throw new UnsupportedOperationException();
    }
}
