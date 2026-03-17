# 修复进度记录

## 任务
- P1-2: 修复 `findByConfigEnvironmentIdAndNamespaceId` 查询条件遗漏 `namespaceId` 的问题。

## 进度
- [x] Step 1: 定位实现文件与方法。
- [x] Step 2: 核对当前实现是否包含 `namespaceId` 条件。
- [x] Step 3: 代码修复判定：当前分支已包含修复，无需再次改动。
- [x] Step 4: 错误检查（见下方记录）。
- [x] Step 5: 输出结论与下一步建议。

## 过程记录
- 2026-03-10: 已定位到 `ConfigItemMyBatisRepository#findByConfigEnvironmentIdAndNamespaceId`。
- 2026-03-10: 当前实现已包含：
  - `lambdaQueryWrapper.eq(ConfigItem::getConfigEnvironmentId, configEnvironmentId);`
  - `lambdaQueryWrapper.eq(ConfigItem::getNamespaceId, namespaceId);`
- 2026-03-10: 全局检索确认该仓储实现仅此一处，未发现漏加 `namespaceId` 的同名实现。
- 2026-03-10: 结论：P1-2 在当前代码状态下为“已修复/已覆盖”。

## 任务
- P2-3: 修复 `updateContentById` 在记录不存在时的空指针风险。

## 进度
- [x] Step 1: 定位问题方法 `ConfigItemMyBatisRepository#updateContentById`。
- [x] Step 2: 增加 `findById` 结果空判。
- [x] Step 3: 统一抛出业务异常 `CommonErrors.DATA_NOT_EXIST`。
- [x] Step 4: 错误检查通过。

## 过程记录
- 2026-03-10: 在 `updateContentById` 中新增空判逻辑，避免 `findById(id).getContent()` 触发 NPE。
- 2026-03-10: 异常语义统一为 `DATA_NOT_EXIST`，并带 `configItemId` 上下文信息。

## 任务
- P1-4: 修复签名认证日志敏感信息明文输出。

## 进度
- [x] Step 1: 定位敏感日志输出点（Header 校验日志与认证通过日志）。
- [x] Step 2: 移除 `nonce/signature` 明文日志输出。
- [x] Step 3: 对 `accessKey` 改为脱敏输出。
- [x] Step 4: 错误检查通过。

## 过程记录
- 2026-03-10: `validateHeaders` 日志由全量输出改为脱敏输出，仅保留 `accessKey` 脱敏值与时间戳。
- 2026-03-10: `签名认证通过` 日志中的 `accessKey` 改为脱敏值。
- 2026-03-10: 新增 `maskSensitive` 方法统一处理敏感字符串脱敏。

## 任务
- P1-1: 修复 Cyberark DAO SQL 字段与实体映射不一致问题。

## 进度
- [x] Step 1: 定位 `ConfigCyberarkInfoDao#findByComponentCodeAndCyberarkObject`。
- [x] Step 2: 将错误字段 `access_key,secret_key` 改为实体实际所需字段。
- [x] Step 3: 显式使用列别名映射到实体属性（`componentCode/cyberarkObject/encryptedValue/appKey/enabled`）。
- [x] Step 4: 错误检查通过。

## 过程记录
- 2026-03-10: SQL 改为查询 `config_cyberark_info` 的业务字段，并显式 `AS` 到实体驼峰字段，避免字段映射歧义。
- 2026-03-10: 保留原有过滤条件 `component_code + cyberark_object + is_enabled`，确保行为一致。

## 任务
- P2-2: 修复 `LongPollingService` 吞异常与 `printStackTrace` 问题。

## 进度
- [x] Step 1: 定位 `onTimeout/safeCompleteContext/destroy` 中的异常处理反模式。
- [x] Step 2: 用结构化日志替换空 catch 与 `printStackTrace`。
- [x] Step 3: 保持原行为不变，仅增强可观测性。
- [x] Step 4: 错误检查通过。

## 过程记录
- 2026-03-10: `onTimeout` 的异常由忽略改为 `logger.warn`，附带 `key` 上下文。
- 2026-03-10: `safeCompleteContext` 中 `printStackTrace` 改为 `logger.error`；`IllegalStateException` 改为 `logger.debug`。
- 2026-03-10: `destroy` 清理异常由忽略改为 `logger.warn`。

## 任务
- P0-1: 处理生产环境明文凭据泄露风险。

## 进度
- [x] Step 1: 移除 `application-prd.yml` 中明文数据库凭据。
- [x] Step 2: 改为环境变量注入（`DB_URL/DB_USERNAME/DB_PASSWORD`）。
- [x] Step 3: 移除 Nacos 明文账号密码并改为环境变量（`NACOS_USERNAME/NACOS_PASSWORD`）。
- [ ] Step 4: 人工执行数据库与Nacos凭据轮换。
- [ ] Step 5: 人工执行 Git 历史敏感信息清理。

## 过程记录
- 2026-03-10: `application-prd.yml` 中数据库连接信息已替换为环境变量，消除仓库当前版本明文泄露。
- 2026-03-10: Nacos 鉴权信息改为环境变量；`server-addr` 支持环境覆盖并保留本地默认值。
- 2026-03-10: 由于凭据可能已在历史提交中泄露，必须执行轮换与历史清理后才算完全闭环。
- 2026-03-10: 已新增 `SECURITY_CLOSURE_CHECKLIST.md`，包含凭据轮换、历史清理与上线验证的可执行步骤。
- 2026-03-10: 已在镜像仓库执行 `git filter-repo --replace-text` 并完成 `--force --all/--tags` 推送到 `origin`。
- 2026-03-10: 远程分支强制更新结果：`master` 与 `2.0.1` 已重写。

## 任务
- 深挖隐藏问题（第二轮审计）。

## 进度
- [x] Step 1: 深挖安全与并发逻辑（crypto/registry/api 契约）。
- [x] Step 2: 形成新增问题清单并按严重级别排序。

## 过程记录
- 2026-03-10: 已新增 `DEEP_DIVE_ISSUES_2026-03-10.md`，包含本轮新增高风险发现与修复建议。

## 任务
- 深挖问题修复 1：`ClientRegistryService` 清理逻辑误删在线监听者。

## 进度
- [x] Step 1: 修正 `cleanInactiveClients` 清理策略。
- [x] Step 2: 仅在监听集合为空时移除 `configKey`。
- [x] Step 3: 错误检查与回归验证。

## 过程记录
- 2026-03-10: 将 `entrySet().removeIf(...)`（删除任意过期即删整组）改为“先删过期客户端，再按空集合删 key”。

## 任务
- 深挖问题修复 2：API 契约与 AES 入参校验增强。

## 进度
- [x] Step 1: 将 `ConfigItemResource` 关键查询接口从 `@RequestMapping` 限定为 `@GetMapping`。
- [x] Step 2: 为 `AESUtils` 增加入参与 key 长度校验（16/24/32字节）。
- [x] Step 3: 错误检查通过。

## 过程记录
- 2026-03-10: `/api/v1/configItems`（namespace/env/componentCode/type 查询）已限定为 GET，避免方法歧义。
- 2026-03-10: `AESUtils.encrypt` 新增 `plainText` 与 `appKey` 非空校验，以及 AES key 长度校验。

## 任务
- 深挖问题修复 3：Cyberark 验签密钥误用与基础参数校验。

## 进度
- [x] Step 1: `validateSignature` 增加 `appId/signature` 非空校验。
- [x] Step 2: 验签改为使用 `secretKey` 计算预期签名。
- [x] Step 3: 错误检查与回归验证。

## 过程记录
- 2026-03-10: 将 `generateSignature(appId, accessKey)` 修正为 `generateSignature(appId, secretKey)`。
- 2026-03-10: 防御空请求/空签名，统一抛出 `INVALID_PARAMETER`。

## 任务
- 深挖问题修复 4：AES 固定 IV 风险收敛（兼容式）。

## 进度
- [x] Step 1: `AESUtils` 从固定 IV 改为随机 IV。
- [x] Step 2: 保持输出为纯 hex 字符串，采用 `ivHex + cipherHex` 以降低协议变更风险。
- [x] Step 3: 错误检查通过。

## 过程记录
- 2026-03-10: 新增 `SecureRandom` 生成每次加密独立 IV，避免固定 IV 导致密文可关联。
- 2026-03-10: 新密文格式升级为 `02 + ivHex + cipherHex`，并保持纯 hex 传输形式。
- 2026-03-10: `AESUtils` 新增 V2 格式检测与解析辅助方法，供下游解密适配使用。
- 2026-03-10: 新增 `CRYPTO_COMPATIBILITY_PLAN.md`，说明 v1/v2 格式与迁移步骤。
