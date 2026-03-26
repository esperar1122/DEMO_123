import axios from 'axios'

const api = axios.create({
  baseURL: '/api',
  timeout: 10000
})

api.interceptors.request.use(config => {
  const token = localStorage.getItem('token')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

api.interceptors.response.use(
  response => response.data,
  error => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token')
      localStorage.removeItem('user')
      window.location.href = '/login'
    }
    return Promise.reject(error)
  }
)

export const authAPI = {
  register: (data) => api.post('/auth/register', data),
  login: (data) => api.post('/auth/login', data)
}

export const itemAPI = {
  getItems: (params) => api.get('/items', { params }),
  getItem: (id) => api.get(`/items/${id}`),
  createItem: (data) => api.post('/items', data),
  updateItem: (id, data) => api.put(`/items/${id}`, data),
  offlineItem: (id) => api.put(`/items/${id}/offline`),
  getMyItems: (params) => api.get('/items/my', { params })
}

export const userAPI = {
  getUser: (id) => api.get(`/users/${id}`)
}

export const orderAPI = {
  createOrder: (data) => api.post('/orders', data),
  getOrder: (id) => api.get(`/orders/${id}`),
  getBuyerOrders: () => api.get('/orders/buyer'),
  getSellerOrders: () => api.get('/orders/seller'),
  confirmOrder: (id) => api.put(`/orders/${id}/confirm`),
  rejectOrder: (id) => api.put(`/orders/${id}/reject`),
  cancelOrder: (id) => api.put(`/orders/${id}/cancel`),
  completeOrder: (id) => api.put(`/orders/${id}/complete`)
}

export const reviewAPI = {
  createReview: (params) => api.post('/reviews', null, { params }),
  getUserReviews: (userId) => api.get(`/reviews/user/${userId}`),
  getCompletableOrders: () => api.get('/reviews/completable')
}

export const adminAPI = {
  getUsers: (params) => api.get('/admin/users', { params }),
  banUser: (id) => api.put(`/admin/users/${id}/ban`),
  unbanUser: (id) => api.put(`/admin/users/${id}/unban`),
  getDashboard: () => api.get('/admin/dashboard')
}

export const messageAPI = {
  createMessage: (data) => api.post('/messages', data),
  getMessagesByItem: (itemId) => api.get(`/messages/item/${itemId}`)
}

export const notificationAPI = {
  getNotifications: () => api.get('/notifications'),
  markAsRead: (id) => api.put(`/notifications/${id}/read`),
  markAllAsRead: () => api.put('/notifications/read-all'),
  getUnreadCount: () => api.get('/notifications/unread-count'),
  deleteNotification: (id) => api.delete(`/notifications/${id}`)
}

export default api