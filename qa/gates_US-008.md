# Quality Gate Report - US-008: 订单创建与锁定

## Story Information
- **Story ID**: US-008
- **Title**: 订单创建与锁定
- **Status**: PASS
- **Test Timestamp**: 2026-03-28
- **Environment**: Development / Local

## Test Execution Summary
| Acceptance Criterion | Status | Evidence |
| :--- | :--- | :--- |
| 1. 下单时必须从三种交易方式中选择一种 | PASS | 前端 `Chat.vue` 提供单选框（面交、自提、送货）；后端 `OrderService` 校验送货地址 |
| 2. 订单创建后，商品状态从 ACTIVE 变为 LOCKED | PASS | 后端 `OrderService.createOrder` 中执行 `item.setStatus(1)` |
| 3. 订单初始状态为 PENDING | PASS | 后端 `OrderService.createOrder` 中执行 `order.setStatus(0)` |

## Functional Correctness Testing
- **Transactional Integrity**: `OrderService.createOrder` 使用了 `@Transactional`，确保商品锁定和订单创建在同一个事务中。验证通过。
- **Permission Check**: 后端校验了用户不能购买自己的商品，且商品必须处于 `ACTIVE` 状态。
- **Notification**: 下单成功后，系统自动向卖家发送一条“新订单提醒”的系统通知，逻辑完整。

## Negative & Edge Case Coverage
- **Buy Own Item**: 后端拒绝并返回 "不能购买自己的商品"，逻辑正确。
- **Locked Item**: 若商品已被他人锁定（status != 0），后端拒绝下单，防止超卖。
- **Banned User**: 封禁用户无法下单。

## UX Consistency Checks
- **Flow**: 用户点击“我要购买”进入私信页，沟通后再点击“立即购买”进行下单，符合校园二手交易先咨询后下单的习惯。
- **Validation**: 选择了“送货上门”但未填地址时，后端会进行拦截并提示。

## Verdict
**PASS**
该功能实现严谨，事务处理正确，状态机流转符合 PRD 要求。

---
*QA Guardian*
