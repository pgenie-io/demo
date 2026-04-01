package io.pgenie.artifacts.myspace.musiccatalogue.types;

import java.time.*;
import java.util.List;

import io.codemine.java.postgresql.codecs.Codec;
import io.codemine.java.postgresql.codecs.CompositeCodec;

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

    public static final CompositeCodec<DiscInfo> CODEC = new CompositeCodec<>(
            "public", "disc_info",
            (String name) -> (RecordingInfo recording) -> new DiscInfo(
                    name, recording),
            new CompositeCodec.Field<>("name", DiscInfo::name, Codec.TEXT),
            new CompositeCodec.Field<>("recording", DiscInfo::recording, RecordingInfo.CODEC));

}
