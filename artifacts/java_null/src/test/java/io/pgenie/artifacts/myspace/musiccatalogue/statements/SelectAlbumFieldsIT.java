package io.pgenie.artifacts.myspace.musiccatalogue.statements;

import static org.junit.jupiter.api.Assertions.*;

import io.pgenie.artifacts.myspace.musiccatalogue.AbstractDatabaseIT;
import io.pgenie.artifacts.myspace.musiccatalogue.types.*;
import java.sql.SQLException;
import java.time.*;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class SelectAlbumFieldsIT extends AbstractDatabaseIT {

    @Test
    void executesWithDefaultValues() throws SQLException {
        var result = execute(new SelectAlbumFields(false, false, false, false, false, false, 0L));
        assertNotNull(result);

    }
}
