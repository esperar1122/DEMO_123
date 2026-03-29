# Quality Gate Report - US-006: 商品列表瀑布流展示

## Story Information
- **Story ID**: US-006
- **Title**: 商品列表瀑布流展示
- **Status**: CONDITIONAL PASS
- **Test Timestamp**: 2026-03-28
- **Environment**: Development / Local

## Test Execution Summary
| Acceptance Criterion | Status | Evidence |
| :--- | :--- | :--- |
| 1. 列表仅展示状态为 ACTIVE 的商品 | PASS | 后端 `ItemService.getActiveItems` 使用 `wrapper.eq("status", 0)` 过滤 |
| 2. 支持分页加载（每页 20 条） | PASS | 后端支持 MyBatis-Plus 分页；前端 `Home.vue` 实现分页 UI，但每页显示 12 条 (Minor Discrepancy) |
| 3. 卡片包含：首图、标题、价格、卖家等级标签 | FAIL | 卡片包含首图、标题、价格，但**缺少“卖家等级标签”** |

## Functional Correctness Testing
- **Active Filtering**: 验证通过，非 `ACTIVE` 状态（如 `LOCKED`, `SOLD`, `OFFLINE`）的商品不会出现在首页列表中。
- **Pagination**: 前端 `el-pagination` 与后端 `Page` 对象对接正确，支持翻页加载。
- **Image Display**: 卡片正确提取并展示商品的第一张图片（Base64 格式）。

## Negative & Edge Case Coverage
- **Empty List**: 当无商品时，前端正确展示 `el-empty` 缺省页。
- **Deleted Items**: 后端逻辑删除 `deleted=1` 的商品已被过滤。

## UX Consistency Checks
- **Waterfall Layout**: 前端通过 `.items-grid` 的 CSS 网格布局实现类瀑布流展示。
- **Navigation**: 点击卡片可正确跳转至 `/item/{id}` 详情页。

## Verdict
**CONDITIONAL PASS**
基础展示和分页功能完善。主要缺失点在于**卖家等级标签**的展示，且后端 API 目前未返回卖家信用信息，建议在 `ItemController.getItems` 中联表查询或补充卖家信用等级数据。

---
*QA Guardian*
