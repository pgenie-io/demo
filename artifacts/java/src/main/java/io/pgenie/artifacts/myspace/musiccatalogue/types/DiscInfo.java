package io.pgenie.artifacts.myspace.musiccatalogue.types;

import java.time.*;
import java.util.List;
import java.util.Optional;

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
        Optional<String> name,
        /**
         * Maps to {@code recording}.
         */
        Optional<RecordingInfo> recording) {

    public static final CompositeCodec<DiscInfo> CODEC = new CompositeCodec<>(
            "public", "disc_info",
            (String name) -> (RecordingInfo recording) -> new DiscInfo(
                    Optional.ofNullable(name), Optional.ofNullable(recording)),
            new CompositeCodec.Field<>("name", row -> row.name().orElse(null), Codec.TEXT),
            new CompositeCodec.Field<>("recording", row -> row.recording().orElse(null), RecordingInfo.CODEC));

}
