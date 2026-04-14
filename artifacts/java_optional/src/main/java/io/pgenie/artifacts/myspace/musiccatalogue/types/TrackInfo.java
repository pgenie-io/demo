package io.pgenie.artifacts.myspace.musiccatalogue.types;

import java.time.*;
import java.util.List;
import java.util.Optional;
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
        Optional<String> title,
        /**
         * Maps to {@code duration_seconds}.
         */
        Optional<Integer> durationSeconds,
        /**
         * Maps to {@code tags}.
         */
        Optional<List<Optional<String>>> tags) {

    public static final Codec<TrackInfo> CODEC = Codec.<TrackInfo>composite(
            "public", "track_info",
            objects -> new TrackInfo((( Optional<String> ) objects[0]), (( Optional<Integer> ) objects[1]), (( Optional<List<Optional<String>>> ) objects[2])),
            Codec.<TrackInfo, String>field("title", Codec.TEXT, row -> row.title().orElse(null)),
            Codec.<TrackInfo, Integer>field("duration_seconds", Codec.INT4, row -> row.durationSeconds().orElse(null)),
            Codec.<TrackInfo, List<String>>field("tags", Codec.TEXT.inDim(), row -> row.tags().map(list -> list.stream().map(o -> o.orElse(null)).toList()).orElse(null)));

}
