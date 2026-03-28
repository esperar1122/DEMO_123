# 轻量化校园二手交易平台 - 前端规格说明文档 (V1.0.0)

## 1. 技术栈选型
依据 PRD V1.3.0 要求，前端核心技术栈如下：
- **框架**: Vue 3 (Composition API)
- **构建工具**: Vite
- **UI 组件库**: Element Plus
- **状态管理**: Pinia
- **路由管理**: Vue Router
- **网络请求**: Axios
- **样式处理**: SCSS (推荐)
- **代码规范**: ESLint + Prettier

---

## 2. 项目架构与目录规范
```text
src/
├── api/             # Axios 实例及各模块接口定义
├── assets/          # 静态资源 (图片、图标、全局样式)
├── components/      # 公共组件 (Button, Card, Upload 等)
├── hooks/           # 组合式函数 (useAuth, useMessage 等)
├── layouts/         # 布局组件 (DefaultLayout, AdminLayout)
├── router/          # 路由配置与权限守卫
├── store/           # Pinia 全局状态管理
├── utils/           # 工具函数 (格式化、校验等)
├── views/           # 页面级组件
│   ├── auth/        # 登录、注册
│   ├── item/        # 商品列表、详情、发布
│   ├── order/       # 订单管理
│   └── admin/       # 后台管理系统
├── App.vue          # 根组件
└── main.ts          # 入口文件
```

---

## 3. UI/UX 设计规范
### 3.1 色彩方案 (清新校园风)
- **主色 (Primary)**: `#42B983` (活力绿) 或 `#409EFF` (标准蓝)
- **辅助色 (Success)**: `#67C23A`
- **警告色 (Warning)**: `#E6A23C`
- **危险色 (Danger)**: `#F56C6C`
- **中性色**:
  - 主要文字: `#303133`
  - 常规文字: `#606266`
  - 边框颜色: `#DCDFE6`

### 3.2 响应式设计
- **核心策略**: 兼容手机端浏览器。
- **断点**:
  - Mobile: < 768px (单列展示，隐藏部分非核心侧边栏)
  - Desktop: >= 768px (网格布局，展示完整分类侧边栏)

---

## 4. 路由与权限守卫
### 4.1 核心路由表
| 路径 | 组件 | 权限 | 说明 |
| :--- | :--- | :--- | :--- |
| `/` | Home.vue | 公开 | 首页商品流 |
| `/login` | Login.vue | 公开 | 登录页 |
| `/item/:id` | Detail.vue | 公开 | 商品详情 |
| `/publish` | Publish.vue | 需登录 | 发布商品 |
| `/my/orders` | Orders.vue | 需登录 | 订单中心 |
| `/admin/*` | Admin.vue | 需管理员 | 后台管理 |

### 4.2 路由守卫 (router.beforeEach)
- 检查 `localStorage` 中的 JWT。
- 若访问受限路由且无 Token，重定向至 `/login`。
- 若访问 `/admin` 且用户角色非 `ADMIN`，重定向至 `/403`。

---

## 5. 全局状态管理 (Pinia Store)
### 5.1 User Store
- **State**: `token`, `userInfo` (id, username, avatar, role, creditScore).
- **Actions**: `login`, `logout`, `fetchProfile`.
- **Persist**: 使用 `pinia-plugin-persistedstate` 持久化存储。

### 5.2 Notification Store
- **State**: `unreadCount`, `messages`.
- **Actions**: `fetchUnread`, `markAsRead`.

---

## 6. API 交互规范
### 6.1 Axios 实例配置
- **BaseURL**: `/api` (通过 Vite Proxy 转发)
- **Timeout**: 5000ms

### 6.2 请求拦截器
- 自动注入 Header: `Authorization: Bearer <token>`。

### 6.3 响应拦截器
- **200**: 返回 `response.data`。
- **401**: 清除 Token，跳转至 `/login` 并提示“登录过期”。
- **403**: 提示“无权访问”。
- **500**: 提示“服务器异常”。

---

## 7. 核心组件规范
### 7.1 ItemCard (商品卡片)
- 包含：图片预览、标题(20字内)、价格、卖家信用等级标签。
- 交互：悬停阴影效果，点击跳转详情。

### 7.2 ImageUpload (图片上传)
- 限制：最多5张，单张 < 4MB。
- 功能：前端预览、压缩、删除。

### 7.3 ChatBox (私信组件)
- 模式：WebSocket 实时接收消息。
- 交互：消息气泡、时间戳显示、滚动至底部。
