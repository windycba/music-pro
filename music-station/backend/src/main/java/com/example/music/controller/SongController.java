package com.example.music.controller;

import com.example.music.dto.Result;
import com.example.music.dto.SongDto;
import com.example.music.entity.Song;
import com.example.music.repository.SongRepository;
import com.example.music.service.SongService;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
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
    public ResponseEntity<StreamingResponseBody> stream(@PathVariable Long id) throws IOException {
        Song song = songRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("歌曲不存在"));
        if (!song.getEnabled()) {
            throw new IllegalArgumentException("歌曲已禁用");
        }
        Path path = Path.of(song.getFilePath());
        StreamingResponseBody body = outputStream -> Files.copy(path, outputStream);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + path.getFileName() + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(body);
    }
}
