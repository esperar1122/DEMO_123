<!--
Fragment Index: 1
Range: L1-L16
Word Count: 105
Checksum: 7d4a2f8b (Simulated)
Predecessor: None
Successor: prd_part_0002.md
-->
# 轻量化校园二手交易平台 - 产品需求文档 (PRD)

| 版本 | 作者 | 日期 | 状态 | 备注 |
| :--- | :--- | :--- | :--- | :--- |
| V1.3.0 | Requirements Expert | 2026-03-25 | 开发就绪 | 整合用户反馈，形成最终版 |

---

## 1. 项目概述

### 1.1 背景与目标
为解决校园二手交易信息不对称、信任成本高的问题，我们构建一个基于 SpringBoot 3 和 Vue 3 的轻量化平台。**核心目标是提供一个功能完整、逻辑严密、代码流畅的校园内部交易市场，为后续 Epic/Story 拆分提供清晰的蓝图。**

### 1.2 核心技术栈
- **前端**: Vue 3 (Composition API) + Vite + Element Plus + Pinia + Vue Router
- **后端**: Spring Boot 3 + **Mybatis-Plus** + JWT
- **数据库**: MySQL 8.0

### 1.3 核心业务流程
`访客 -> 注册/登录 -> (买家)搜索/浏览 -> 查看商品 -> 咨询 -> 下单 -> (卖家)确认订单 -> 线下交易(面交/自提/送货) -> (买家)确认收货 -> 双方互评 -> 交易完成`
