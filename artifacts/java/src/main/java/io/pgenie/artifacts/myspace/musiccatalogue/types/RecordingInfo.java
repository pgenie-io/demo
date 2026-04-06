package io.pgenie.artifacts.myspace.musiccatalogue.types;

import java.time.*;
import java.util.List;
import java.util.Optional;

import io.codemine.java.postgresql.jdbc.Codec;

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

    public static final Codec<RecordingInfo> CODEC = Codec.<RecordingInfo>composite(
            "public", "recording_info",
            objects -> new RecordingInfo((( Optional<String> ) objects[0]), (( Optional<String> ) objects[1]), (( Optional<String> ) objects[2]), (( Optional<LocalDate> ) objects[3])),
            Codec.<RecordingInfo, String>field("studio_name", Codec.TEXT, row -> row.studioName().orElse(null)),
            Codec.<RecordingInfo, String>field("city", Codec.TEXT, row -> row.city().orElse(null)),
            Codec.<RecordingInfo, String>field("country", Codec.TEXT, row -> row.country().orElse(null)),
            Codec.<RecordingInfo, LocalDate>field("recorded_date", Codec.DATE, row -> row.recordedDate().orElse(null)));

}
