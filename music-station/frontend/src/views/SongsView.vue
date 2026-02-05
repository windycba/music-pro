<template>
  <el-container class="page">
    <el-header>
      <el-space>
        <el-button type="primary" @click="goPlaylists">歌单管理</el-button>
        <el-button v-if="isAdmin" @click="goAdmin">管理员</el-button>
        <el-button type="danger" @click="logout">退出</el-button>
      </el-space>
    </el-header>
    <el-main>
      <el-table :data="songs" style="width: 100%">
        <el-table-column prop="title" label="歌名" />
        <el-table-column prop="artist" label="歌手" />
        <el-table-column prop="album" label="专辑" />
        <el-table-column label="播放">
          <template #default="scope">
            <el-button type="primary" @click="playSong(scope.row)">播放</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-main>
  </el-container>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { fetchSongs, SongDto } from '../api'
import { setCurrentSong } from '../store/player'

const router = useRouter()
const songs = ref<SongDto[]>([])
const isAdmin = localStorage.getItem('role') === 'ADMIN'

const loadSongs = async () => {
  const { data } = await fetchSongs()
  if (data.success) {
    songs.value = data.data
  }
}

const goPlaylists = () => router.push('/playlists')
const goAdmin = () => router.push('/admin')
const playSong = (song: SongDto) => {
  setCurrentSong(song)
  router.push('/player')
}
const logout = () => {
  localStorage.clear()
  router.push('/login')
}

onMounted(loadSongs)
</script>
