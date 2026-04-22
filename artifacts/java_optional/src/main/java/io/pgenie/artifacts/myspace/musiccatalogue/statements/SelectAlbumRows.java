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
 * Type-safe binding for the {@code select_album_rows} query.
 *
 * <h2>SQL Template</h2>
 *
 * <pre>{@code
 * select (album.*)::album from album
 * }</pre>
 *
 * <h2>Source Path</h2> {@code ./queries/select_album_rows.sql}
 *
 * <p>
 * Generated from SQL queries using the
 * <a href="https://pgenie.io">pGenie</a> code generator.
 */
public record SelectAlbumRows(
        )
        implements Statement<SelectAlbumRows.Result> {
    
    // -------------------------------------------------------------------------
    // Result type
    // -------------------------------------------------------------------------
    /**
     * Result of the statement parameterised by {@link SelectAlbumRows}.
     */
    public static final class Result extends ArrayList<ResultRow> {
        Result() {}
    }

    /**
     * Row of {@link Result}.
     */
    public record ResultRow(
            /**
             * Maps to the {@code album} result-set column. Nullable.
             */
            Optional<Album> album) {}
    
    // -------------------------------------------------------------------------
    // Statement implementation
    // -------------------------------------------------------------------------
    @Override
    public String sql() {
        return """
               select (album.*)::album from album
               """;
    }

    @Override
    public void bindParams(PreparedStatement ps) throws SQLException {
        
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
            Optional<Album> albumCol = Album.CODEC.decodeOptional(rs, row, 1);

            output.add(new ResultRow(albumCol));
            row++;
        }

        return output;
    }

    @Override
    public SelectAlbumRows.Result decodeAffectedRows(long affectedRows) {
        throw new UnsupportedOperationException();
    }
}
