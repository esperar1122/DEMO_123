<template>
  <div class="login-container">
    <el-card class="login-card">
      <h2>登录</h2>
      <el-tabs v-model="activeTab" class="login-tabs">
        <el-tab-pane label="用户登录" name="user">
          <el-form :model="userForm" :rules="userRules" ref="userFormRef">
            <el-form-item prop="username">
              <el-input v-model="userForm.username" placeholder="用户名" />
            </el-form-item>
            <el-form-item prop="password">
              <el-input v-model="userForm.password" type="password" placeholder="密码" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleUserLogin" :loading="loading" style="width: 100%">登录</el-button>
            </el-form-item>
          </el-form>
          <div class="footer">
            <span>还没有账号？</span>
            <router-link to="/register">立即注册</router-link>
          </div>
        </el-tab-pane>

        <el-tab-pane label="管理员登录" name="admin">
          <el-form :model="adminForm" :rules="adminRules" ref="adminFormRef">
            <el-form-item prop="username">
              <el-input v-model="adminForm.username" placeholder="管理员用户名" />
            </el-form-item>
            <el-form-item prop="password">
              <el-input v-model="adminForm.password" type="password" placeholder="管理员密码" />
            </el-form-item>
            <el-form-item>
              <el-button type="danger" @click="handleAdminLogin" :loading="loading" style="width: 100%">管理员登录</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { ElMessage } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()

const activeTab = ref('user')
const loading = ref(false)

const userFormRef = ref(null)
const userForm = reactive({
  username: '',
  password: ''
})
const userRules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

const adminFormRef = ref(null)
const adminForm = reactive({
  username: '',
  password: ''
})
const adminRules = {
  username: [{ required: true, message: '请输入管理员用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入管理员密码', trigger: 'blur' }]
}

const handleUserLogin = async () => {
  const valid = await userFormRef.value.validate().catch(() => false)
  if (!valid) return

  loading.value = true
  try {
    await userStore.login(userForm.username, userForm.password)
    if (userStore.isAdmin) {
      ElMessage.warning('请使用管理员入口登录')
      return
    }
    ElMessage.success('登录成功')
    router.push('/home')
  } catch (error) {
    ElMessage.error(error.message || '登录失败')
  } finally {
    loading.value = false
  }
}

const handleAdminLogin = async () => {
  const valid = await adminFormRef.value.validate().catch(() => false)
  if (!valid) return

  loading.value = true
  try {
    await userStore.login(adminForm.username, adminForm.password)
    if (!userStore.isAdmin) {
      ElMessage.warning('您不是管理员，无法从此入口登录')
      return
    }
    ElMessage.success('管理员登录成功')
    router.push('/admin')
  } catch (error) {
    ElMessage.error(error.message || '登录失败')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.login-card {
  width: 420px;
  padding: 20px;
}

h2 {
  text-align: center;
  margin-bottom: 20px;
  color: #333;
}

.login-tabs {
  margin-top: 10px;
}

.footer {
  text-align: center;
  margin-top: 20px;
  color: #666;
}

.footer a {
  color: #409eff;
  text-decoration: none;
  margin-left: 5px;
}
</style>