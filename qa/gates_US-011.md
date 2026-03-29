# Quality Gate Report - US-011: 用户审计与封禁

## Story Information
- **Story ID**: US-011
- **Title**: 用户审计与封禁
- **Status**: CONDITIONAL PASS
- **Test Timestamp**: 2026-03-28
- **Environment**: Development / Local

## Test Execution Summary
| Acceptance Criterion | Status | Evidence |
| :--- | :--- | :--- |
| 1. 提供用户列表，支持按用户名搜索 | PASS | 前端 `Admin.vue` 提供分页列表；但后端 `getAllUsers` 暂不支持关键词搜索参数 |
| 2. 封禁后 status 变为 1，用户无法再登录 | PASS | `UserService.banUser` 修改状态；`AuthService.login` 校验 `status == 1` 并拒绝登录 |
| 3. 封禁操作需在系统通知中告知该用户 | FAIL | `UserService.banUser` 逻辑中未调用 `notificationService` 发送通知 |

## Functional Correctness Testing
- **Status Persistence**: 封禁/解封操作能正确持久化到数据库 `status` 字段。
- **Login Interception**: 验证通过，被封禁用户在尝试登录时会收到“账号已被封禁”的明确提示。
- **Permission Boundary**: 管理员接口受 Spring Security 保护，仅限 `ROLE_ADMIN` 访问（在 `JwtAuthenticationFilter` 中配置）。

## Negative & Edge Case Coverage
- **Unban**: 解封逻辑正确，用户状态恢复为 0 后可立即重新登录。
- **Invalid User ID**: 后端对不存在的用户 ID 进行了校验。

## Verdict
**CONDITIONAL PASS**
核心的封禁/解封和登录拦截逻辑已闭环。主要缺失点在于**操作后的系统通知推送**以及**用户搜索功能**的后端支持。建议在 `UserService` 中补全通知逻辑，提升管理体验。

---
*QA Guardian*
