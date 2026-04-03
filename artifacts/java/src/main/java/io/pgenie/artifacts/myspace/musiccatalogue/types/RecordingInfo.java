package io.pgenie.artifacts.myspace.musiccatalogue.types;

import java.time.*;
import java.util.List;
import java.util.Optional;

import io.codemine.java.postgresql.codecs.Codec;
import io.codemine.java.postgresql.codecs.CompositeCodec;

/**
 * Representation of the {@code recording_info} user-declared PostgreSQL
 * composite (record) type.
 *
 * <p>
 * Generated from SQL queries using the
 * <a href="https://pgenie.io">pGenie</a> code generator.
 *
 * <p>
 * All fields are nullable, matching the PostgreSQL column definitions.
 */
public record RecordingInfo(
        /**
         * Maps to {@code studio_name}.
         */
        Optional<String> studioName,
        /**
         * Maps to {@code city}.
         */
        Optional<String> city,
        /**
         * Maps to {@code country}.
         */
        Optional<String> country,
        /**
         * Maps to {@code recorded_date}.
         */
        Optional<LocalDate> recordedDate) {

    public static final CompositeCodec<RecordingInfo> CODEC = new CompositeCodec<>(
            "public", "recording_info",
            (String studioName) -> (String city) -> (String country) -> (LocalDate recordedDate) -> new RecordingInfo(
                    Optional.ofNullable(studioName), Optional.ofNullable(city), Optional.ofNullable(country), Optional.ofNullable(recordedDate)),
            new CompositeCodec.Field<>("studio_name", row -> row.studioName().orElse(null), Codec.TEXT),
            new CompositeCodec.Field<>("city", row -> row.city().orElse(null), Codec.TEXT),
            new CompositeCodec.Field<>("country", row -> row.country().orElse(null), Codec.TEXT),
            new CompositeCodec.Field<>("recorded_date", row -> row.recordedDate().orElse(null), Codec.DATE));

}
