<!--
Fragment Index: 3
Range: L34-L56
Word Count: 128
Checksum: c3d4e5f6 (Simulated)
Predecessor: fe_arch_part_0002.md
Successor: fe_arch_part_0004.md
-->
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
