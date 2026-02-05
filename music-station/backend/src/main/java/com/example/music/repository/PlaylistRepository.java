package com.example.music.repository;

import com.example.music.entity.Playlist;
import com.example.music.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    List<Playlist> findByUser(User user);
}
