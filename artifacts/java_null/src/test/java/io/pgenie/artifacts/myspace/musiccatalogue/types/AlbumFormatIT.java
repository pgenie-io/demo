package io.pgenie.artifacts.myspace.musiccatalogue.types;

import static org.junit.jupiter.api.Assertions.*;

import io.pgenie.artifacts.myspace.musiccatalogue.AbstractDatabaseIT;
import io.codemine.java.postgresql.jdbc.Statement;
import java.sql.*;
import org.junit.jupiter.api.Test;

class AlbumFormatIT extends AbstractDatabaseIT {

    private AlbumFormat roundtrip(AlbumFormat input) throws SQLException {
        return execute(new Statement<AlbumFormat>() {
            @Override public String sql() { return "select ?::album_format"; }
            @Override public void bindParams(PreparedStatement ps) throws SQLException {
                AlbumFormat.CODEC.bind(ps, 1, input);
            }
            @Override public boolean returnsRows() { return true; }
            @Override public AlbumFormat decodeResultSet(ResultSet rs) throws SQLException {
                rs.next();
                return AlbumFormat.CODEC.decodeNullable(rs, 0, 1);
            }
            @Override public AlbumFormat decodeAffectedRows(long r) {
                throw new UnsupportedOperationException();
            }
        });
    }
    

    @Test
    void roundtripNull() throws SQLException {
        assertNull(roundtrip(null));
    }
    

    @Test
    void roundtripVinyl() throws SQLException {
        assertEquals(AlbumFormat.Vinyl, roundtrip(AlbumFormat.Vinyl));
    }

    @Test
    void roundtripCd() throws SQLException {
        assertEquals(AlbumFormat.Cd, roundtrip(AlbumFormat.Cd));
    }

    @Test
    void roundtripCassette() throws SQLException {
        assertEquals(AlbumFormat.Cassette, roundtrip(AlbumFormat.Cassette));
    }

    @Test
    void roundtripDigital() throws SQLException {
        assertEquals(AlbumFormat.Digital, roundtrip(AlbumFormat.Digital));
    }

    @Test
    void roundtripDvdAudio() throws SQLException {
        assertEquals(AlbumFormat.DvdAudio, roundtrip(AlbumFormat.DvdAudio));
    }

    @Test
    void roundtripSacd() throws SQLException {
        assertEquals(AlbumFormat.Sacd, roundtrip(AlbumFormat.Sacd));
    }
}
