package io.pgenie.artifacts.myspace.musiccatalogue.types;

import static org.junit.jupiter.api.Assertions.*;

import io.pgenie.artifacts.myspace.musiccatalogue.AbstractDatabaseIT;
import io.codemine.java.postgresql.jdbc.Statement;
import java.sql.*;
import java.time.*;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class DiscInfoIT extends AbstractDatabaseIT {

    private Optional<DiscInfo> roundtrip(DiscInfo input) throws SQLException {
        return execute(new Statement<Optional<DiscInfo>>() {
            @Override public String sql() { return "select ?::disc_info"; }
            @Override public void bindParams(PreparedStatement ps) throws SQLException {
                DiscInfo.CODEC.bind(ps, 1, input);
            }
            @Override public boolean returnsRows() { return true; }
            @Override public Optional<DiscInfo> decodeResultSet(ResultSet rs) throws SQLException {
                rs.next();
                return DiscInfo.CODEC.decodeOptional(rs, 0, 1);
            }
            @Override public Optional<DiscInfo> decodeAffectedRows(long r) {
                throw new UnsupportedOperationException();
            }
        });
    }

    @Test
    void roundtripNull() throws SQLException {
        assertEquals(Optional.empty(), roundtrip(null));
    }

    @Test
    void roundtripCombination0() throws SQLException {
        var value = new DiscInfo(Optional.empty(), Optional.empty());
        assertEquals(Optional.of(value), roundtrip(value));
    }

    @Test
    void roundtripCombination1() throws SQLException {
        var value = new DiscInfo(Optional.of(""), Optional.empty());
        assertEquals(Optional.of(value), roundtrip(value));
    }

    @Test
    void roundtripCombination2() throws SQLException {
        var value = new DiscInfo(Optional.empty(), Optional.of(RecordingInfo.CODEC.toAgnostic().random(new java.util.Random(0L), 0)));
        assertEquals(Optional.of(value), roundtrip(value));
    }

    @Test
    void roundtripCombination3() throws SQLException {
        var value = new DiscInfo(Optional.of(""), Optional.of(RecordingInfo.CODEC.toAgnostic().random(new java.util.Random(0L), 0)));
        assertEquals(Optional.of(value), roundtrip(value));
    }
}
