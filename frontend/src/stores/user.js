import { defineStore } from 'pinia'
import { authAPI } from '@/api'

export const useUserStore = defineStore('user', {
  state: () => ({
    token: localStorage.getItem('token') || '',
    user: JSON.parse(localStorage.getItem('user') || 'null'),
    isLoggedIn: !!localStorage.getItem('token')
  }),
  getters: {
    userId: (state) => state.user?.userId,
    username: (state) => state.user?.username,
    creditScore: (state) => state.user?.creditScore,
    role: (state) => state.user?.role,
    isAdmin: (state) => state.user?.role === 1
  },
  actions: {
    async register(username, password) {
      const res = await authAPI.register({ username, password })
      if (res.code === 200) {
        this.setAuth(res.data)
        return true
      }
      throw new Error(res.message)
    },
    async login(username, password) {
      const res = await authAPI.login({ username, password })
      if (res.code === 200) {
        this.setAuth(res.data)
        return true
      }
      throw new Error(res.message)
    },
    setAuth(data) {
      this.token = data.token
      this.user = data
      this.isLoggedIn = true
      localStorage.setItem('token', data.token)
      localStorage.setItem('user', JSON.stringify(data))
    },
    logout() {
      this.token = ''
      this.user = null
      this.isLoggedIn = false
      localStorage.removeItem('token')
      localStorage.removeItem('user')
    }
  }
})