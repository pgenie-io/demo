package io.pgenie.artifacts.myspace.musiccatalogue.statements;

import static org.junit.jupiter.api.Assertions.*;

import io.pgenie.artifacts.myspace.musiccatalogue.AbstractDatabaseIT;
import io.pgenie.artifacts.myspace.musiccatalogue.types.*;
import java.sql.SQLException;
import java.time.*;
import org.junit.jupiter.api.Test;

class SelectGenreByArtistIT extends AbstractDatabaseIT {

    @Test
    void executesWithDefaultValues() throws SQLException {
        var result = execute(new SelectGenreByArtist(0));
        assertNotNull(result);

    }
}

