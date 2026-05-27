package io.pgenie.artifacts.myspace.musiccatalogue.types;

import static org.junit.jupiter.api.Assertions.*;

import io.pgenie.artifacts.myspace.musiccatalogue.AbstractDatabaseIT;
import io.codemine.java.postgresql.jdbc.Statement;
import java.sql.*;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class AlbumFormatIT extends AbstractDatabaseIT {

    private Optional<AlbumFormat> roundtrip(Optional<AlbumFormat> input) throws SQLException {
        return execute(new Statement<Optional<AlbumFormat>>() {
            @Override public String sql() { return "select ?::album_format"; }
            @Override public void bindParams(PreparedStatement ps) throws SQLException {
                AlbumFormat.CODEC.bind(ps, 1, input.orElse(null));
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
        assertEquals(Optional.empty(), roundtrip(Optional.empty()));
    }
    

    @Test
    void roundtripVinyl() throws SQLException {
        assertEquals(Optional.of(AlbumFormat.Vinyl), roundtrip(Optional.of(AlbumFormat.Vinyl)));
    }

    @Test
    void roundtripCd() throws SQLException {
        assertEquals(Optional.of(AlbumFormat.Cd), roundtrip(Optional.of(AlbumFormat.Cd)));
    }

    @Test
    void roundtripCassette() throws SQLException {
        assertEquals(Optional.of(AlbumFormat.Cassette), roundtrip(Optional.of(AlbumFormat.Cassette)));
    }

    @Test
    void roundtripDigital() throws SQLException {
        assertEquals(Optional.of(AlbumFormat.Digital), roundtrip(Optional.of(AlbumFormat.Digital)));
    }

    @Test
    void roundtripDvdAudio() throws SQLException {
        assertEquals(Optional.of(AlbumFormat.DvdAudio), roundtrip(Optional.of(AlbumFormat.DvdAudio)));
    }

    @Test
    void roundtripSacd() throws SQLException {
        assertEquals(Optional.of(AlbumFormat.Sacd), roundtrip(Optional.of(AlbumFormat.Sacd)));
    }
}
