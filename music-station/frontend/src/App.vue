<template>
  <router-view />
  <div v-if="showMiniPlayer" class="mini-player" @click="goPlayer">
    <div class="info">
      <strong>{{ playerState.current?.title }}</strong>
      <span class="meta">{{ playerState.current?.artist }}</span>
    </div>
    <el-button size="small" type="primary">播放中</el-button>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { playerState } from './store/player'

const route = useRoute()
const router = useRouter()

const showMiniPlayer = computed(() => playerState.current && route.path !== '/player')

const goPlayer = () => {
  router.push('/player')
}
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
  cursor: pointer;
}
.info {
  display: flex;
  flex-direction: column;
}
.meta {
  color: #888;
  font-size: 12px;
}
</style>
