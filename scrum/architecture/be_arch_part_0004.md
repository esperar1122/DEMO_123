<!--
Fragment Index: 4
Range: L64-L77
Word Count: 108
Checksum: e5f6g7h8 (Simulated)
Predecessor: be_arch_part_0003.md
Successor: be_arch_part_0005.md
-->
---

## 4. 数据库设计方案

### 4.1 核心表设计 (MyBatis-Plus 集成)
- **逻辑删除**: 所有核心表（用户、商品、订单）均增加 `is_deleted` 字段，配合 MyBatis-Plus 实现逻辑删除。
- **自动填充**: 利用 `MetaObjectHandler` 自动填充 `create_time` 和 `update_time`。

### 4.2 性能优化
- **索引**: 
  - `items` 表: `seller_id`, `category_id`, `status` 建立索引；`title` 建立全文索引或前缀索引。
  - `orders` 表: `buyer_id`, `seller_id`, `item_id` 建立索引。
- **事务管理**: 涉及信用分更新、订单状态流转等操作，统一使用 `@Transactional` 注解保证原子性。
