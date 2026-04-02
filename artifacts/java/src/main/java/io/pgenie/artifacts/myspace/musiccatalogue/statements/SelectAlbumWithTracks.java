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
             * Maps to the {@code tracks} result-set column.
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
        while (rs.next()) {
            try {
                long id = rs.getLong(1);
                String name = rs.getString(2);
                List<TrackInfo> tracks = TrackInfo.CODEC.inDim().decodeInTextFromString(rs.getString(3));
                String discStr = rs.getString(4);
                DiscInfo disc = discStr != null ? DiscInfo.CODEC.decodeInTextFromString(discStr) : null;
                output.add(new OutputRow(id, name, tracks, disc));
            } catch (io.codemine.java.postgresql.codecs.Codec.DecodingException e) {
                throw new IllegalStateException(e);
            }
        }
        return output;
    }

    @Override
    public SelectAlbumWithTracks.Output decodeAffectedRows(long affectedRows) {
        throw new UnsupportedOperationException();
    }
}
