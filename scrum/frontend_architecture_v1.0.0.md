# 轻量化校园二手交易平台 - 前端架构设计文档 (V1.0.0)

## 1. 架构总览
基于 PRD V1.3.0 和前端规格说明书，本项目采用**分层架构设计**，确保 UI 层、业务逻辑层与数据访问层的解耦，提升系统的可维护性与扩展性。

### 1.1 分层模型
- **UI 层 (View Layer)**: Vue 3 组件，负责视图渲染与用户交互。
- **业务层 (Business/Service Layer)**: Hooks (Composition API) 与 Pinia Store，处理复杂业务逻辑。
- **数据层 (Data Layer)**: Axios 拦截器与 API 模块，负责与 Spring Boot 后端进行 RESTful 通信。
- **工具层 (Infrastructure)**: 通用工具函数、常量定义、格式化库。

---

## 2. 技术深度设计

### 2.1 状态管理与数据流 (Pinia)
采用**模块化存储**，区分全局状态与页面私有状态：
- **`user.ts`**: 存储 JWT、用户信息及权限角色。
- **`app.ts`**: 全局 UI 状态（侧边栏开关、全屏加载、主题）。
- **`chat.ts`**: WebSocket 会话状态、未读消息计数。
- **数据流原则**: 
  - 单向数据流：Store -> Component。
  - 复杂异步逻辑封装在 Store 的 Actions 中。

### 2.2 路由与导航架构 (Vue Router)
- **动态路由**: 基于用户角色（`ADMIN` / `USER`）动态加载后台管理模块。
- **权限模型**:
  - `whiteList`: 登录、注册、首页、商品详情。
  - `privateList`: 个人中心、订单、发布、私信。
  - `adminList`: 仪表盘、用户/商品管理。
- **守卫逻辑**: 
  - `beforeEach`: 检查 Token 存在性及 Token 有效期（JWT 解析）。

### 2.3 API 与通信架构
- **Axios 封装**: 
  - 支持请求去重（防止重复点击提交）。
  - 自动注入 `Bearer Token`。
  - 响应统一处理：`code` 非 200 自动弹出 Element Plus `ElMessage` 报错。
- **WebSocket 架构**:
  - 封装全局 `useSocket` Hook。
  - 自动心跳检测、断线重连机制。
  - 消息分发：根据消息类型触发通知 Store 更新。

---

## 4. 组件化开发模式

### 4.1 设计模式
- **高阶组件/插槽 (Slots)**: 统一 Layout 布局，通过 `v-slot` 实现内容注入。
- **组合式函数 (Hooks)**: 抽离通用逻辑（如分页逻辑 `usePagination`、图片上传逻辑 `useUpload`）。
- **组件通信**:
  - 父传子: Props。
  - 子传父: Emits (事件驱动)。
  - 跨层级: Provide/Inject 或 Store。

---

## 5. 安全与性能策略

### 5.1 安全防御
- **XSS 防御**: Vue 3 默认对 `{{ }}` 进行转义；对于 `v-html` 需使用 `DOMPurify` 库过滤。
- **CSRF 防御**: 使用 JWT 无状态认证，不依赖 Cookie。
- **敏感数据**: Token 存储在 `localStorage` 中，敏感操作二次校验（如修改支付方式）。

### 5.2 性能优化
- **按需加载**: 路由懒加载、Element Plus 组件按需导入。
- **资源优化**: 
  - 图片懒加载 (v-lazy)。
  - 图片上传前置压缩（Vite 插件或前端库）。
- **缓存策略**: 静态资源持久化缓存，API 数据使用短期缓存（如分类列表）。

---

## 6. 异常与监控

### 6.1 异常处理
- **全局错误捕获**: `app.config.errorHandler` 监控渲染错误。
- **网络错误**: Axios 拦截器统一拦截并友好提示。
- **边界处理**: 增加 `404` 与 `EmptyState` 组件。

### 6.2 开发规范
- **Git 工作流**: 遵循 Feature/Bugfix 分支模型。
- **Commit 规范**: 遵循 `feat:`, `fix:`, `docs:`, `style:` 等前缀。
