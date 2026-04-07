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
 * Type-safe binding for the {@code insert_multiple_albums} query.
 *
 * <h2>SQL Template</h2>
 *
 * <pre>{@code
 * -- This is an example of a bulk-insert (batch-insert) technique.
 * -- We pass in all fields as arrays of the same size, and we unnest it to insert multiple rows at once.
 * insert into album (name, released, format)
 * select *
 * from unnest(
 *   $name::text[],
 *   $released::date[],
 *   $format::album_format[]
 * )
 * returning id
 * }</pre>
 *
 * <h2>Source Path</h2> {@code ./queries/insert_multiple_albums.sql}
 *
 * <p>
 * Generated from SQL queries using the
 * <a href="https://pgenie.io">pGenie</a> code generator.
 */
public record InsertMultipleAlbums(
        /**
         * Maps to {@code $name} in the template.
         */
        List<String> name,
        /**
         * Maps to {@code $released} in the template.
         */
        List<LocalDate> released,
        /**
         * Maps to {@code $format} in the template.
         */
        List<AlbumFormat> format)
        implements Statement<InsertMultipleAlbums.Result> {
    
    // -------------------------------------------------------------------------
    // Result type
    // -------------------------------------------------------------------------
    /**
     * Result of the statement parameterised by {@link InsertMultipleAlbums}.
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
            long id) {}
    
    // -------------------------------------------------------------------------
    // Statement implementation
    // -------------------------------------------------------------------------
    @Override
    public String sql() {
        return """
               -- This is an example of a bulk-insert (batch-insert) technique.
               -- We pass in all fields as arrays of the same size, and we unnest it to insert multiple rows at once.
               insert into album (name, released, format)
               select *
               from unnest(
                 ?::text[],
                 ?::date[],
                 ?::album_format[]::album_format[]
               )
               returning id
               """;
    }

    @Override
    public void bindParams(PreparedStatement ps) throws SQLException {
        Codec.TEXT.inDim().bind(ps, 1, this.name());
        Codec.DATE.inDim().bind(ps, 2, this.released());
        AlbumFormat.CODEC.inDim().bind(ps, 3, this.format());
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

            output.add(new ResultRow(idCol));
            row++;
        }

        return output;
    }

    @Override
    public InsertMultipleAlbums.Result decodeAffectedRows(long affectedRows) {
        throw new UnsupportedOperationException();
    }
}
