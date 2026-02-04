package com.example.music.dto;

import com.example.music.entity.Song;

public class SongDto {
    private Long id;
    private String title;
    private String artist;
    private String album;
    private Integer duration;
    private Boolean enabled;

    public static SongDto from(Song song) {
        SongDto dto = new SongDto();
        dto.id = song.getId();
        dto.title = song.getTitle();
        dto.artist = song.getArtist();
        dto.album = song.getAlbum();
        dto.duration = song.getDuration();
        dto.enabled = song.getEnabled();
        return dto;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbum() {
        return album;
    }

    public Integer getDuration() {
        return duration;
    }

    public Boolean getEnabled() {
        return enabled;
    }
}
