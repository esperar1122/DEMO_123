# 轻量化校园二手交易平台 - 后端架构设计文档 (V1.0.0)

## 1. 架构总览
依据 PRD V1.3.0 和前端规格说明书，本项目后端采用基于 **Spring Boot 3** 的**分层架构设计**。系统旨在提供高性能、可扩展且易于维护的 RESTful API 服务，并利用 **Mybatis-Plus** 简化持久层操作。

### 1.1 核心技术栈
- **核心框架**: Spring Boot 3.x (Java 17+)
- **持久层框架**: Mybatis-Plus 3.5.x
- **数据库**: MySQL 8.0
- **安全认证**: Spring Security + JWT (JSON Web Token)
- **实时通信**: Spring WebSocket
- **工具类**: Lombok, Hutool, Jackson
- **API 文档**: Swagger / Knife4j (推荐)

---

## 2. 逻辑架构与分层设计

### 2.1 项目目录结构 (Maven)
```text
src/main/java/com/campus/scrum/
├── common/              # 公共模块 (常量、枚举、工具类)
│   ├── exception/       # 全局异常处理
│   ├── result/          # 统一返回结果封装
│   └── utils/           # 工具类 (JWT, 密码加密等)
├── config/              # 配置类 (Security, Mybatis-Plus, WebSocket)
├── controller/          # 表现层 (REST API 暴露)
│   ├── auth/            # 注册、登录
│   ├── item/            # 商品相关
│   ├── order/           # 订单相关
│   └── admin/           # 管理员功能
├── service/             # 业务逻辑层接口
│   └── impl/            # 业务逻辑层实现
├── mapper/              # 持久层 (Mybatis-Plus Mapper 接口)
├── model/               # 数据模型
│   ├── entity/          # 数据库实体类 (PO)
│   ├── dto/             # 数据传输对象 (Request Params)
│   └── vo/              # 视图对象 (Response Result)
├── security/            # 安全模块 (JWT 过滤器、认证授权)
└── websocket/           # WebSocket 处理器与会话管理
```

### 2.2 分层原则
- **Controller**: 负责请求参数校验、响应分发，严禁包含业务逻辑。
- **Service**: 核心业务逻辑实现层，处理事务控制。
- **Mapper**: 专注于 SQL 操作，利用 Mybatis-Plus 提高开发效率。
- **Entity**: 映射数据库表，使用 MyBatis-Plus 注解进行主键生成和逻辑删除配置。

---

## 3. 安全认证设计 (JWT + Spring Security)

### 3.1 认证流程
1. **登录**: 用户提交账号密码，后端验证通过后生成 JWT。
2. **载荷 (Payload)**: 包含 `userId` 和 `role`。
3. **有效期**: 7天 (依据 PRD)。
4. **验证**: 前端在 Header 的 `Authorization` 字段携带 `Bearer <token>`。
5. **拦截器/过滤器**: `JwtAuthenticationFilter` 拦截受保护资源，解析 Token 并将其存入 `SecurityContextHolder`。

### 3.2 权限控制
- 使用 Spring Security 的注解 `@PreAuthorize("hasRole('ADMIN')")` 进行接口级权限控制。

---

## 4. 数据库设计方案

### 4.1 核心表设计 (MyBatis-Plus 集成)
- **逻辑删除**: 所有核心表（用户、商品、订单）均增加 `is_deleted` 字段，配合 MyBatis-Plus 实现逻辑删除。
- **自动填充**: 利用 `MetaObjectHandler` 自动填充 `create_time` 和 `update_time`。

### 4.2 性能优化
- **索引**: 
  - `items` 表: `seller_id`, `category_id`, `status` 建立索引；`title` 建立全文索引或前缀索引。
  - `orders` 表: `buyer_id`, `seller_id`, `item_id` 建立索引。
- **事务管理**: 涉及信用分更新、订单状态流转等操作，统一使用 `@Transactional` 注解保证原子性。

---

## 5. 实时通信设计 (WebSocket)
依据 PRD V1.3.0 咨询功能：
- **处理器**: 实现 `WebSocketHandler` 或使用 `@ServerEndpoint`。
- **会话管理**: 使用 `ConcurrentHashMap` 存储在线用户的 `Session`。
- **消息持久化**: 聊天消息实时写入数据库 `chat_messages` 表。
- **集成**: 在拦截器中获取 JWT 识别用户身份。

---

## 6. 文件存储方案 (商品图片)
- **处理**: 接口接收 `MultipartFile`。
- **存储**: 
  - 初期: 本地存储 (指定路径)，返回静态映射 URL。
  - 扩展: 封装 `FileService` 接口，便于未来无缝切换至 OSS 或 MinIO。
- **约束**: 限制单张图片大小为 4MB，总数 5 张。

---

## 7. 全局异常处理
- **统一返回**: 封装 `Result<T>` 类，包含 `code`, `message`, `data`。
- **全局捕获**: 使用 `@RestControllerAdvice` 和 `@ExceptionHandler`。
- **异常分类**:
  - `BusinessException`: 业务逻辑错误 (如信用分不足、商品已锁定)。
  - `SecurityException`: 认证授权错误。
  - `SystemException`: 未知系统错误。

---

## 8. 开发规范
- **RESTful 风格**: 使用标准的 HTTP 方法 (GET, POST, PUT, DELETE)。
- **Lombok**: 强制使用 `@Data`, `@Builder` 减少模板代码。
- **版本控制**: API 路径建议以 `/api/v1` 开头。
