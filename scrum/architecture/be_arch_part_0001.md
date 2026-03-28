<!--
Fragment Index: 1
Range: L1-L15
Word Count: 92
Checksum: b1a2c3d4 (Simulated)
Predecessor: None
Successor: be_arch_part_0002.md
-->
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
