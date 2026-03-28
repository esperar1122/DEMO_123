<!--
Fragment Index: 5
Range: L78-L95
Word Count: 124
Checksum: f6g7h8i9 (Simulated)
Predecessor: be_arch_part_0004.md
Successor: be_arch_part_0006.md
-->
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
