package io.pgenie.artifacts.myspace.musiccatalogue.types;

import static org.junit.jupiter.api.Assertions.*;

import io.pgenie.artifacts.myspace.musiccatalogue.AbstractDatabaseIT;
import io.codemine.java.postgresql.jdbc.Statement;
import java.sql.*;
import java.time.*;
import java.util.List;
import org.junit.jupiter.api.Test;

class RecordingInfoIT extends AbstractDatabaseIT {

    private RecordingInfo roundtrip(RecordingInfo input) throws SQLException {
        return execute(new Statement<RecordingInfo>() {
            @Override public String sql() { return "select ?::recording_info"; }
            @Override public void bindParams(PreparedStatement ps) throws SQLException {
                RecordingInfo.CODEC.bind(ps, 1, input);
            }
            @Override public boolean returnsRows() { return true; }
            @Override public RecordingInfo decodeResultSet(ResultSet rs) throws SQLException {
                rs.next();
                return RecordingInfo.CODEC.decodeNullable(rs, 0, 1);
            }
            @Override public RecordingInfo decodeAffectedRows(long r) {
                throw new UnsupportedOperationException();
            }
        });
    }
    

    @Test
    void roundtripNull() throws SQLException {
        assertNull(roundtrip(null));
    }
    

    @Test
    void roundtripCombination0() throws SQLException {
        var value = new RecordingInfo("", "", "", LocalDate.of(2000, 1, 1));
        assertEquals(value, roundtrip(value));
    }
}
