package com.example.music.repository;

import com.example.music.entity.Song;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SongRepository extends JpaRepository<Song, Long> {
    Optional<Song> findByFilePath(String filePath);
}
