<template>
  <router-view />
  <div v-if="playerState.current" class="mini-player">
    <div class="info" @click="goPlayer">
      <strong>{{ playerState.current?.title }}</strong>
      <span class="meta">{{ playerState.current?.artist }}</span>
    </div>
    <div class="controls">
      <el-button size="small" @click="togglePlay">
        {{ playerState.playing ? '暂停' : '播放' }}
      </el-button>
    </div>
    <audio
      ref="audioRef"
      class="audio"
      controls
      :src="currentSrc"
      @play="setPlaying(true)"
      @pause="setPlaying(false)"
    />
  </div>
</template>

<script setup lang="ts">
import { computed, ref, watch } from 'vue'
import { useRouter } from 'vue-router'
import { streamUrl } from './api'
import { playerState, setPlaying } from './store/player'

const router = useRouter()
const audioRef = ref<HTMLAudioElement | null>(null)

const currentSrc = computed(() =>
  playerState.current ? streamUrl(playerState.current.id) : ''
)

const goPlayer = () => {
  router.push('/player')
}

const togglePlay = () => {
  if (!audioRef.value) return
  if (playerState.playing) {
    audioRef.value.pause()
  } else {
    audioRef.value.play()
  }
}

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
.mini-player {
  position: fixed;
  left: 16px;
  right: 16px;
  bottom: 16px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  padding: 12px 16px;
  background: #ffffff;
  border-radius: 12px;
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.12);
}
.info {
  display: flex;
  flex-direction: column;
  cursor: pointer;
}
.meta {
  color: #888;
  font-size: 12px;
}
.controls {
  display: flex;
  align-items: center;
}
.audio {
  width: 600px;
}
</style>
