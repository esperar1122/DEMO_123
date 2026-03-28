<!--
Fragment Index: 6
Range: L96-L110
Word Count: 118
Checksum: g7h8i9j0 (Simulated)
Predecessor: be_arch_part_0005.md
Successor: None
-->
---

## 7. 全局异常处理
- **统一返回**: 封装 `Result<T>` 类，包含 `code`, `message`, `data`。
- **全局捕获**: 使用 `@RestControllerAdvice` 和 `@ExceptionHandler`。
- **异常分类**:
  - `BusinessException`: 业务逻辑错误 (如信用分不足、商品已锁定)。
  - `SecurityException`: 认证授权错误。
  - `SystemException`: 未知系统错误。

---

## 8. 开发规范
- **RESTful 风格**: 使用标准的 HTTP 方法 (GET, POST, PUT, DELETE)。
- **Lombok**: 强制使用 `@Data`, `@Builder` 减少模板代码。
- **版本控制**: API 路径建议以 `/api/v1` 开头。
