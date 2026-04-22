package io.pgenie.artifacts.myspace.musiccatalogue.types;

import java.time.*;
import java.util.List;
import java.util.Optional;
import io.codemine.java.postgresql.jdbc.Codec;

/**
 * Representation of the {@code album} user-declared PostgreSQL
 * composite (record) type.
 *
 * <p>
 * Generated from SQL queries using the
 * <a href="https://pgenie.io">pGenie</a> code generator.
 *
 * <p>
 * All fields are nullable, matching the PostgreSQL column definitions.
 */
public record Album(
        /**
         * Maps to {@code id}.
         */
        Optional<Long> id,
        /**
         * Maps to {@code name}.
         */
        Optional<String> name,
        /**
         * Maps to {@code released}.
         */
        Optional<LocalDate> released,
        /**
         * Maps to {@code format}.
         */
        Optional<AlbumFormat> format,
        /**
         * Maps to {@code recording}.
         */
        Optional<RecordingInfo> recording,
        /**
         * Maps to {@code tracks}.
         */
        Optional<List<Optional<TrackInfo>>> tracks,
        /**
         * Maps to {@code disc}.
         */
        Optional<DiscInfo> disc) {

    public static final Codec<Album> CODEC = Codec.<Album>composite(
            "public", "album",
            objects -> new Album((( Optional<Long> ) objects[0]), (( Optional<String> ) objects[1]), (( Optional<LocalDate> ) objects[2]), (( Optional<AlbumFormat> ) objects[3]), (( Optional<RecordingInfo> ) objects[4]), (( Optional<List<Optional<TrackInfo>>> ) objects[5]), (( Optional<DiscInfo> ) objects[6])),
            Codec.<Album, Long>field("id", Codec.INT8, row -> row.id().orElse(null)),
            Codec.<Album, String>field("name", Codec.TEXT, row -> row.name().orElse(null)),
            Codec.<Album, LocalDate>field("released", Codec.DATE, row -> row.released().orElse(null)),
            Codec.<Album, AlbumFormat>field("format", AlbumFormat.CODEC, row -> row.format().orElse(null)),
            Codec.<Album, RecordingInfo>field("recording", RecordingInfo.CODEC, row -> row.recording().orElse(null)),
            Codec.<Album, List<TrackInfo>>field("tracks", TrackInfo.CODEC.inDim(), row -> row.tracks().map(list -> list.stream().map(o -> o.orElse(null)).toList()).orElse(null)),
            Codec.<Album, DiscInfo>field("disc", DiscInfo.CODEC, row -> row.disc().orElse(null)));

}
