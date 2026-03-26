<template>
  <div class="review-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>订单评价</span>
        </div>
      </template>

      <div v-if="order" class="review-content">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="订单号">{{ order.id }}</el-descriptions-item>
          <el-descriptions-item label="商品">{{ item?.title }}</el-descriptions-item>
          <el-descriptions-item label="价格">¥{{ order.totalPrice }}</el-descriptions-item>
          <el-descriptions-item label="交易方式">{{ getTransactionType(order.transactionType) }}</el-descriptions-item>
        </el-descriptions>

        <el-form :model="form" label-width="80px" style="margin-top: 20px">
          <el-form-item label="评分">
            <el-rate v-model="form.rating" :max="5" show-text :texts="['极差', '差', '一般', '好', '非常好']" />
          </el-form-item>
          
          <el-form-item label="评价内容">
            <el-input v-model="form.content" type="textarea" :rows="4" placeholder="请输入评价内容（选填）" maxlength="200" show-word-limit />
          </el-form-item>
          
          <el-form-item>
            <el-button type="primary" @click="handleSubmit" :loading="submitting">提交评价</el-button>
            <el-button @click="$router.back()">取消</el-button>
          </el-form-item>
        </el-form>
      </div>

      <el-empty v-else description="订单不存在" />
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { orderAPI, itemAPI, reviewAPI } from '@/api'
import { ElMessage } from 'element-plus'

const route = useRoute()
const router = useRouter()

const order = ref(null)
const item = ref(null)
const submitting = ref(false)

const form = reactive({
  rating: 5,
  content: ''
})

const getTransactionType = (type) => {
  const texts = { 0: '校园面交', 1: '自提', 2: '送货上门' }
  return texts[type] || '未知'
}

const loadData = async () => {
  try {
    const orderId = route.query.orderId
    console.log('Loading order, orderId:', orderId)
    
    if (!orderId) {
      ElMessage.error('订单不存在')
      router.push('/orders')
      return
    }
    
    const res = await orderAPI.getOrder(orderId)
    console.log('Order API response:', res)
    
    if (res.code === 200 && res.data && res.data.order) {
      order.value = res.data.order
      console.log('Order loaded:', order.value)
      
      const itemRes = await itemAPI.getItem(order.value.itemId)
      console.log('Item API response:', itemRes)
      if (itemRes.code === 200) {
        item.value = itemRes.data.item
      }
    } else {
      ElMessage.error('订单不存在')
      router.push('/orders')
    }
  } catch (error) {
    console.error('Load error:', error)
    ElMessage.error('加载失败')
    router.push('/orders')
  }
}

const handleSubmit = async () => {
  if (form.rating < 1) {
    ElMessage.warning('请选择评分')
    return
  }
  
  submitting.value = true
  try {
    const res = await reviewAPI.createReview({
      orderId: order.value.id,
      rating: form.rating,
      content: form.content
    })
    
    if (res.code === 200) {
      ElMessage.success('评价成功')
      router.push('/orders')
    }
  } catch (error) {
    ElMessage.error(error.message || '评价失败')
  } finally {
    submitting.value = false
  }
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.review-container {
  min-height: 100vh;
  background: #f5f5f5;
  padding: 40px;
}

.review-container .el-card {
  max-width: 800px;
  margin: 0 auto;
}

.card-header {
  font-size: 18px;
  font-weight: bold;
}

.review-content {
  padding: 10px 0;
}
</style>