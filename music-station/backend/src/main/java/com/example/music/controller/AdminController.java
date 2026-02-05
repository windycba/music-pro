package com.example.music.controller;

import com.example.music.dto.Result;
import com.example.music.dto.SongDto;
import com.example.music.service.SongService;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final SongService songService;

    public AdminController(SongService songService) {
        this.songService = songService;
    }

    @GetMapping("/songs")
    public Result<List<SongDto>> listSongs() {
        return Result.ok(songService.findAll().stream().map(SongDto::from).collect(Collectors.toList()));
    }

    @PostMapping("/songs/scan")
    public Result<List<SongDto>> scan() throws IOException {
        return Result.ok(songService.scanAndImport().stream().map(SongDto::from).collect(Collectors.toList()));
    }

    @PutMapping("/songs/{id}/enable")
    public Result<SongDto> enable(@PathVariable Long id, @RequestParam boolean enabled) {
        return Result.ok(SongDto.from(songService.toggleEnabled(id, enabled)));
    }
}
