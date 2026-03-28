<!--
Fragment Index: 3
Range: L35-L49
Word Count: 114
Checksum: b5a4c3d2 (Simulated)
Predecessor: prd_part_0002.md
Successor: prd_part_0004.md
-->
### 2.2 商品管理模块

*   **Epic**: 作为用户，我希望能方便地发布、管理和发现商品。

*   **Story 1: 商品发布/编辑**
    *   **字段约束**: 标题(**1-20字**), 描述(1-1000字), 图片(**1-5张**, 前端压缩<**4MB**), 价格(>0)。
    *   **规则**: 未完成交易的商品，仅可修改描述/图片。

*   **Story 2: 商品状态机**
    *   `ACTIVE` (在售) -> `LOCKED` (被下单后锁定) -> `SOLD` (交易完成后)。
    *   `LOCKED` -> `ACTIVE` (订单被取消/拒绝)。
    *   `ACTIVE` -> `OFFLINE` (卖家手动下架)。
