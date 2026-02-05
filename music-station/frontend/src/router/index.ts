import { createRouter, createWebHistory } from 'vue-router'
import LoginView from '../views/LoginView.vue'
import SongsView from '../views/SongsView.vue'
import PlaylistsView from '../views/PlaylistsView.vue'
import AdminView from '../views/AdminView.vue'
import PlayerView from '../views/PlayerView.vue'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/', redirect: '/login' },
    { path: '/login', component: LoginView },
    { path: '/songs', component: SongsView },
    { path: '/playlists', component: PlaylistsView },
    { path: '/admin', component: AdminView },
    { path: '/player', component: PlayerView }
  ]
})

export default router
