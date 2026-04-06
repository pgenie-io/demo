package io.pgenie.artifacts.myspace.musiccatalogue.statements;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.*;
import java.util.Optional;
import io.codemine.java.postgresql.jdbc.Codec;
import io.codemine.java.postgresql.jdbc.Statement;
import io.pgenie.artifacts.myspace.musiccatalogue.types.*;

/**
 * Type-safe binding for the {@code update_album_released} query.
 *
 * <h2>SQL Template</h2>
 *
 * <pre>{@code
 * update album
 * set released = $released
 * where id = $id
 * }</pre>
 *
 * <h2>Source Path</h2> {@code ./queries/update_album_released.sql}
 *
 * <p>
 * Generated from SQL queries using the
 * <a href="https://pgenie.io">pGenie</a> code generator.
 */
public record UpdateAlbumReleased(
        /**
         * Maps to {@code $released} in the template. Nullable.
         */
        Optional<LocalDate> released,
        /**
         * Maps to {@code $id} in the template.
         */
        long id)
        implements Statement<Long> {
    
    // -------------------------------------------------------------------------
    // Statement implementation
    // -------------------------------------------------------------------------
    @Override
    public String sql() {
        return """
               update album
               set released = ?
               where id = ?
               """;
    }

    @Override
    public void bindParams(PreparedStatement ps) throws SQLException {
        Codec.DATE.bind(ps, 1, this.released().orElse(null));
        Codec.INT8.bind(ps, 2, this.id());
    }

    /**
     * Returns the number of rows affected by the statement.
     */
    @Override
    public boolean returnsRows() {
        return false;
    }

    /**
     * Returns the number of rows affected by the statement.
     *
     * <p>
     * Uses {@code affectedRows} forwarded from
     * {@link java.sql.PreparedStatement#executeUpdate()}.
     */
    @Override
    public Long decodeAffectedRows(long affectedRows) throws SQLException {
        return affectedRows;
    }

    @Override
    public Long decodeResultSet(ResultSet rs) {
        throw new UnsupportedOperationException();
    }
}
