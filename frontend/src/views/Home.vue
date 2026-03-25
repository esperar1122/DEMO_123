<template>
  <div class="home-container">
    <div class="header">
      <h1>校园二手交易平台</h1>
      <div class="header-right">
        <el-input v-model="keyword" placeholder="搜索商品" class="search-input" @keyup.enter="handleSearch">
          <template #append>
            <el-button :icon="Search" @click="handleSearch" />
          </template>
        </el-input>
        <el-button type="primary" @click="$router.push('/publish')">发布商品</el-button>
        <el-dropdown @command="handleCommand">
          <span class="user-info">
            <span>{{ userStore.username || '用户' }}</span>
            <span class="credit-score">信用分: {{ userStore.creditScore || 80 }}</span>
          </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="my-items">我的商品</el-dropdown-item>
              <el-dropdown-item command="orders">我的订单</el-dropdown-item>
              <el-dropdown-item command="profile">个人中心</el-dropdown-item>
              <el-dropdown-item command="admin" v-if="isAdmin">管理后台</el-dropdown-item>
              <el-dropdown-item command="logout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </div>

    <div class="content">
      <div class="items-grid">
        <div v-for="item in items" :key="item.id" class="item-card" @click="$router.push(`/item/${item.id}`)">
          <div class="item-image">
            <img :src="item.images?.[0] || '/placeholder.png'" alt="" />
          </div>
          <div class="item-info">
            <h3>{{ item.title }}</h3>
            <p class="price">¥{{ item.price }}</p>
            <p class="time">{{ formatTime(item.createdAt) }}</p>
          </div>
        </div>
      </div>
      
      <el-empty v-if="items.length === 0 && !loading" description="暂无商品" />
      
      <div class="pagination">
        <el-pagination
          :current-page="page"
          :page-size="pageSize"
          :total="total"
          layout="total, prev, pager, next"
          @current-change="loadItems"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { itemAPI } from '@/api'
import { Search } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()

const items = ref([])
const page = ref(1)
const pageSize = ref(12)
const total = ref(0)
const loading = ref(false)
const keyword = ref('')

const isAdmin = computed(() => userStore.isAdmin)

const loadItems = async () => {
  loading.value = true
  try {
    const res = await itemAPI.getItems({ page: page.value, size: pageSize.value, keyword: keyword.value })
    if (res.code === 200) {
      items.value = res.data.records
      total.value = res.data.total
    }
  } catch (error) {
    ElMessage.error('加载失败')
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  page.value = 1
  loadItems()
}

const handleCommand = (command) => {
  if (command === 'logout') {
    userStore.logout()
    router.push('/login')
  } else if (command === 'admin') {
    router.push('/admin')
  } else {
    router.push(`/${command}`)
  }
}

const formatTime = (time) => {
  if (!time) return ''
  const date = new Date(time)
  return `${date.getMonth() + 1}-${date.getDate()}`
}

onMounted(() => {
  loadItems()
})
</script>

<style scoped>
.home-container {
  min-height: 100vh;
  background: #f5f5f5;
}

.header {
  background: #fff;
  padding: 20px 40px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.header h1 {
  font-size: 24px;
  color: #333;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 15px;
}

.search-input {
  width: 300px;
}

.user-info {
  display: flex;
  flex-direction: column;
  align-items: center;
  cursor: pointer;
}

.credit-score {
  font-size: 12px;
  color: #909399;
}

.content {
  padding: 20px 40px;
}

.items-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
}

.item-card {
  background: #fff;
  border-radius: 8px;
  overflow: hidden;
  cursor: pointer;
  transition: transform 0.2s, box-shadow 0.2s;
}

.item-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.item-image {
  width: 100%;
  height: 200px;
  overflow: hidden;
  background: #eee;
}

.item-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.item-info {
  padding: 15px;
}

.item-info h3 {
  font-size: 16px;
  margin-bottom: 10px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.price {
  color: #f56c6c;
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 5px;
}

.time {
  color: #909399;
  font-size: 12px;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}
</style>