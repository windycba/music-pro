package com.example.music.dto;

public class PlaylistSongDto {
    private Long songId;
    private String title;
    private String artist;
    private String album;

    public PlaylistSongDto(Long songId, String title, String artist, String album) {
        this.songId = songId;
        this.title = title;
        this.artist = artist;
        this.album = album;
    }

    public Long getSongId() {
        return songId;
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
}
