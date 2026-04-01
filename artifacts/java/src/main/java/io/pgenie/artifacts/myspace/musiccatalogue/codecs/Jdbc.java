package io.pgenie.artifacts.myspace.musiccatalogue.codecs;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.postgresql.util.PGobject;

import io.codemine.java.postgresql.codecs.Codec;

/**
 * JDBC binding utilities for the {@code postgresql-codecs} library.
 *
 * <p>
 * Provides a thin bridge between driver-agnostic {@link Codec} instances
 * and the PostgreSQL JDBC driver ({@code pgjdbc}). Values are encoded as
 * text-format {@link PGobject} instances so that the driver sends the
 * correct type OID.
 */
public final class Jdbc {

    private Jdbc() {
    }

    /**
     * Binds a value to a prepared statement parameter using a codec.
     *
     * @param ps    the prepared statement
     * @param index the 1-based parameter index
     * @param codec the codec to use for encoding
     * @param value the value to bind (may be {@code null})
     * @param <A>   the value type
     */
    public static <A> void bind(PreparedStatement ps, int index, Codec<A> codec, A value) throws SQLException {
        PGobject obj = new PGobject();
        obj.setType(codec.typeSig());
        if (value != null) {
            obj.setValue(codec.encodeInTextToString(value));
        }
        ps.setObject(index, obj);
    }

}
