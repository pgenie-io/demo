package io.pgenie.artifacts.myspace.musiccatalogue;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.postgresql.util.PGobject;

/**
 * Adapter between the {@code postgresql-codecs} library and JDBC. Provides
 * utilities for encoding and decoding values using {@link Codec} instances and
 * binding them to JDBC statements.
 */
public final class JdbcCodec<A> {

    private final io.codemine.java.postgresql.codecs.Codec<A> codec;

    public JdbcCodec(io.codemine.java.postgresql.codecs.Codec<A> codec) {
        this.codec = codec;
    }

    public void bind(PreparedStatement ps, int index, A value) throws SQLException {
        PGobject obj = new PGobject();
        obj.setType(codec.typeSig());
        if (value != null) {
            obj.setValue(codec.encodeInTextToString(value));
        }
        ps.setObject(index, obj);
    }

    public A decodeNullable(ResultSet rs, int row, int col) throws SQLException {
        String text = rs.getString(col);
        if (text == null) {
            return null;
        }
        try {
            return codec.decodeInTextFromString(text);
        } catch (io.codemine.java.postgresql.codecs.Codec.DecodingException e) {
            throw new SQLException(
                    "Failed to decode cell at row " + row + ", column " + col,
                    "22000",
                    e);
        }
    }

    public A decodeNonNullable(ResultSet rs, int row, int col) throws SQLException {
        String text = rs.getString(col);
        if (text == null) {
            throw new SQLException(
                    "Unexpected NULL value at row " + row + ", column " + col,
                    "22004");
        }
        try {
            return codec.decodeInTextFromString(text);
        } catch (io.codemine.java.postgresql.codecs.Codec.DecodingException e) {
            throw new SQLException(
                    "Failed to decode cell at row " + row + ", column " + col,
                    "22000",
                    e);
        }
    }

}
