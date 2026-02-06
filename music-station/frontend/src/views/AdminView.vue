<template>
  <el-container class="page">
    <el-header>
      <el-space>
        <el-button type="primary" @click="goSongs">返回歌曲</el-button>
        <el-button @click="scan">扫描本地音乐</el-button>
      </el-space>
    </el-header>
    <el-main>
      <el-table :data="songs" style="width: 100%">
        <el-table-column prop="title" label="歌名" />
        <el-table-column prop="artist" label="歌手" />
        <el-table-column prop="album" label="专辑" />
        <el-table-column prop="enabled" label="状态">
          <template #default="scope">
            <el-tag :type="scope.row.enabled ? 'success' : 'info'">
              {{ scope.row.enabled ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作">
          <template #default="scope">
            <el-button @click="toggle(scope.row)">
              {{ scope.row.enabled ? '禁用' : '启用' }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-main>
  </el-container>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { adminFetchSongs, adminScanSongs, adminToggleSong, SongDto } from '../api'

const router = useRouter()
const songs = ref<SongDto[]>([])

const loadSongs = async () => {
  const { data } = await adminFetchSongs()
  if (data.success) {
    songs.value = data.data
  }
}

const scan = async () => {
  const { data } = await adminScanSongs()
  if (data.success) {
    songs.value = data.data
  }
}

const toggle = async (song: SongDto) => {
  const { data } = await adminToggleSong(song.id, !song.enabled)
  if (data.success) {
    await loadSongs()
  }
}

const goSongs = () => router.push('/songs')

onMounted(loadSongs)
</script>
