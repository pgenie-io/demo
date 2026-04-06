package io.pgenie.artifacts.myspace.musiccatalogue.statements;

import static org.junit.jupiter.api.Assertions.*;

import io.pgenie.artifacts.myspace.musiccatalogue.AbstractDatabaseIT;
import io.pgenie.artifacts.myspace.musiccatalogue.types.*;
import java.sql.SQLException;
import java.time.*;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class UpdateAlbumReleasedIT extends AbstractDatabaseIT {

    @Test
    void executesWithDefaultValues() throws SQLException {
        var result = execute(new UpdateAlbumReleased(Optional.empty(), 0L));
        assertTrue(result >= 0L);

    }
}
