<template>
  <el-container class="page">
    <el-header>
      <el-space>
        <el-button type="primary" @click="goSongs">返回歌曲</el-button>
      </el-space>
    </el-header>
    <el-main>
      <el-card>
        <h2>正在播放</h2>
        <div v-if="playerState.current" class="info">
          <p class="title">{{ playerState.current.title }}</p>
          <p class="meta">{{ playerState.current.artist }} - {{ playerState.current.album }}</p>
          <el-alert type="info" show-icon title="播放由底部悬浮播放器控制" />
        </div>
        <div v-else class="empty">暂无播放歌曲，请在歌曲列表中选择。</div>
      </el-card>
    </el-main>
  </el-container>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'
import { playerState } from '../store/player'

const router = useRouter()

const goSongs = () => router.push('/songs')
</script>

<style scoped>
.page {
  min-height: 100vh;
}
.title {
  font-size: 20px;
  margin-bottom: 4px;
}
.meta {
  color: #666;
  margin-bottom: 12px;
}
.empty {
  color: #999;
}
</style>
