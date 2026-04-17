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
 * Type-safe binding for the {@code select_track_info} query.
 *
 * <h2>SQL Template</h2>
 *
 * <pre>{@code
 * select
 *   (
 *     'title',
 *     180,
 *     array['a', 'b']
 *   )::track_info as track_info,
 *   234 as id
 * }</pre>
 *
 * <h2>Source Path</h2> {@code ./queries/select_track_info.sql}
 *
 * <p>
 * Generated from SQL queries using the
 * <a href="https://pgenie.io">pGenie</a> code generator.
 */
public record SelectTrackInfo(
        )
        implements Statement<SelectTrackInfo.Result> {
    
    // -------------------------------------------------------------------------
    // Result type
    // -------------------------------------------------------------------------
    /**
     * Result of the statement parameterised by {@link SelectTrackInfo}.
     */
    public record Result(
            /**
             * Maps to the {@code track_info} result-set column. Nullable.
             */
            TrackInfo trackInfo,
            /**
             * Maps to the {@code id} result-set column. Nullable.
             */
            int id) {}
    
    // -------------------------------------------------------------------------
    // Statement implementation
    // -------------------------------------------------------------------------
    @Override
    public String sql() {
        return """
               select
                 (
                   'title',
                   180,
                   array['a', 'b']
                 )::track_info as track_info,
                 234 as id
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
        rs.next();

        TrackInfo trackInfoCol = TrackInfo.CODEC.decodeNullable(rs, 0, 1);
        int idCol = Codec.INT4.decodeNullable(rs, 0, 2);

        return new Result(trackInfoCol, idCol);
    }

    @Override
    public SelectTrackInfo.Result decodeAffectedRows(long affectedRows) {
        throw new UnsupportedOperationException();
    }
}
