<template>
  <el-container class="page">
    <el-card class="card">
      <h2>音乐台系统登录</h2>
      <el-form :model="form" label-width="80px">
        <el-form-item label="用户名">
          <el-input v-model="form.username" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="form.password" type="password" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleLogin">登录</el-button>
          <el-button @click="handleRegister">注册</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </el-container>
</template>

<script setup lang="ts">
import { reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { login, register } from '../api'

const router = useRouter()
const form = reactive({
  username: '',
  password: ''
})

const handleLogin = async () => {
  const { data } = await login(form.username, form.password)
  if (data.success) {
    localStorage.setItem('token', data.data.token)
    localStorage.setItem('role', data.data.role)
    router.push('/songs')
  } else {
    ElMessage.error(data.message)
  }
}

const handleRegister = async () => {
  const { data } = await register(form.username, form.password)
  if (data.success) {
    localStorage.setItem('token', data.data.token)
    localStorage.setItem('role', data.data.role)
    router.push('/songs')
  } else {
    ElMessage.error(data.message)
  }
}
</script>

<style scoped>
.page {
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
}
.card {
  width: 360px;
}
</style>
