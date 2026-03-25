<template>
  <div class="publish-container">
    <el-card>
      <h2>发布商品</h2>
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="商品标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入商品标题(1-20字)" maxlength="20" show-word-limit />
        </el-form-item>
        
        <el-form-item label="商品描述" prop="description">
          <el-input 
            v-model="form.description" 
            type="textarea" 
            placeholder="请输入商品描述(1-1000字)"
            maxlength="1000" 
            show-word-limit
            :rows="6"
          />
        </el-form-item>
        
        <el-form-item label="商品价格" prop="price">
          <el-input-number v-model="form.price" :min="0.01" :precision="2" />
        </el-form-item>
        
        <el-form-item label="商品图片">
          <el-upload
            v-model:file-list="fileList"
            action="/api/upload"
            list-type="picture-card"
            :auto-upload="false"
            :on-change="handleFileChange"
            :on-remove="handleRemove"
            :limit="5"
          >
            <el-icon><Plus /></el-icon>
          </el-upload>
          <div class="upload-tip">最多上传5张图片，单张不超过4MB（选填）</div>
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" @click="handleSubmit" :loading="loading">发布商品</el-button>
          <el-button @click="$router.back()">取消</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { itemAPI } from '@/api'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'

const router = useRouter()

const formRef = ref(null)
const loading = ref(false)
const fileList = ref([])

const form = reactive({
  title: '',
  description: '',
  price: null,
  images: []
})

const rules = {
  title: [
    { required: true, message: '请输入商品标题', trigger: 'blur' },
    { min: 1, max: 20, message: '标题需1-20字', trigger: 'blur' }
  ],
  description: [
    { required: true, message: '请输入商品描述', trigger: 'blur' },
    { min: 1, max: 1000, message: '描述需1-1000字', trigger: 'blur' }
  ],
  price: [
    { required: true, message: '请输入商品价格', trigger: 'blur' }
  ]
}

const handleFileChange = (uploadFile) => {
  const rawFile = uploadFile.raw
  if (!rawFile) return
  
  if (rawFile.size > 4 * 1024 * 1024) {
    ElMessage.warning('图片不能超过4MB')
    fileList.value = fileList.value.filter(f => f.uid !== uploadFile.uid)
    return
  }

  const reader = new FileReader()
  reader.onload = (e) => {
    form.images.push(e.target.result)
  }
  reader.readAsDataURL(rawFile)
}

const handleRemove = (file) => {
  const index = fileList.value.findIndex(f => f.uid === file.uid)
  if (index > -1) {
    form.images.splice(index, 1)
  }
}

const handleSubmit = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  loading.value = true
  try {
    const res = await itemAPI.createItem({
      title: form.title,
      description: form.description,
      price: form.price,
      images: form.images
    })
    if (res.code === 200) {
      ElMessage.success('发布成功')
      router.push('/my-items')
    }
  } catch (error) {
    ElMessage.error(error.message || '发布失败')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.publish-container {
  min-height: 100vh;
  background: #f5f5f5;
  padding: 40px;
}

.publish-container .el-card {
  max-width: 800px;
  margin: 0 auto;
}

h2 {
  margin-bottom: 30px;
}

.upload-tip {
  color: #909399;
  font-size: 12px;
  margin-top: 5px;
}
</style>