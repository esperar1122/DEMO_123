# Quality Gate Report - US-009: 实时私信咨询

## Story Information
- **Story ID**: US-009
- **Title**: 实时私信咨询
- **Status**: FAIL
- **Test Timestamp**: 2026-03-28
- **Environment**: Development / Local

## Test Execution Summary
| Acceptance Criterion | Status | Evidence |
| :--- | :--- | :--- |
| 1. 基于 WebSocket 实现，消息发送后对方实时收到红点提醒 | FAIL | 前后端均未使用 WebSocket，而是通过 `setInterval` 每 3 秒进行一次 HTTP 轮询 (Polling) |
| 2. 首次咨询自动创建会话 | PASS | `MessageService.createMessage` 将消息持久化至数据库，实现了基于商品的咨询记录 |
| 3. 消息需持久化存储，支持查看历史聊天记录 | PASS | `messages` 表存储所有对话内容，`getMessagesByItem` 接口支持历史记录加载 |

## Functional Correctness Testing
- **Persistence**: 消息正确存入数据库，刷新页面后记录依然存在。
- **Notification**: 消息发送后，卖家会收到一条类型为 `message` 的系统通知，且首页红点会通过轮询更新。
- **Concurrency**: 多用户同时留言时，由于使用数据库存储，不会出现消息丢失，但实时性受限于 3 秒的轮询间隔。

## Negative & Edge Case Coverage
- **Banned User**: 封禁用户无法发送消息，后端校验通过。
- **Empty Message**: 前后端均有非空校验，逻辑正确。

## Verdict
**FAIL**
虽然功能逻辑（发送、接收、存储、通知）已实现且能跑通，但**未满足“基于 WebSocket 实现”这一核心技术规格要求**。HTTP 轮询在消息密集场景下会对服务器造成不必要的压力且实时性较差。请开发工程师重构为 WebSocket 长连接实现。

---
*QA Guardian*
