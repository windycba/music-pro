package com.example.music.controller;

import com.example.music.dto.Result;
import com.example.music.dto.SongDto;
import com.example.music.entity.Song;
import com.example.music.repository.SongRepository;
import com.example.music.service.SongService;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@RestController
@RequestMapping("/api/songs")
public class SongController {
    private final SongService songService;
    private final SongRepository songRepository;

    public SongController(SongService songService, SongRepository songRepository) {
        this.songService = songService;
        this.songRepository = songRepository;
    }

    @GetMapping
    public Result<List<SongDto>> listEnabled() {
        List<SongDto> songs = songService.findAll().stream()
                .filter(Song::getEnabled)
                .map(SongDto::from)
                .collect(Collectors.toList());
        return Result.ok(songs);
    }

    @GetMapping("/{id}/stream")
    public ResponseEntity<StreamingResponseBody> stream(@PathVariable Long id, HttpServletRequest request)
            throws IOException {
        Song song = songRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("歌曲不存在"));
        if (!song.getEnabled()) {
            throw new IllegalArgumentException("歌曲已禁用");
        }
        Path path = Path.of(song.getFilePath());
        long fileSize = Files.size(path);
        String range = request.getHeader(HttpHeaders.RANGE);
        MediaType mediaType = resolveMediaType(path);
        if (range != null && range.startsWith("bytes=")) {
            String[] parts = range.substring(6).split("-", 2);
            long start = Long.parseLong(parts[0]);
            long end = (parts.length > 1 && !parts[1].isBlank()) ? Long.parseLong(parts[1]) : fileSize - 1;
            if (end >= fileSize) {
                end = fileSize - 1;
            }
            long contentLength = end - start + 1;
            StreamingResponseBody body = outputStream -> {
                try (InputStream inputStream = Files.newInputStream(path)) {
                    inputStream.skip(start);
                    byte[] buffer = new byte[8192];
                    long remaining = contentLength;
                    int read;
                    while (remaining > 0 && (read = inputStream.read(buffer, 0, (int) Math.min(buffer.length, remaining))) != -1) {
                        outputStream.write(buffer, 0, read);
                        remaining -= read;
                    }
                }
            };
            return ResponseEntity.status(206)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + path.getFileName() + "\"")
                    .header(HttpHeaders.CONTENT_RANGE, "bytes " + start + "-" + end + "/" + fileSize)
                    .header(HttpHeaders.ACCEPT_RANGES, "bytes")
                    .contentLength(contentLength)
                    .contentType(mediaType)
                    .body(body);
        }
        StreamingResponseBody body = outputStream -> Files.copy(path, outputStream);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + path.getFileName() + "\"")
                .header(HttpHeaders.ACCEPT_RANGES, "bytes")
                .contentLength(fileSize)
                .contentType(mediaType)
                .body(body);
    }

    private MediaType resolveMediaType(Path path) {
        String name = path.getFileName().toString().toLowerCase(Locale.ROOT);
        if (name.endsWith(".mp3")) {
            return MediaType.valueOf("audio/mpeg");
        }
        if (name.endsWith(".flac")) {
            return MediaType.valueOf("audio/flac");
        }
        return MediaType.APPLICATION_OCTET_STREAM;
    }
}
