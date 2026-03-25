<template>
  <div class="orders-container">
    <el-tabs v-model="activeTab">
      <el-tab-pane label="我买到的" name="buyer">
        <div class="orders-list">
          <div v-for="order in buyerOrders" :key="order.id" class="order-card">
            <div class="order-header">
              <span>订单号: {{ order.id }}</span>
              <span class="status">{{ getOrderStatus(order.status) }}</span>
            </div>
            <div class="order-body">
              <p>商品ID: {{ order.itemId }}</p>
              <p>交易方式: {{ getTransactionType(order.transactionType) }}</p>
              <p class="price">总价: ¥{{ order.totalPrice }}</p>
              <p>创建时间: {{ formatTime(order.createdAt) }}</p>
            </div>
            <div class="order-actions">
              <el-button size="small" @click="handleCancel(order)" v-if="order.status === 0">取消订单</el-button>
              <el-button size="small" type="primary" @click="handleComplete(order)" v-if="order.status === 1">确认收货</el-button>
            </div>
          </div>
          <el-empty v-if="buyerOrders.length === 0" description="暂无订单" />
        </div>
      </el-tab-pane>

      <el-tab-pane label="我卖出的" name="seller">
        <div class="orders-list">
          <div v-for="order in sellerOrders" :key="order.id" class="order-card">
            <div class="order-header">
              <span>订单号: {{ order.id }}</span>
              <span class="status">{{ getOrderStatus(order.status) }}</span>
            </div>
            <div class="order-body">
              <p>商品ID: {{ order.itemId }}</p>
              <p>交易方式: {{ getTransactionType(order.transactionType) }}</p>
              <p class="price">总价: ¥{{ order.totalPrice }}</p>
              <p>创建时间: {{ formatTime(order.createdAt) }}</p>
            </div>
            <div class="order-actions">
              <el-button size="small" type="success" @click="handleConfirm(order)" v-if="order.status === 0">确认订单</el-button>
              <el-button size="small" type="danger" @click="handleReject(order)" v-if="order.status === 0">拒绝订单</el-button>
            </div>
          </div>
          <el-empty v-if="sellerOrders.length === 0" description="暂无订单" />
        </div>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { orderAPI } from '@/api'
import { ElMessage, ElMessageBox } from 'element-plus'

const activeTab = ref('buyer')
const buyerOrders = ref([])
const sellerOrders = ref([])

const loadOrders = async () => {
  try {
    const [buyerRes, sellerRes] = await Promise.all([
      orderAPI.getBuyerOrders(),
      orderAPI.getSellerOrders()
    ])
    if (buyerRes.code === 200) buyerOrders.value = buyerRes.data
    if (sellerRes.code === 200) sellerOrders.value = sellerRes.data
  } catch (error) {
    ElMessage.error('加载订单失败')
  }
}

const getOrderStatus = (status) => {
  const texts = { 0: '待确认', 1: '待交易', 2: '已完成', 3: '已取消' }
  return texts[status] || '未知'
}

const getTransactionType = (type) => {
  const texts = { 0: '校园面交', 1: '自提', 2: '送货上门' }
  return texts[type] || '未知'
}

const formatTime = (time) => {
  if (!time) return ''
  const date = new Date(time)
  return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')} ${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')}`
}

const handleConfirm = async (order) => {
  try {
    await orderAPI.confirmOrder(order.id)
    ElMessage.success('确认成功')
    loadOrders()
  } catch (error) {
    ElMessage.error(error.message || '操作失败')
  }
}

const handleReject = async (order) => {
  try {
    await ElMessageBox.confirm('确定要拒绝该订单吗？', '提示', { type: 'warning' })
    await orderAPI.rejectOrder(order.id)
    ElMessage.success('已拒绝')
    loadOrders()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error(error.message || '操作失败')
    }
  }
}

const handleCancel = async (order) => {
  try {
    await ElMessageBox.confirm('确定要取消该订单吗？', '提示', { type: 'warning' })
    await orderAPI.cancelOrder(order.id)
    ElMessage.success('已取消')
    loadOrders()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error(error.message || '操作失败')
    }
  }
}

const handleComplete = async (order) => {
  try {
    await ElMessageBox.confirm('确认已收到商品吗？', '提示', { type: 'info' })
    await orderAPI.completeOrder(order.id)
    ElMessage.success('确认成功')
    loadOrders()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error(error.message || '操作失败')
    }
  }
}

onMounted(() => {
  loadOrders()
})
</script>

<style scoped>
.orders-container {
  min-height: 100vh;
  background: #f5f5f5;
  padding: 20px;
}

.orders-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.order-card {
  background: #fff;
  border-radius: 8px;
  padding: 20px;
}

.order-header {
  display: flex;
  justify-content: space-between;
  padding-bottom: 15px;
  border-bottom: 1px solid #eee;
  margin-bottom: 15px;
}

.status {
  color: #409eff;
  font-weight: bold;
}

.order-body p {
  margin: 8px 0;
  color: #666;
}

.price {
  color: #f56c6c;
  font-size: 18px;
  font-weight: bold;
}

.order-actions {
  margin-top: 15px;
  display: flex;
  gap: 10px;
}
</style>