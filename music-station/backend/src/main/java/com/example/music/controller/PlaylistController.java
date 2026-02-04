package com.example.music.controller;

import com.example.music.dto.PlaylistDto;
import com.example.music.dto.PlaylistRequest;
import com.example.music.dto.PlaylistSongDto;
import com.example.music.dto.PlaylistSongRequest;
import com.example.music.dto.Result;
import com.example.music.service.PlaylistService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/playlists")
public class PlaylistController {
    private final PlaylistService playlistService;

    public PlaylistController(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    @GetMapping
    public Result<List<PlaylistDto>> list(Authentication authentication) {
        String username = authentication.getName();
        List<PlaylistDto> playlists = playlistService.getPlaylists(username).stream()
                .map(PlaylistDto::from)
                .collect(Collectors.toList());
        return Result.ok(playlists);
    }

    @PostMapping
    public Result<PlaylistDto> create(Authentication authentication, @RequestBody PlaylistRequest request) {
        String username = authentication.getName();
        return Result.ok(PlaylistDto.from(playlistService.createPlaylist(username, request.getName())));
    }

    @GetMapping("/{playlistId}/songs")
    public Result<List<PlaylistSongDto>> listSongs(@PathVariable Long playlistId) {
        return Result.ok(playlistService.getPlaylistSongs(playlistId));
    }

    @PostMapping("/{playlistId}/songs")
    public Result<Void> addSong(@PathVariable Long playlistId, @RequestBody PlaylistSongRequest request) {
        playlistService.addSong(playlistId, request.getSongId());
        return Result.ok(null);
    }

    @DeleteMapping("/{playlistId}/songs/{songId}")
    public Result<Void> removeSong(@PathVariable Long playlistId, @PathVariable Long songId) {
        playlistService.removeSong(playlistId, songId);
        return Result.ok(null);
    }
}
