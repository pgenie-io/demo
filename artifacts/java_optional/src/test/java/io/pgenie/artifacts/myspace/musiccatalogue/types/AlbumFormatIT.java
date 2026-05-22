package io.pgenie.artifacts.myspace.musiccatalogue.types;

import static org.junit.jupiter.api.Assertions.*;

import io.pgenie.artifacts.myspace.musiccatalogue.AbstractDatabaseIT;
import io.codemine.java.postgresql.jdbc.Statement;
import java.sql.*;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class AlbumFormatIT extends AbstractDatabaseIT {

    private Optional<AlbumFormat> roundtrip(AlbumFormat input) throws SQLException {
        return execute(new Statement<Optional<AlbumFormat>>() {
            @Override public String sql() { return "select ?::album_format"; }
            @Override public void bindParams(PreparedStatement ps) throws SQLException {
                AlbumFormat.CODEC.bind(ps, 1, input);
            }
            @Override public boolean returnsRows() { return true; }
            @Override public Optional<AlbumFormat> decodeResultSet(ResultSet rs) throws SQLException {
                rs.next();
                return AlbumFormat.CODEC.decodeOptional(rs, 0, 1);
            }
            @Override public Optional<AlbumFormat> decodeAffectedRows(long r) {
                throw new UnsupportedOperationException();
            }
        });
    }

    @Test
    void roundtripNull() throws SQLException {
        assertEquals(Optional.empty(), roundtrip(null));
    }

    @Test
    void roundtripVinyl() throws SQLException {
        assertEquals(Optional.of(AlbumFormat.Vinyl), roundtrip(AlbumFormat.Vinyl));
    }

    @Test
    void roundtripCd() throws SQLException {
        assertEquals(Optional.of(AlbumFormat.Cd), roundtrip(AlbumFormat.Cd));
    }

    @Test
    void roundtripCassette() throws SQLException {
        assertEquals(Optional.of(AlbumFormat.Cassette), roundtrip(AlbumFormat.Cassette));
    }

    @Test
    void roundtripDigital() throws SQLException {
        assertEquals(Optional.of(AlbumFormat.Digital), roundtrip(AlbumFormat.Digital));
    }

    @Test
    void roundtripDvdAudio() throws SQLException {
        assertEquals(Optional.of(AlbumFormat.DvdAudio), roundtrip(AlbumFormat.DvdAudio));
    }

    @Test
    void roundtripSacd() throws SQLException {
        assertEquals(Optional.of(AlbumFormat.Sacd), roundtrip(AlbumFormat.Sacd));
    }
}
