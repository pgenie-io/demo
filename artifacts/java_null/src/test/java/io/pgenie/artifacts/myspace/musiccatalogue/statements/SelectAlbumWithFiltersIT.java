package io.pgenie.artifacts.myspace.musiccatalogue.statements;

import static org.junit.jupiter.api.Assertions.*;

import io.pgenie.artifacts.myspace.musiccatalogue.AbstractDatabaseIT;
import io.pgenie.artifacts.myspace.musiccatalogue.types.*;
import io.codemine.java.postgresql.jdbc.Codec;
import io.codemine.java.postgresql.codecs.*;
import java.util.List;
import java.sql.SQLException;
import java.time.*;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class SelectAlbumWithFiltersIT extends AbstractDatabaseIT {

    @Test
    void executesWithDefaultValues() throws SQLException {
        var result = execute(new SelectAlbumWithFilters(false, false, false, false, false, false, "", "", AlbumFormat.CODEC.toAgnostic().random(new java.util.Random(0L), 0), Codec.TIMESTAMP.toAgnostic().random(new java.util.Random(0L), 0), "", false, false));
        assertNotNull(result);

    }
}
