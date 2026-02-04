package com.example.music.dto;

import com.example.music.entity.Playlist;

public class PlaylistDto {
    private Long id;
    private String name;

    public static PlaylistDto from(Playlist playlist) {
        PlaylistDto dto = new PlaylistDto();
        dto.id = playlist.getId();
        dto.name = playlist.getName();
        return dto;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
