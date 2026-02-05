import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

export default defineConfig({
  plugins: [vue()],
  server: {
    port: 5173,
    proxy: {
      // 将所有以 /api 开头的请求代理到后端
      '/api': {
        target: 'http://localhost:8080', // 你的后端地址（“后台地址”）
        changeOrigin: true,              // 改变 origin，适用于虚拟主机
      },
      // 也可以代理其他路径，比如 /auth、/upload 等
      '/upload': {
        target: 'http://192.168.1.100:9000',
        changeOrigin: true
      }
    }

  }
})
