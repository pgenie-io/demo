package io.pgenie.artifacts.myspace.musiccatalogue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Implemented by each query's parameter+result class. Provides a uniform way to
 * prepare and execute statements against a JDBC {@link java.sql.Connection}.
 *
 * <p>
 * Generated from SQL queries using the <a href="https://pgenie.io">pGenie</a>
 * code generator.
 *
 * @param <R> the result type returned by {@link #decodeResultSet} or
 * {@link #decodeAffectedRows}
 */
public interface Statement<R> {

    /**
     * The SQL text for this statement. Parameter placeholders use JDBC
     * {@code ?} syntax; custom PostgreSQL types are cast explicitly, e.g.
     * {@code ?::album_format}.
     */
    String sql();

    /**
     * Bind to the prepared statement's parameter slots.
     */
    void bindParams(PreparedStatement ps) throws SQLException;

    /**
     * Whether this statement returns rows (i.e. is a {@code SELECT} or contains
     * a {@code RETURNING} clause).
     */
    boolean returnsRows();

    /**
     * Decode a result set into the statement's result type.
     */
    R decodeResultSet(ResultSet rs) throws SQLException;

    /**
     * Decode an affected-row count into the statement's result type.
     */
    R decodeAffectedRows(long affectedRows) throws SQLException;

    /** Execute this statement using the provided JDBC connection. */
    default R execute(Connection conn) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(sql())) {
            bindParams(ps);
            if (returnsRows()) {
                ps.execute();
                try (ResultSet rs = ps.getResultSet()) {
                    return decodeResultSet(rs);
                }
            } else {
                long affectedRows = ps.executeUpdate();
                return decodeAffectedRows(affectedRows);
            }
        }
    }
}
