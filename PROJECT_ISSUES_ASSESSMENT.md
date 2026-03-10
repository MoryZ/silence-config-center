# 项目问题评估清单（按功能与优先级）

> 评估日期：2026-03-10
> 适用仓库：`silence-config-center`
> 说明：本清单用于决策“先修什么”，不包含代码修改。

## 1. 问题总览（按优先级）

| ID | 优先级 | 功能模块 | 问题 | 风险摘要 |
|---|---|---|---|---|
| P0-1 | P0 | 配置与安全 | 生产数据库凭据明文提交 | 凭据泄露可导致数据库被直接访问 |
| P1-1 | P1 | Cyberark 密码查询 | DAO SQL 字段与实体不匹配 | 可能查到错误字段，返回空/错密码 |
| P1-2 | P1 | 配置同步逻辑 | `findByConfigEnvironmentIdAndNamespaceId` 漏 `namespaceId` 条件 | 同环境多命名空间场景可能更新错数据 |
| P1-3 | P1 | 签名认证 | Cyberark 验签使用 `accessKey` 而非 `secretKey`（待协议确认） | 验签强度和正确性风险 |
| P1-4 | P1 | 日志安全 | 认证日志打印 `nonce/signature` 明文 | 日志泄露后可被利用进行攻击辅助 |
| P2-1 | P2 | 长轮询 | 单 key 只存一个 `AsyncContext`，后订阅覆盖先订阅 | 多客户端并发订阅漏通知 |
| P2-2 | P2 | 异常处理 | 吞异常与 `printStackTrace` | 问题难追踪，线上可观测性差 |
| P2-3 | P2 | 配置内容更新 | `findById(id).getContent()` 未判空 | 不存在 ID 时可能 NPE |
| P3-1 | P3 | 代码整洁性 | 未使用 import/方法 | 可维护性下降，噪音增加 |

---

## 2. 按功能模块拆分

### 2.1 配置与安全

#### P0-1 生产凭据明文提交（P0）
- 证据：`src/main/resources/application-prd.yml:5`
- 证据：`src/main/resources/application-prd.yml:6`
- 问题：`url/username/password` 为真实值，且含公网地址。
- 影响：凭据泄露、越权访问、合规风险。
- 建议：
  - 立即轮换数据库账号和密码。
  - 改为环境变量或密钥服务（Vault/KMS）。
  - 清理 Git 历史敏感信息（仅改文件不足以消除泄露痕迹）。

#### P1-4 认证日志泄露敏感信息（P1）
- 证据：`src/main/java/com/old/silence/config/center/api/config/SignatureAuthInterceptor.java:137`
- 问题：日志打印 `accessKey/timestamp/nonce/signature`。
- 影响：重放/攻击分析辅助信息暴露。
- 建议：日志脱敏，仅保留必要可观测字段（如 requestId、部分掩码）。

---

### 2.2 Cyberark 密码查询与验签

#### P1-1 DAO 字段映射疑似错误（P1）
- 证据：`src/main/java/com/old/silence/config/center/infrastructure/persistence/dao/ConfigCyberarkInfoDao.java:13`
- 问题：SQL 查询 `access_key,secret_key`，返回类型却是 `ConfigCyberarkInfo`（主要业务字段是 `encryptedValue` 等）。
- 影响：`encryptedValue` 可能为空，导致密码查询错误。
- 建议：按业务实体改为查询 `encrypted_value` 等正确列，并补充 DAO 单测。

#### P1-3 验签字段使用待确认（P1）
- 证据：`src/main/java/com/old/silence/config/center/domain/service/ConfigCyberarkInfoService.java:54`
- 问题：`generateSignature(appId, accessKey)` 似乎使用了 `accessKey` 作为签名密钥。
- 影响：若协议期望 `secretKey`，则会导致验签逻辑错误或安全性下降。
- 建议：与调用方协议对齐后修正并补充正反例测试。

---

### 2.3 配置项同步与发布逻辑

#### P1-2 查找条件缺失（P1）
- 证据：`src/main/java/com/old/silence/config/center/infrastructure/persistence/ConfigItemMyBatisRepository.java:46`
- 证据：`src/main/java/com/old/silence/config/center/domain/repository/ConfigItemRepository.java:24`
- 问题：方法名要求按 `configEnvironmentId + namespaceId` 查询，实际只按 `configEnvironmentId`。
- 影响：多 namespace 环境下可能取错数据，影响 sync/overwrite 正确性。
- 建议：补上 `namespaceId` 条件并增加覆盖测试。

#### P2-3 更新内容空指针风险（P2）
- 证据：`src/main/java/com/old/silence/config/center/infrastructure/persistence/ConfigItemMyBatisRepository.java:95`
- 问题：直接 `findById(id).getContent()` 无判空。
- 影响：ID 不存在时 NPE，错误语义不一致。
- 建议：判空并抛统一业务异常（如 `DATA_NOT_EXIST`）。

---

### 2.4 长轮询与稳定性

#### P2-1 订阅覆盖问题（P2）
- 证据：`src/main/java/com/old/silence/config/center/domain/service/LongPollingService.java:29`
- 证据：`src/main/java/com/old/silence/config/center/domain/service/LongPollingService.java:80`
- 问题：`Map<String, AsyncContext>` 每个配置 key 只能存一个 context。
- 影响：并发订阅时后来的请求覆盖先前请求，导致漏通知。
- 建议：改为 `Map<String, List<AsyncContext>>` 或并发集合并广播通知。

#### P2-2 吞异常与 `printStackTrace`（P2）
- 证据：`src/main/java/com/old/silence/config/center/domain/service/LongPollingService.java:62`
- 证据：`src/main/java/com/old/silence/config/center/domain/service/LongPollingService.java:127`
- 证据：`src/main/java/com/old/silence/config/center/domain/service/LongPollingService.java:144`
- 问题：空 catch/忽略异常/直接打印堆栈。
- 影响：线上问题不可观测，排障成本高。
- 建议：统一 `logger.error(..., e)` 并附带业务上下文。

---

### 2.5 代码整洁性

#### P3-1 未使用代码（P3）
- 证据：`src/main/java/com/old/silence/config/center/api/ConfigItemResource.java:19`
- 证据：`src/main/java/com/old/silence/config/center/api/ConfigItemResource.java:35`
- 证据：`src/main/java/com/old/silence/config/center/domain/service/SignatureService.java:74`
- 问题：未使用 import/方法。
- 影响：可读性与维护体验下降。
- 建议：清理并接入静态检查规则。

---

## 3. 四象限优先矩阵（影响 x 修复成本）

## Q1: 高影响 + 低成本（优先立即处理）
- P1-4 认证日志敏感信息脱敏。
- P1-2 补齐 `namespaceId` 查询条件。
- P2-3 `updateContentById` 判空并抛统一异常。
- P3-1 清理未使用代码。

## Q2: 高影响 + 高成本（尽快排期）
- P0-1 凭据轮换 + 配置改造 + 历史清理。
- P1-1 Cyberark DAO 字段映射修正与联调验证。
- P2-1 长轮询多订阅者模型重构（涉及并发与通知语义）。

## Q3: 低影响 + 低成本（顺手处理）
- P2-2 局部异常处理标准化（可分批处理）。

## Q4: 低影响 + 高成本（暂缓）
- 当前无明显项。

---

## 4. 推荐修复顺序（可直接用于迭代拆分）

### 批次 A（当天可完成）
1. P1-4 日志脱敏
2. P1-2 补查询条件
3. P2-3 空判与统一异常
4. P3-1 清理无用代码

### 批次 B（1-2 天）
1. P1-1 修 DAO 查询字段与映射
2. P1-3 与协议方确认并修验签字段
3. P2-2 异常处理规范化

### 批次 C（2-3 天）
1. P2-1 长轮询多订阅者架构改造
2. 回归压测与并发验证

### 批次 S（安全专项，优先级最高）
1. P0-1 立即轮换生产凭据
2. 移除仓库明文敏感配置
3. 评估并执行历史泄露清理
