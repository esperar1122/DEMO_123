<!--
Fragment Index: 2
Range: L16-L48
Word Count: 148
Checksum: c3d4e5f6 (Simulated)
Predecessor: be_arch_part_0001.md
Successor: be_arch_part_0003.md
-->
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
