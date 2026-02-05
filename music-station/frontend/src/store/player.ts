import { reactive } from 'vue'
import type { SongDto } from '../api'

interface PlayerState {
  current: SongDto | null
  playing: boolean
}

export const playerState = reactive<PlayerState>({
  current: null,
  playing: false
})

export const setCurrentSong = (song: SongDto) => {
  playerState.current = song
  playerState.playing = true
}

export const setPlaying = (playing: boolean) => {
  playerState.playing = playing
}
