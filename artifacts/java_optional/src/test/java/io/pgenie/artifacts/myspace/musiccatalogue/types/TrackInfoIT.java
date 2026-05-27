package io.pgenie.artifacts.myspace.musiccatalogue.types;

import static org.junit.jupiter.api.Assertions.*;

import io.pgenie.artifacts.myspace.musiccatalogue.AbstractDatabaseIT;
import io.codemine.java.postgresql.jdbc.Statement;
import java.sql.*;
import java.time.*;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class TrackInfoIT extends AbstractDatabaseIT {

    private Optional<TrackInfo> roundtrip(Optional<TrackInfo> input) throws SQLException {
        return execute(new Statement<Optional<TrackInfo>>() {
            @Override public String sql() { return "select ?::track_info"; }
            @Override public void bindParams(PreparedStatement ps) throws SQLException {
                TrackInfo.CODEC.bind(ps, 1, input.orElse(null));
            }
            @Override public boolean returnsRows() { return true; }
            @Override public Optional<TrackInfo> decodeResultSet(ResultSet rs) throws SQLException {
                rs.next();
                return TrackInfo.CODEC.decodeOptional(rs, 0, 1);
            }
            @Override public Optional<TrackInfo> decodeAffectedRows(long r) {
                throw new UnsupportedOperationException();
            }
        });
    }
    

    @Test
    void roundtripNull() throws SQLException {
        assertEquals(Optional.empty(), roundtrip(Optional.empty()));
    }
    

    @Test
    void roundtripCombination0() throws SQLException {
        var value = new TrackInfo("", 0, List.of());
        assertEquals(Optional.of(value), roundtrip(Optional.of(value)));
    }
}
