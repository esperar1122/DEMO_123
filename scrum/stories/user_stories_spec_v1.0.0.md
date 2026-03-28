# 轻量化校园二手交易平台 - 标准化 User Stories 规格说明书 (V1.0.0)

本说明书由 Agile Coach 编写，严格基于 [PRD V1.3.0](file:///c:/Users/张潇予/Documents/trae_projects/scrum/prd_v1.3.0.md) 及其分片文档、前端架构设计及后端架构设计。所有 Story 均遵循 INVEST 原则，并包含技术实现上下文。

---

## Epic 1: 用户管理模块 (User Management)

### US-001: 用户账号注册
- **User Story**: 作为一名校园访客，我想要通过输入基本信息注册账号，以便于我能使用平台的发布和交易功能。
- **Acceptance Criteria**:
  1. Given 访客进入注册页面，When 输入用户名(4-16位, 字母/数字/下划线)且唯一，Then 前端应显示校验通过标识。
  2. Given 输入符合规则的密码(6-20位, 字母+数字)且确认密码一致，When 点击注册，Then 后端应创建用户记录并返回初始信用分 80。
  3. 后端必须使用 BCrypt 对密码进行哈希处理后入库。
  4. 注册成功后应自动触发自动登录逻辑。
- **Technical Notes**: 涉及 `users` 表；后端 `AuthController.register()`；前端 `Login.vue` 切换至注册模式。
- **Dependencies**: 无。
- **Estimation Aid**: Medium | 6-8 h | 假设：前端已有基础表单组件。

### US-002: 用户登录与 JWT 认证
- **User Story**: 作为已注册用户，我想要通过账号密码登录系统，以便于我能安全地访问我的私有数据。
- **Acceptance Criteria**:
  1. 输入正确凭证，系统返回有效 JWT (7天有效期)。
  2. 登录成功后，前端 Pinia Store 需持久化存储 `token` 和 `userInfo`。
  3. 所有受保护 API 请求头必须携带 `Authorization: Bearer <token>`。
  4. 登录失败需给出明确的友好提示（用户名或密码错误）。
- **Technical Notes**: 涉及 `JwtAuthenticationFilter` 和 `UserStore.ts`。
- **Dependencies**: Depends on: US-001。
- **Estimation Aid**: Medium | 4-6 h | 假设：Spring Security 已配置基础框架。

### US-003: 个人资料管理
- **User Story**: 作为平台用户，我想要修改我的头像、联系方式和所属校区，以便于其他交易者能联系到我。
- **Acceptance Criteria**:
  1. 支持上传 4MB 以内的图片作为头像。
  2. 可修改微信号、手机号等联系信息。
  3. 校区选择应从后端配置的校区列表中选取。
- **Technical Notes**: 涉及 `FileService` (图片上传)；后端 `UserController.updateMe()`。
- **Dependencies**: Depends on: US-002。
- **Estimation Aid**: Low | 2-4 h。

### US-004: 信用评级展示
- **User Story**: 作为一名交易者，我想要在我的资料页看到我的信用等级，以便于我了解自己的信誉状况。
- **Acceptance Criteria**:
  1. 根据 `credit_score` 映射并展示等级文字（如：95分显示“信用极好”）。
  2. 初始分为 80 分。
  3. 信用分变更时（如 US-012 完成交易），UI 需实时更新。
- **Technical Notes**: 等级映射逻辑：90-100极好, 80-89优秀, 70-79良好, 60-69一般, 0-59糟糕。
- **Dependencies**: Depends on: US-003。
- **Estimation Aid**: Low | 1-2 h。

---

## Epic 2: 商品管理模块 (Item Management)

### US-006: 二手商品发布
- **User Story**: 作为卖家，我想要发布二手商品信息并上传多张图片，以便于买家能看到并购买。
- **Acceptance Criteria**:
  1. 标题必须在 1-20 字之间。
  2. 最多支持上传 5 张图片，单张 < 4MB。
  3. 价格必须大于 0。
  4. 发布成功后，商品状态默认为 `ACTIVE` (在售)。
- **Technical Notes**: 涉及 `items` 表和 `item_images` 表；前端 `ImageUpload` 组件。
- **Dependencies**: Depends on: US-002。
- **Estimation Aid**: High | 8-12 h | 包含图片压缩和多表关联写入。

### US-008: 商品列表瀑布流展示
- **User Story**: 作为买家，我想要在首页以瀑布流形式浏览所有在售商品，以便于快速发现感兴趣的物品。
- **Acceptance Criteria**:
  1. 列表仅展示状态为 `ACTIVE` 的商品。
  2. 支持分页加载（每页 20 条）。
  3. 卡片包含：首图、标题、价格、卖家等级标签。
- **Technical Notes**: 后端 `ItemController.list()` 支持 PageHelper/Mybatis-Plus 分页。
- **Dependencies**: Depends on: US-006。
- **Estimation Aid**: Medium | 4-6 h。

### US-010: 多维度搜索与筛选
- **User Story**: 作为买家，我想要按关键词、分类和排序规则筛选商品，以便于精准定位我需要的物品。
- **Acceptance Criteria**:
  1. 关键词搜索覆盖标题和描述。
  2. 支持按分类（数码、生活等）和校区筛选。
  3. 支持按“最新发布”、“价格升/降”、“信用最高”排序。
- **Technical Notes**: SQL 需动态拼接 `WHERE` 和 `ORDER BY`；分类数据来源于 `categories` 表。
- **Dependencies**: Depends on: US-008。
- **Estimation Aid**: Medium | 6-8 h。

---

## Epic 3: 交易与私信模块 (Transaction & Messaging)

### US-011: 订单创建与锁定
- **User Story**: 作为买家，我想要对心仪商品点击“我想要”并选择交易方式，以便于锁定商品防止被他人购买。
- **Acceptance Criteria**:
  1. 下单时必须从“校园面交”、“自提”、“送货上门”中选择一种。
  2. 订单创建后，商品状态必须从 `ACTIVE` 变为 `LOCKED`。
  3. 订单初始状态为 `PENDING` (待确认)。
- **Technical Notes**: 必须在数据库事务 `@Transactional` 中同时更新商品和订单状态。
- **Dependencies**: Depends on: US-006, US-008。
- **Estimation Aid**: High | 6-8 h | [Split Hint] 涉及复杂的原子性状态变更。

### US-013: 实时私信咨询
- **User Story**: 作为买家，我想要在商品页直接向卖家发消息咨询细节，以便于确认交易意向。
- **Acceptance Criteria**:
  1. 基于 WebSocket 实现，消息发送后对方应能实时收到红点提醒。
  2. 首次咨询自动创建会话。
  3. 消息需持久化存储，支持查看历史聊天记录。
- **Technical Notes**: 后端 `Spring WebSocket` + 前端 `useSocket` Hook；涉及 `chat_messages` 表。
- **Dependencies**: Depends on: US-002。
- **Estimation Aid**: High | 12-16 h | 涉及长连接管理和心跳检测。

---

## Epic 4: 管理后台模块 (Admin Backend)

### US-016: 管理员数据看板
- **User Story**: 作为管理员，我想要查看平台运营的核心指标和趋势图，以便于监控平台活跃度。
- **Acceptance Criteria**:
  1. 展示总用户数、今日新增、总成交额、今日成交额。
  2. 展示近 7 日用户增长折线图。
  3. 展示商品分类占比饼图。
- **Technical Notes**: 后端 `AdminController.getStats()`；前端集成 `ECharts`。
- **Dependencies**: Depends on: US-002 (Admin Role)。
- **Estimation Aid**: Medium | 6-8 h。

### US-017: 用户审计与封禁
- **User Story**: 作为管理员，我想要对违规用户进行封禁操作，以便于维护平台交易秩序。
- **Acceptance Criteria**:
  1. 提供用户列表，支持按用户名搜索。
  2. 点击“封禁”后，用户 `status` 变为 1，且该用户无法再登录系统。
  3. 封禁操作需在系统通知中告知该用户。
- **Technical Notes**: `SecurityContext` 需拦截 `status=1` 的用户登录请求。
- **Dependencies**: Depends on: US-002。
- **Estimation Aid**: Low | 2-4 h。

---

## 5. 汇总概览
- **Total Stories**: 12 (精选核心路径)
- **High Complexity**: US-006, US-011, US-013
- **Ready for**: Sprint Planning & Epic/Story Splitting
