package io.pgenie.artifacts.myspace.musiccatalogue.types;

import java.time.*;
import java.util.List;
import io.codemine.java.postgresql.jdbc.Codec;

/**
 * Representation of the {@code album} user-declared PostgreSQL
 * composite (record) type.
 *
 * <p>
 * Generated from SQL queries using the
 * <a href="https://pgenie.io">pGenie</a> code generator.
 *
 * <p>
 * All fields are nullable, matching the PostgreSQL column definitions.
 */
public record Album(
        /**
         * Maps to {@code id}.
         */
        Long id,
        /**
         * Maps to {@code name}.
         */
        String name,
        /**
         * Maps to {@code released}.
         */
        LocalDate released,
        /**
         * Maps to {@code format}.
         */
        AlbumFormat format,
        /**
         * Maps to {@code recording}.
         */
        RecordingInfo recording,
        /**
         * Maps to {@code tracks}.
         */
        List<TrackInfo> tracks,
        /**
         * Maps to {@code disc}.
         */
        DiscInfo disc) {

    public static final Codec<Album> CODEC = Codec.<Album>composite(
            "public", "album",
            objects -> new Album((( Long ) objects[0]), (( String ) objects[1]), (( LocalDate ) objects[2]), (( AlbumFormat ) objects[3]), (( RecordingInfo ) objects[4]), (( List<TrackInfo> ) objects[5]), (( DiscInfo ) objects[6])),
            Codec.<Album, Long>field("id", Codec.INT8, Album::id),
            Codec.<Album, String>field("name", Codec.TEXT, Album::name),
            Codec.<Album, LocalDate>field("released", Codec.DATE, Album::released),
            Codec.<Album, AlbumFormat>field("format", AlbumFormat.CODEC, Album::format),
            Codec.<Album, RecordingInfo>field("recording", RecordingInfo.CODEC, Album::recording),
            Codec.<Album, List<TrackInfo>>field("tracks", TrackInfo.CODEC.inDim(), Album::tracks),
            Codec.<Album, DiscInfo>field("disc", DiscInfo.CODEC, Album::disc));

}
