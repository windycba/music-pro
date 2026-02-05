<template>
  <el-container class="page">
    <el-header>
      <el-space>
        <el-button type="primary" @click="goSongs">返回歌曲</el-button>
      </el-space>
    </el-header>
    <el-main>
      <el-row :gutter="20">
        <el-col :span="8">
          <el-card>
            <h3>创建歌单</h3>
            <el-input v-model="newName" placeholder="歌单名称" />
            <el-button type="primary" class="mt" @click="create">创建</el-button>
          </el-card>
          <el-card class="mt">
            <h3>我的歌单</h3>
            <el-menu @select="selectPlaylist">
              <el-menu-item v-for="p in playlists" :key="p.id" :index="String(p.id)">
                {{ p.name }}
              </el-menu-item>
            </el-menu>
          </el-card>
        </el-col>
        <el-col :span="16">
          <el-card>
            <h3>歌单歌曲</h3>
            <el-table :data="playlistSongs" style="width: 100%">
              <el-table-column prop="title" label="歌名" />
              <el-table-column prop="artist" label="歌手" />
              <el-table-column prop="album" label="专辑" />
              <el-table-column label="操作">
                <template #default="scope">
                  <el-button type="primary" @click="playSong(scope.row)">播放</el-button>
                  <el-button type="danger" @click="removeSong(scope.row.songId)">移除</el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-card>
          <el-card class="mt">
            <h3>添加歌曲</h3>
            <el-select v-model="selectedSong" placeholder="选择歌曲">
              <el-option v-for="song in songs" :key="song.id" :label="song.title" :value="song.id" />
            </el-select>
            <el-button type="primary" class="ml" @click="addSong">添加</el-button>
          </el-card>
        </el-col>
      </el-row>
    </el-main>
  </el-container>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import {
  addPlaylistSong,
  createPlaylist,
  fetchPlaylistSongs,
  fetchPlaylists,
  fetchSongs,
  PlaylistDto,
  PlaylistSongDto,
  SongDto,
  removePlaylistSong
} from '../api'
import { setCurrentSong } from '../store/player'

const router = useRouter()
const playlists = ref<PlaylistDto[]>([])
const playlistSongs = ref<PlaylistSongDto[]>([])
const songs = ref<SongDto[]>([])
const selectedPlaylist = ref<number | null>(null)
const selectedSong = ref<number | null>(null)
const newName = ref('')

const loadPlaylists = async () => {
  const { data } = await fetchPlaylists()
  if (data.success) {
    playlists.value = data.data
    if (data.data.length && selectedPlaylist.value === null) {
      selectedPlaylist.value = data.data[0].id
      loadPlaylistSongs()
    }
  }
}

const loadPlaylistSongs = async () => {
  if (!selectedPlaylist.value) return
  const { data } = await fetchPlaylistSongs(selectedPlaylist.value)
  if (data.success) {
    playlistSongs.value = data.data
  }
}

const loadSongs = async () => {
  const { data } = await fetchSongs()
  if (data.success) {
    songs.value = data.data
  }
}

const create = async () => {
  if (!newName.value) return
  const { data } = await createPlaylist(newName.value)
  if (data.success) {
    newName.value = ''
    await loadPlaylists()
  }
}

const selectPlaylist = (index: string) => {
  selectedPlaylist.value = Number(index)
  loadPlaylistSongs()
}

const addSong = async () => {
  if (!selectedPlaylist.value || !selectedSong.value) return
  const { data } = await addPlaylistSong(selectedPlaylist.value, selectedSong.value)
  if (data.success) {
    await loadPlaylistSongs()
  }
}

const removeSong = async (songId: number) => {
  if (!selectedPlaylist.value) return
  const { data } = await removePlaylistSong(selectedPlaylist.value, songId)
  if (data.success) {
    await loadPlaylistSongs()
  }
}

const playSong = (song: PlaylistSongDto) => {
  setCurrentSong({
    id: song.songId,
    title: song.title,
    artist: song.artist,
    album: song.album,
    duration: 0,
    enabled: true
  })
  router.push('/player')
}

const goSongs = () => router.push('/songs')

onMounted(() => {
  loadPlaylists()
  loadSongs()
})
</script>

<style scoped>
.mt {
  margin-top: 12px;
}
.ml {
  margin-left: 8px;
}
</style>
