# Quality Gate Report - US-007: 多维度搜索与筛选

## Story Information
- **Story ID**: US-007
- **Title**: 多维度搜索与筛选
- **Status**: FAIL
- **Test Timestamp**: 2026-03-28
- **Environment**: Development / Local

## Test Execution Summary
| Acceptance Criterion | Status | Evidence |
| :--- | :--- | :--- |
| 1. 关键词搜索覆盖标题和描述 | PASS | 后端 `ItemService.getActiveItems` 使用 `like("title", keyword).or().like("description", keyword)` 实现 |
| 2. 支持按分类和校区筛选 | FAIL | `items` 表缺少 `category_id` 和 `campus_id` 字段；前后端均未实现相关逻辑 |
| 3. 支持按“最新发布”、“价格升/降”、“信用最高”排序 | FAIL | 后端硬编码为 `orderByDesc("created_at")`，不支持价格或信用分排序切换 |

## Functional Correctness Testing
- **Keyword Search**: 验证通过，前端输入关键词后能正确调用后端 API 并返回匹配结果。
- **Data Model Inconsistency**: 数据库设计未包含分类和校区信息，导致相关业务功能无法开展。
- **Sorting Logic**: 排序规则过于单一，未提供参数化排序接口。

## Negative & Edge Case Coverage
- **Empty Keyword**: 传空字符串时返回所有在售商品，符合预期。
- **No Results**: 关键词不匹配时展示空状态，符合预期。

## Verdict
**FAIL**
该功能实现严重缩水，仅完成了最基础的关键词搜索。请开发工程师补充数据库字段（分类、校区）、完善后端动态 SQL 筛选与排序逻辑，并更新前端 UI 以支持多维度筛选。

---
*QA Guardian*
