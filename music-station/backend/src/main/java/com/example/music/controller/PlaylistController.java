package com.example.music.controller;

import com.example.music.dto.PlaylistDto;
import com.example.music.dto.PlaylistRequest;
import com.example.music.dto.PlaylistSongDto;
import com.example.music.dto.PlaylistSongRequest;
import com.example.music.dto.Result;
import com.example.music.security.TokenStore;
import com.example.music.service.PlaylistService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/playlists")
public class PlaylistController {
    private final PlaylistService playlistService;
    private final TokenStore tokenStore;

    public PlaylistController(PlaylistService playlistService, TokenStore tokenStore) {
        this.playlistService = playlistService;
        this.tokenStore = tokenStore;
    }

    @GetMapping
    public Result<List<PlaylistDto>> list(HttpServletRequest request) {
        String username = resolveUsername(request);
        List<PlaylistDto> playlists = playlistService.getPlaylists(username).stream()
                .map(PlaylistDto::from)
                .collect(Collectors.toList());
        return Result.ok(playlists);
    }

    @PostMapping
    public Result<PlaylistDto> create(HttpServletRequest request, @RequestBody PlaylistRequest playlistRequest) {
        String username = resolveUsername(request);
        return Result.ok(PlaylistDto.from(playlistService.createPlaylist(username, playlistRequest.getName())));
    }

    @GetMapping("/{playlistId}/songs")
    public Result<List<PlaylistSongDto>> listSongs(@PathVariable(name = "playlistId") Long playlistId) {
        return Result.ok(playlistService.getPlaylistSongs(playlistId));
    }

    @PostMapping("/{playlistId}/songs")
    public Result<Void> addSong(@PathVariable(name = "playlistId") Long playlistId, @RequestBody PlaylistSongRequest request) {
        playlistService.addSong(playlistId, request.getSongId());
        return Result.ok(null);
    }

    @DeleteMapping("/{playlistId}/songs/{songId}")
    public Result<Void> removeSong(@PathVariable Long playlistId, @PathVariable Long songId) {
        playlistService.removeSong(playlistId, songId);
        return Result.ok(null);
    }

    private String resolveUsername(HttpServletRequest request) {
        Object attr = request.getAttribute("username");
        if (attr != null) {
            return String.valueOf(attr);
        }
        String token = resolveToken(request);
        return tokenStore.findByToken(token)
                .map(TokenStore.SessionUser::username)
                .orElseThrow(() -> new IllegalArgumentException("未登录"));
    }

    private String resolveToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return request.getHeader("X-Token");
    }
}
