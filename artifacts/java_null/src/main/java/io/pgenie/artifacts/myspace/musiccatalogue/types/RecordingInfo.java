package io.pgenie.artifacts.myspace.musiccatalogue.types;

import java.time.*;
import java.util.List;
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
        String studioName,
        /**
         * Maps to {@code city}.
         */
        String city,
        /**
         * Maps to {@code country}.
         */
        String country,
        /**
         * Maps to {@code recorded_date}.
         */
        LocalDate recordedDate) {

    public static final Codec<RecordingInfo> CODEC = Codec.<RecordingInfo>composite(
            "public", "recording_info",
            objects -> new RecordingInfo((( String ) objects[0]), (( String ) objects[1]), (( String ) objects[2]), (( LocalDate ) objects[3])),
            Codec.<RecordingInfo, String>field("studio_name", Codec.TEXT, RecordingInfo::studioName),
            Codec.<RecordingInfo, String>field("city", Codec.TEXT, RecordingInfo::city),
            Codec.<RecordingInfo, String>field("country", Codec.TEXT, RecordingInfo::country),
            Codec.<RecordingInfo, LocalDate>field("recorded_date", Codec.DATE, RecordingInfo::recordedDate));

}
