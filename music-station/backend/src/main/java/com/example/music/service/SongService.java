package com.example.music.service;

import com.example.music.entity.Song;
import com.example.music.repository.SongRepository;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SongService {
    private final SongRepository songRepository;
    private final String scanPath;

    public SongService(SongRepository songRepository, @Value("${music.scan-path}") String scanPath) {
        this.songRepository = songRepository;
        this.scanPath = scanPath;
    }

    public List<Song> findAll() {
        return songRepository.findAll();
    }

    public List<Song> scanAndImport() throws IOException {
        Path basePath = Path.of(scanPath);
        if (!Files.exists(basePath)) {
            return List.of();
        }
        try (var stream = Files.walk(basePath)) {
            List<Path> files = stream.filter(Files::isRegularFile)
                    .filter(this::isAudioFile)
                    .collect(Collectors.toList());
            for (Path file : files) {
                String fullPath = file.toAbsolutePath().toString();
                if (songRepository.findByFilePath(fullPath).isPresent()) {
                    continue;
                }
                String fileName = file.getFileName().toString();
                String baseName = fileName.substring(0, fileName.lastIndexOf('.'));
                String artist = "未知";
                String title = baseName;
                if (baseName.contains("-")) {
                    String[] parts = baseName.split("-", 2);
                    artist = parts[0].trim();
                    title = parts[1].trim();
                }
                Song song = new Song();
                song.setArtist(artist);
                song.setTitle(title);
                song.setAlbum("默认专辑");
                song.setFilePath(fullPath);
                song.setDuration(0);
                song.setEnabled(true);
                songRepository.save(song);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return songRepository.findAll();
    }

    public Song toggleEnabled(Long id, boolean enabled) {
        Song song = songRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("歌曲不存在"));
        song.setEnabled(enabled);
        return songRepository.save(song);
    }

    private boolean isAudioFile(Path path) {
        String name = path.getFileName().toString().toLowerCase(Locale.ROOT);
        return name.endsWith(".mp3") || name.endsWith(".flac") || name.endsWith(".wav") || name.endsWith(".ogg");
    }
}
