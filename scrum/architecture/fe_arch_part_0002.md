<!--
Fragment Index: 2
Range: L14-L33
Word Count: 112
Checksum: b2c3d4e5 (Simulated)
Predecessor: fe_arch_part_0001.md
Successor: fe_arch_part_0003.md
-->
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
