<!--
Fragment Index: 3
Range: L49-L63
Word Count: 112
Checksum: d4e5f6g7 (Simulated)
Predecessor: be_arch_part_0002.md
Successor: be_arch_part_0004.md
-->
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
