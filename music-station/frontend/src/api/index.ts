import http from './http'

export interface Result<T> {
  success: boolean
  message: string
  data: T
}

export interface AuthResponse {
  token: string
  role: string
}

export interface SongDto {
  id: number
  title: string
  artist: string
  album: string
  duration: number
  enabled: boolean
}

export interface PlaylistDto {
  id: number
  name: string
}

export interface PlaylistSongDto {
  songId: number
  title: string
  artist: string
  album: string
}

export const login = (username: string, password: string) =>
  http.post<Result<AuthResponse>>('/api/auth/login', { username, password })

export const register = (username: string, password: string) =>
  http.post<Result<AuthResponse>>('/api/auth/register', { username, password })

export const fetchSongs = () => http.get<Result<SongDto[]>>('/api/songs')

export const streamUrl = (id: number) => `/api/songs/${id}/stream`

export const fetchPlaylists = () => http.get<Result<PlaylistDto[]>>('/api/playlists')

export const createPlaylist = (name: string) =>
  http.post<Result<PlaylistDto>>('/api/playlists', { name })

export const fetchPlaylistSongs = (playlistId: number) =>
  http.get<Result<PlaylistSongDto[]>>(`/api/playlists/${playlistId}/songs`)

export const addPlaylistSong = (playlistId: number, songId: number) =>
  http.post<Result<void>>(`/api/playlists/${playlistId}/songs`, { songId })

export const removePlaylistSong = (playlistId: number, songId: number) =>
  http.delete<Result<void>>(`/api/playlists/${playlistId}/songs/${songId}`)

export const adminFetchSongs = () => http.get<Result<SongDto[]>>('/api/admin/songs')

export const adminScanSongs = () => http.post<Result<SongDto[]>>('/api/admin/songs/scan')

export const adminToggleSong = (id: number, enabled: boolean) =>
  http.put<Result<SongDto>>(`/api/admin/songs/${id}/enable?enabled=${enabled}`)
