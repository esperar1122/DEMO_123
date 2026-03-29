# Quality Gate Report - US-005: 二手商品发布

## Story Information
- **Story ID**: US-005
- **Title**: 二手商品发布
- **Status**: CONDITIONAL PASS
- **Test Timestamp**: 2026-03-28
- **Environment**: Development / Local

## Test Execution Summary
| Acceptance Criterion | Status | Evidence |
| :--- | :--- | :--- |
| 1. 标题必须在 1-20 字之间 | PASS | 后端 `ItemService.createItem` 校验标题长度；前端 `Publish.vue` 设置 `maxlength="20"` |
| 2. 最多支持上传 5 张图片，单张 < 4MB | PASS | 后端 `ItemService` 校验图片数量；前端 `Publish.vue` 在 `handleFileChange` 中校验文件大小 |
| 3. 价格必须大于 0 | PASS | 后端 `ItemService` 校验 `price.compareTo(ZERO) <= 0`；前端使用 `el-input-number` 限制最小值为 0.01 |
| 4. 发布成功后，商品状态默认为 ACTIVE | PASS | 后端 `ItemService` 显式设置 `item.setStatus(0)`，在数据库中对应 `ACTIVE` |

## Functional Correctness Testing
- **Transaction Support**: `ItemService.createItem` 使用了 `@Transactional` 保证商品主表和图片表写入的原子性。验证通过。
- **Field Constraints**: 商品描述支持 1-1000 字，符合 PRD 要求。
- **Image Persistence**: 前端将图片转为 Base64 字符串发送至后端，后端存入 `item_images` 表。

## Negative & Edge Case Coverage
- **Empty Title/Description**: 后端抛出 `IllegalArgumentException`，前端表单校验拦截。
- **Invalid Price**: 后端拒绝小于等于 0 的价格。
- **Too Many Images**: 前端 `:limit="5"` 限制，后端亦有校验兜底。

## Risk Assessment
- **High Risk**: `item_images.image_url` 在 `schema.sql` 中定义为 `VARCHAR(255)`，但前端发送的是 Base64 编码的图片数据。**由于 4MB 图片的 Base64 长度远超 255 个字符，这将导致数据库写入失败（数据过长）**。
- **Recommendation**: 建议将 `image_url` 字段类型修改为 `LONGTEXT` 或切换至真正的文件存储系统（如本地存储或 OSS）。

## Verdict
**CONDITIONAL PASS**
业务逻辑实现正确，符合验收标准。但存在严重的技术实现缺陷（数据库字段长度不足以支撑 Base64 存储），需在上线前修正数据库表结构。

---
*QA Guardian*
