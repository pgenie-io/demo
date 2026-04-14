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
import io.codemine.java.postgresql.codecs.*;
import io.codemine.java.postgresql.codecs.*;

/**
 * Type-safe binding for the {@code select_genre_by_path} query.
 *
 * <h2>SQL Template</h2>
 *
 * <pre>{@code
 * select id, name, path
 * from genre
 * where path <@ $path
 * }</pre>
 *
 * <h2>Source Path</h2> {@code ./queries/select_genre_by_path.sql}
 *
 * <p>
 * Generated from SQL queries using the
 * <a href="https://pgenie.io">pGenie</a> code generator.
 */
public record SelectGenreByPath(
        /**
         * Maps to {@code $path} in the template.
         */
        Ltree path)
        implements Statement<SelectGenreByPath.Result> {
    
    // -------------------------------------------------------------------------
    // Result type
    // -------------------------------------------------------------------------
    /**
     * Result of the statement parameterised by {@link SelectGenreByPath}.
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
            int id,
            /**
             * Maps to the {@code name} result-set column.
             */
            String name,
            /**
             * Maps to the {@code path} result-set column.
             */
            Ltree path) {}
    
    // -------------------------------------------------------------------------
    // Statement implementation
    // -------------------------------------------------------------------------
    @Override
    public String sql() {
        return """
               select id, name, path
               from genre
               where path <@ ?
               """;
    }

    @Override
    public void bindParams(PreparedStatement ps) throws SQLException {
        Codec.LTREE.bind(ps, 1, this.path());
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
            int idCol = Codec.INT4.decodeNonNullable(rs, row, 1);
            String nameCol = Codec.TEXT.decodeNonNullable(rs, row, 2);
            Ltree pathCol = Codec.LTREE.decodeNonNullable(rs, row, 3);

            output.add(new ResultRow(idCol, nameCol, pathCol));
            row++;
        }

        return output;
    }

    @Override
    public SelectGenreByPath.Result decodeAffectedRows(long affectedRows) {
        throw new UnsupportedOperationException();
    }
}
