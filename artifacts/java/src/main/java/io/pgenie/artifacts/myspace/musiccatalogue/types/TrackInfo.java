package io.pgenie.artifacts.myspace.musiccatalogue.types;

import java.time.*;
import java.util.List;
import java.util.Optional;

import io.codemine.java.postgresql.codecs.Codec;
import io.codemine.java.postgresql.codecs.CompositeCodec;

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

    public static final CompositeCodec<TrackInfo> CODEC = new CompositeCodec<>(
            "public", "track_info",
            (String title) -> (Integer durationSeconds) -> (List<String> tags) -> new TrackInfo(
                    Optional.ofNullable(title), Optional.ofNullable(durationSeconds), Optional.ofNullable(tags).map(list -> list.stream().map(Optional::ofNullable).toList())),
            new CompositeCodec.Field<>("title", row -> row.title().orElse(null), Codec.TEXT),
            new CompositeCodec.Field<>("duration_seconds", row -> row.durationSeconds().orElse(null), Codec.INT4),
            new CompositeCodec.Field<>("tags", row -> row.tags().map(list -> list.stream().map(o -> o.orElse(null)).toList()).orElse(null), Codec.TEXT.inDim()));

}
