package io.pgenie.artifacts.myspace.musiccatalogue.types;

import java.time.*;
import java.util.List;

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
        String title,
        /**
         * Maps to {@code duration_seconds}.
         */
        Integer durationSeconds,
        /**
         * Maps to {@code tags}.
         */
        List<String> tags) {

    public static final CompositeCodec<TrackInfo> CODEC = new CompositeCodec<>(
            "public", "track_info",
            (String title) -> (Integer durationSeconds) -> (List<String> tags) -> new TrackInfo(
                    title, durationSeconds, tags),
            new CompositeCodec.Field<>("title", TrackInfo::title, Codec.TEXT),
            new CompositeCodec.Field<>("duration_seconds", TrackInfo::durationSeconds, Codec.INT4),
            new CompositeCodec.Field<>("tags", TrackInfo::tags, Codec.TEXT.inDim()));

}
