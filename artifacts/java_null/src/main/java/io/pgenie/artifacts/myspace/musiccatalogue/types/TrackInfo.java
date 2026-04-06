package io.pgenie.artifacts.myspace.musiccatalogue.types;

import java.time.*;
import java.util.List;

import io.codemine.java.postgresql.jdbc.Codec;

/**
 * Representation of the {@code track_info} user-declared PostgreSQL
 * composite (record) type.
 *
 * <p>
 * Generated from SQL queries using the
 * <a href="https://pgenie.io">pGenie</a> code generator.
 *
 * <p>
 * All fields are nullable, matching the PostgreSQL column definitions.
 */
public record TrackInfo(
        /**
         * Maps to {@code title}.
         */
        String title,
        /**
         * Maps to {@code duration_seconds}.
         */
        Integer durationSeconds,
        /**
         * Maps to {@code tags}.
         */
        List<String> tags) {

    public static final Codec<TrackInfo> CODEC = Codec.<TrackInfo>composite(
            "public", "track_info",
            objects -> new TrackInfo((( String ) objects[0]), (( Integer ) objects[1]), (( List<String> ) objects[2])),
            Codec.<TrackInfo, String>field("title", Codec.TEXT, TrackInfo::title),
            Codec.<TrackInfo, Integer>field("duration_seconds", Codec.INT4, TrackInfo::durationSeconds),
            Codec.<TrackInfo, List<String>>field("tags", Codec.TEXT.inDim(), TrackInfo::tags));

}
