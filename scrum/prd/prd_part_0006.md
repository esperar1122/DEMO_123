<!--
Fragment Index: 6
Range: L78-L94
Word Count: 168
Checksum: e5f6g7h8 (Simulated)
Predecessor: prd_part_0005.md
Successor: None
-->
---

## 3. 技术实现概要

### 3.1 API 端点设计 (部分)
*   **Auth**: `POST /api/auth/register`, `POST /api/auth/login`
*   **Items**: `GET /api/items`, `POST /api/items`
*   **Orders**: `POST /api/orders`, `PUT /api/orders/{id}/confirm`

### 3.2 数据字典
| 表名 | 字段名 | 数据类型 | 约束/注释 |
| :--- | :--- | :--- | :--- |
| **users** | `id`, `username`, `password`, `credit_score`, `status` | `BIGINT`, `VARCHAR(50)`, `VARCHAR(255)`, `INT`, `TINYINT` | PK, UNIQUE, NOT NULL, **DEFAULT 80**, 0:正常/1:封禁 |
| **items** | `id`, `seller_id`, `title`, `price`, `status` | `BIGINT`, `BIGINT`, `VARCHAR(50)`, `DECIMAL(10,2)`, `TINYINT` | PK, FK, **VARCHAR(50)**, >0, 0:在售/1:锁定/2:已售出 |
| **orders** | `id`, `item_id`, `buyer_id`, `transaction_type` | `BIGINT`, `BIGINT`, `BIGINT`, `TINYINT` | PK, FK, FK, 0:面交/1:自提/2:送货 |

---

## 4. 验收标准
1.  **端到端流程通畅**: 可无阻碍地完成“注册-发布-下单-交易-评价”的完整闭环。
2.  **状态机准确**: 商品和订单状态的流转必须与业务规则完全一致。
3.  **数据一致性**: 信用分、订单金额等核心数据的计算和更新必须准确无误。
4.  **后台管理有效**: 管理员的封禁、下架等操作能实时生效。
