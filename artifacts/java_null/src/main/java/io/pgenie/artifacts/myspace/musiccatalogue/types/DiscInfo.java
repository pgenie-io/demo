package io.pgenie.artifacts.myspace.musiccatalogue.types;

import java.time.*;
import java.util.List;

import io.codemine.java.postgresql.jdbc.Codec;

/**
 * Representation of the {@code disc_info} user-declared PostgreSQL
 * composite (record) type.
 *
 * <p>
 * Generated from SQL queries using the
 * <a href="https://pgenie.io">pGenie</a> code generator.
 *
 * <p>
 * All fields are nullable, matching the PostgreSQL column definitions.
 */
public record DiscInfo(
        /**
         * Maps to {@code name}.
         */
        String name,
        /**
         * Maps to {@code recording}.
         */
        RecordingInfo recording) {

    public static final Codec<DiscInfo> CODEC = Codec.<DiscInfo>composite(
            "public", "disc_info",
            objects -> new DiscInfo((( String ) objects[0]), (( RecordingInfo ) objects[1])),
            Codec.<DiscInfo, String>field("name", Codec.TEXT, DiscInfo::name),
            Codec.<DiscInfo, RecordingInfo>field("recording", RecordingInfo.CODEC, DiscInfo::recording));

}
