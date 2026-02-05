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
          <audio
            ref="audioRef"
            controls
            autoplay
            :src="streamUrl(playerState.current.id)"
            @play="setPlaying(true)"
            @pause="setPlaying(false)"
          />
        </div>
        <div v-else class="empty">暂无播放歌曲，请在歌曲列表中选择。</div>
      </el-card>
    </el-main>
  </el-container>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import { useRouter } from 'vue-router'
import { streamUrl } from '../api'
import { playerState, setPlaying } from '../store/player'

const router = useRouter()
const audioRef = ref<HTMLAudioElement | null>(null)

const goSongs = () => router.push('/songs')

watch(
  () => playerState.current,
  () => {
    if (audioRef.value) {
      audioRef.value.load()
      audioRef.value.play()
    }
  }
)
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
