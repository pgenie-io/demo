package io.pgenie.artifacts.myspace.musiccatalogue.statements;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.time.*;
import java.util.ArrayList;
import java.util.List;
import io.pgenie.artifacts.myspace.musiccatalogue.JdbcCodec;
import io.pgenie.artifacts.myspace.musiccatalogue.Statement;
import io.pgenie.artifacts.myspace.musiccatalogue.types.*;

/**
 * Type-safe binding for the {@code select_genre_by_artist} query.
 *
 * <h2>SQL Template</h2>
 *
 * <pre>{@code
 * select id, genre.name
 * from genre
 * left join album_genre on album_genre.genre = genre.id
 * left join album_artist on album_artist.album = album_genre.album
 * where album_artist.artist = $artist
 * }</pre>
 *
 * <h2>Source Path</h2> {@code ./queries/select_genre_by_artist.sql}
 *
 * <p>
 * Generated from SQL queries using the
 * <a href="https://pgenie.io">pGenie</a> code generator.
 */
public record SelectGenreByArtist(
        /**
         * Maps to {@code $artist} in the template.
         */
        int artist)
        implements Statement<SelectGenreByArtist.Output> {

    // -------------------------------------------------------------------------
    // Result type
    // -------------------------------------------------------------------------
    /**
     * Result of the statement parameterised by {@link SelectGenreByArtist}.
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
            int id,
            /**
             * Maps to the {@code name} result-set column.
             */
            String name) {}

    // -------------------------------------------------------------------------
    // Statement implementation
    // -------------------------------------------------------------------------
    @Override
    public String sql() {
        return """
               select id, genre.name
               from genre
               left join album_genre on album_genre.genre = genre.id
               left join album_artist on album_artist.album = album_genre.album
               where album_artist.artist = ?
               """;
    }

    @Override
    public void bindParams(PreparedStatement ps) throws SQLException {
        ps.setInt(1, this.artist());
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
            int idCol = rs.getInt(1);
            String nameCol = rs.getString(2);

            output.add(new OutputRow(idCol, nameCol));
            row++;
        }

        return output;
    }

    @Override
    public SelectGenreByArtist.Output decodeAffectedRows(long affectedRows) {
        throw new UnsupportedOperationException();
    }
}
