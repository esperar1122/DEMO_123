<!--
Fragment Index: 4
Range: L50-L67
Word Count: 132
Checksum: d9c8b7a6 (Simulated)
Predecessor: prd_part_0003.md
Successor: prd_part_0005.md
-->
### 2.3 交易管理模块

*   **Epic**: 作为用户，我希望能顺畅地完成交易并与对方沟通。

*   **Story 1: 订单创建与交易方式**
    *   **流程**: 买家点击“我想要”创建订单，商品状态变为 `LOCKED`。
    *   **交易方式**: 下单时需选择“校园面交”、“自提”或“送货上门”（如选此项，需填写/确认地址）。

*   **Story 2: 订单状态机**
    *   `PENDING` (待支付/确认): 买家下单后。卖家可“确认”/“拒绝”，买家可“取消”。
    *   `CONFIRMED` (待交易): 卖家确认后。
    *   `COMPLETED` (已完成): 买家确认收货后。触发信用分计算。
    *   `CANCELLED` (已取消): 流程中取消。商品状态恢复 `ACTIVE`。
