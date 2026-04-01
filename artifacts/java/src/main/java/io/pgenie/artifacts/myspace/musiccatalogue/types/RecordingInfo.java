package io.pgenie.artifacts.myspace.musiccatalogue.types;

import java.time.*;
import java.util.List;

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

    public static final CompositeCodec<RecordingInfo> CODEC = new CompositeCodec<>(
            "public", "recording_info",
            (String studioName) -> (String city) -> (String country) -> (LocalDate recordedDate) -> new RecordingInfo(
                    studioName, city, country, recordedDate),
            new CompositeCodec.Field<>("studio_name", RecordingInfo::studioName, Codec.TEXT),
            new CompositeCodec.Field<>("city", RecordingInfo::city, Codec.TEXT),
            new CompositeCodec.Field<>("country", RecordingInfo::country, Codec.TEXT),
            new CompositeCodec.Field<>("recorded_date", RecordingInfo::recordedDate, Codec.DATE));

}
