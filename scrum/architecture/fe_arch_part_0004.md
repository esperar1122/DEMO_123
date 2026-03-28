<!--
Fragment Index: 4
Range: L57-L83
Word Count: 154
Checksum: d4e5f6g7 (Simulated)
Predecessor: fe_arch_part_0003.md
Successor: None
-->
---

## 5. 安全与性能策略

### 5.1 安全防御
- **XSS 防御**: Vue 3 默认对 `{{ }}` 进行转义；对于 `v-html` 需使用 `DOMPurify` 库过滤。
- **CSRF 防御**: 使用 JWT 无状态认证，不依赖 Cookie。
- **敏感数据**: Token 存储在 `localStorage` 中，敏感操作二次校验（如修改支付方式）。

### 5.2 性能优化
- **按需加载**: 路由懒加载、Element Plus 组件按需导入。
- **资源优化**: 
  - 图片懒加载 (v-lazy)。
  - 图片上传前置压缩（Vite 插件或前端库）。
- **缓存策略**: 静态资源持久化缓存，API 数据使用短期缓存（如分类列表）。

---

## 6. 异常与监控

### 6.1 异常处理
- **全局错误捕获**: `app.config.errorHandler` 监控渲染错误。
- **网络错误**: Axios 拦截器统一拦截并友好提示。
- **边界处理**: 增加 `404` 与 `EmptyState` 组件。

### 6.2 开发规范
- **Git 工作流**: 遵循 Feature/Bugfix 分支模型。
- **Commit 规范**: 遵循 `feat:`, `fix:`, `docs:`, `style:` 等前缀。
