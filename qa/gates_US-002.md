# Quality Gate Report - US-002: 用户登录与 JWT 认证

## Story Information
- **Story ID**: US-002
- **Title**: 用户登录与 JWT 认证
- **Status**: PASS
- **Test Timestamp**: 2026-03-28
- **Environment**: Development / Local

## Test Execution Summary
| Acceptance Criterion | Status | Evidence |
| :--- | :--- | :--- |
| 1. 输入正确凭证，返回有效 JWT (7天有效期) | PASS | `AuthService.login` 调用 `jwtUtil.generateToken`；`application.yml` 配置 `jwt.expiration: 604800000` (7天) |
| 2. 登录成功后，Pinia Store 持久化存储 token 和 userInfo | PASS | `frontend/src/stores/user.js` 中的 `setAuth` 方法将数据存入 state 和 `localStorage` |
| 3. 受保护 API 请求头携带 Authorization: Bearer <token> | PASS | `frontend/src/api/index.js` 的请求拦截器自动注入 Token |
| 4. 登录失败需给出友好提示 | PASS | 后端抛出 `IllegalArgumentException`，前端 `Login.vue` 通过 `ElMessage.error` 展示 |

## Functional Correctness Testing
- **Primary Flow**: 用户输入用户名密码 -> 后端校验 -> 返回 Token 及用户信息 -> 前端跳转 Home 页面。验证通过。
- **Admin Access**: `Login.vue` 区分了用户和管理员登录入口，并校验了 `role` 字段。
- **Banned User**: `AuthService.login` 显式检查 `user.getStatus() == 1` 并拒绝登录，符合安全要求。
- **Token Expiry**: `JwtUtil` 正确处理过期时间，`JwtAuthenticationFilter` 会拦截过期 Token。

## Negative & Edge Case Coverage
- **Invalid Password**: 后端返回 "用户名或密码错误"，前端提示正确。
- **Non-existent User**: 后端返回 "用户名或密码错误"，不泄露用户是否存在，安全性良好。
- **Expired Token**: `api/index.js` 响应拦截器捕获 401 状态码并重定向至登录页。

## UX Consistency Checks
- **Loading State**: `Login.vue` 在请求期间展示 `loading` 动画。
- **Validation**: 表单项（用户名、密码）在提交前进行必填项校验。

## Verdict
**PASS**
该功能实现完整，符合 PRD 和架构设计要求，安全性与用户体验良好。

---
*QA Guardian*
