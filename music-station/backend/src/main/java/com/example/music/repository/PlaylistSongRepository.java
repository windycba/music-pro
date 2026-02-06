package com.example.music.repository;

import com.example.music.entity.Playlist;
import com.example.music.entity.PlaylistSong;
import com.example.music.entity.PlaylistSongId;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaylistSongRepository extends JpaRepository<PlaylistSong, PlaylistSongId> {
    List<PlaylistSong> findByPlaylist(Playlist playlist);
}
