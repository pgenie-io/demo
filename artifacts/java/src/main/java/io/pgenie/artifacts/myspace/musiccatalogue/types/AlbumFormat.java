package io.pgenie.artifacts.myspace.musiccatalogue.types;

import java.util.Map;

import io.codemine.java.postgresql.jdbc.Codec;

/**
 * Representation of the {@code album_format} user-declared PostgreSQL
 * enumeration type.
 *
 * <p>
 * Generated from SQL queries using the
 * <a href="https://pgenie.io">pGenie</a> code generator.
 */
public enum AlbumFormat {

    /**
     * Corresponds to the PostgreSQL enum variant {@code Vinyl}.
     */
    Vinyl,
    /**
     * Corresponds to the PostgreSQL enum variant {@code CD}.
     */
    Cd,
    /**
     * Corresponds to the PostgreSQL enum variant {@code Cassette}.
     */
    Cassette,
    /**
     * Corresponds to the PostgreSQL enum variant {@code Digital}.
     */
    Digital,
    /**
     * Corresponds to the PostgreSQL enum variant {@code DVD-Audio}.
     */
    DvdAudio,
    /**
     * Corresponds to the PostgreSQL enum variant {@code SACD}.
     */
    Sacd;

    public static final Codec<AlbumFormat> CODEC = Codec.enumeration(
            "public", "album_format",
            Map.ofEntries(
                    Map.entry(Vinyl, "Vinyl"),
                    Map.entry(Cd, "CD"),
                    Map.entry(Cassette, "Cassette"),
                    Map.entry(Digital, "Digital"),
                    Map.entry(DvdAudio, "DVD-Audio"),
                    Map.entry(Sacd, "SACD")));

}
