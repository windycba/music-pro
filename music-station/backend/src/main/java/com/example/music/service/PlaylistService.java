package com.example.music.service;

import com.example.music.dto.PlaylistSongDto;
import com.example.music.entity.Playlist;
import com.example.music.entity.PlaylistSong;
import com.example.music.entity.PlaylistSongId;
import com.example.music.entity.Song;
import com.example.music.entity.User;
import com.example.music.repository.PlaylistRepository;
import com.example.music.repository.PlaylistSongRepository;
import com.example.music.repository.SongRepository;
import com.example.music.repository.UserRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class PlaylistService {
    private final PlaylistRepository playlistRepository;
    private final PlaylistSongRepository playlistSongRepository;
    private final SongRepository songRepository;
    private final UserRepository userRepository;

    public PlaylistService(PlaylistRepository playlistRepository,
                           PlaylistSongRepository playlistSongRepository,
                           SongRepository songRepository,
                           UserRepository userRepository) {
        this.playlistRepository = playlistRepository;
        this.playlistSongRepository = playlistSongRepository;
        this.songRepository = songRepository;
        this.userRepository = userRepository;
    }

    public Playlist createPlaylist(String username, String name) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("用户不存在"));
        Playlist playlist = new Playlist();
        playlist.setName(name);
        playlist.setUser(user);
        return playlistRepository.save(playlist);
    }

    public List<Playlist> getPlaylists(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("用户不存在"));
        return playlistRepository.findByUser(user);
    }

    public List<PlaylistSongDto> getPlaylistSongs(Long playlistId) {
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new IllegalArgumentException("歌单不存在"));
        return playlistSongRepository.findByPlaylist(playlist).stream()
                .map(ps -> new PlaylistSongDto(
                        ps.getSong().getId(),
                        ps.getSong().getTitle(),
                        ps.getSong().getArtist(),
                        ps.getSong().getAlbum()))
                .collect(Collectors.toList());
    }

    public void addSong(Long playlistId, Long songId) {
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new IllegalArgumentException("歌单不存在"));
        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new IllegalArgumentException("歌曲不存在"));
        PlaylistSongId id = new PlaylistSongId(playlist.getId(), song.getId());
        if (playlistSongRepository.existsById(id)) {
            return;
        }
        PlaylistSong playlistSong = new PlaylistSong(playlist, song);
        playlistSongRepository.save(playlistSong);
    }

    public void removeSong(Long playlistId, Long songId) {
        PlaylistSongId id = new PlaylistSongId(playlistId, songId);
        playlistSongRepository.deleteById(id);
    }
}
