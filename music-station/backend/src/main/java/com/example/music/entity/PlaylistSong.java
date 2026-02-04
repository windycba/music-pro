package com.example.music.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "playlist_song")
public class PlaylistSong {
    @EmbeddedId
    private PlaylistSongId id;

    @ManyToOne(optional = false)
    @MapsId("playlistId")
    @JoinColumn(name = "playlist_id")
    private Playlist playlist;

    @ManyToOne(optional = false)
    @MapsId("songId")
    @JoinColumn(name = "song_id")
    private Song song;

    public PlaylistSong() {
    }

    public PlaylistSong(Playlist playlist, Song song) {
        this.playlist = playlist;
        this.song = song;
        this.id = new PlaylistSongId(playlist.getId(), song.getId());
    }

    public PlaylistSongId getId() {
        return id;
    }

    public Playlist getPlaylist() {
        return playlist;
    }

    public Song getSong() {
        return song;
    }
}
