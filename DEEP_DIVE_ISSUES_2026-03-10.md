# 深挖问题清单（新增）

> 时间：2026-03-10
> 范围：基于已修复项之外的隐藏问题审计

## Findings

| Severity | 模块 | 问题 | 证据 | 影响 | 建议 |
|---|---|---|---|---|---|
| High | 客户端注册/心跳清理 | `cleanInactiveClients` 会在删除任意过期客户端后把整组监听者一起删掉 | `src/main/java/com/old/silence/config/center/domain/service/ClientRegistryService.java:114` `src/main/java/com/old/silence/config/center/domain/service/ClientRegistryService.java:115` | 可能误删仍在线的客户端监听，造成配置通知丢失 | 调整为“先移除过期客户端，再仅在集合为空时删除 key” |
| High | Cyberark 验签 | 验签使用 `accessKey` 参与签名计算，且算法为 `SHA1(appId&appKey)`，缺少时间戳/nonce 抗重放 | `src/main/java/com/old/silence/config/center/domain/service/ConfigCyberarkInfoService.java:55` `src/main/java/com/old/silence/config/center/domain/service/ConfigCyberarkInfoService.java:68` | 可被重放或伪造，安全性明显不足 | 按统一签名体系升级（建议 HMAC-SHA256 + timestamp + nonce + 过期校验） |
| Medium | 对称加密 | `AES/CBC` 使用固定 IV，导致同明文可重复产生同密文模式 | `src/main/java/com/old/silence/config/center/util/AESUtils.java:17` `src/main/java/com/old/silence/config/center/util/AESUtils.java:18` | 泄露明文模式特征，降低加密安全性 | 改为随机 IV（每条数据独立），与密文一起存储 |
| Medium | 对称加密 | `appKey` 直接转字节构造密钥，未做长度与格式校验 | `src/main/java/com/old/silence/config/center/util/AESUtils.java:27` | 运行时可能因密钥长度异常失败，行为不可预测 | 入参校验（16/24/32 字节）或统一 KDF 派生 |
| Medium | API 契约 | 关键查询接口使用 `@RequestMapping` 未限制 HTTP 方法 | `src/main/java/com/old/silence/config/center/api/ConfigItemResource.java:60` | 该接口可能被非预期 HTTP 方法调用，策略面更难治理 | 明确改为 `@GetMapping` 或 `@PostMapping` |
| Low | 响应签名语义 | Cyberark 响应中直接回传请求签名而非服务端响应签名 | `src/main/java/com/old/silence/config/center/domain/service/ConfigCyberarkInfoService.java:95` | 客户端无法基于响应签名做完整性校验（取决于协议） | 若协议要求双向签名，增加响应签名字段计算 |

## 建议优先级（新增）

1. `High` 先修 `ClientRegistryService` 清理逻辑误删问题（功能正确性）
2. `High` 再修 Cyberark 验签体系（安全性）
3. `Medium` 收敛 AES 固定 IV 与 key 长度校验
4. `Medium/Low` 统一 API 契约与响应签名语义
