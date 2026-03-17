# 安全收口执行清单（P0-1）

> 目标：完成凭据泄露事件的闭环处置，避免“仅改配置文件但历史仍可恢复敏感信息”的风险。

## 1. 当前状态

- 已完成：`application-prd.yml` 明文凭据移除，改为环境变量。
- 待完成：凭据轮换、历史清理、部署侧验证。

## 2. 立即执行（同日）

1. 冻结相关发布窗口（避免轮换期间服务抖动扩大）。
2. 轮换数据库凭据：
- `DB_USERNAME`
- `DB_PASSWORD`
- 如可行，替换为新账号而非仅改密码。

3. 轮换 Nacos 凭据：
- `NACOS_USERNAME`
- `NACOS_PASSWORD`

4. 在生产环境注入新变量（示例）：
```bash
export DB_URL='jdbc:mysql://<host>:3306/<db>?allowPublicKeyRetrieval=true&useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai&useSSL=false'
export DB_USERNAME='<new_user>'
export DB_PASSWORD='<new_password>'

export NACOS_SERVER_ADDR='<nacos-host>:8848'
export NACOS_USERNAME='<new_nacos_user>'
export NACOS_PASSWORD='<new_nacos_password>'
```

## 3. Git 历史敏感信息清理（必须）

说明：只改当前文件不能清除历史泄露。以下提供两种常见方式，任选一种。

## 方案 A：`git filter-repo`（推荐）

1. 安装工具（macOS）：
```bash
brew install git-filter-repo
```

2. 在镜像仓库中执行（建议先 `--mirror` 克隆一份临时仓库）：
```bash
git clone --mirror <repo-url> repo-mirror.git
cd repo-mirror.git
```

3. 使用 replace-text 清理历史中的敏感字面量（示例）：
```bash
cat > replacements.txt <<'EOF'
literal:520loveTmx@#==>***REDACTED***
literal:jdbc:mysql://115.190.196.117:3306/silence-platform?allowPublicKeyRetrieval=true&useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai&useSSL=false==>***REDACTED_DB_URL***
EOF

git filter-repo --replace-text replacements.txt
```

4. 强推历史（需团队确认）：
```bash
git push --force --all
git push --force --tags
```

## 方案 B：BFG Repo-Cleaner（备选）

1. 下载 BFG 后执行：
```bash
java -jar bfg.jar --replace-text replacements.txt repo-mirror.git
cd repo-mirror.git
git reflog expire --expire=now --all
git gc --prune=now --aggressive
git push --force --all
git push --force --tags
```

## 4. 历史重写后的团队动作

1. 通知所有开发者重新拉取：
- 最安全方式：重新 clone 仓库。

2. 如需保留本地改动，先备份补丁再同步新历史。

3. 在 CI/CD 平台、制品库、日志系统中检索旧密钥痕迹并清理。

## 5. 上线前验证

1. 配置验证：
```bash
rg -n "520loveTmx@#|115\.190\.196\.117|password:\s*[^$]" src/main/resources
```

2. 启动验证：
- 服务可正常读取 `DB_*` / `NACOS_*` 环境变量。
- 健康检查、关键接口（配置查询/发布）可用。

3. 权限验证：
- 旧数据库账号与旧 Nacos 账号应失效。

## 6. 后续加固建议

1. 在 CI 增加 Secret 扫描（如 `gitleaks` 或平台内置 secret scanning）。
2. 约定禁止在 `application-*.yml` 提交任何真实凭据。
3. 可选：将 `application-dev.yml` 的本地默认账号密码也迁移为环境变量，减少误提交风险。

## 7. 说明

- 我已完成仓库代码层面的 P0-1 修复（`application-prd.yml`）。
- “凭据轮换”和“历史重写”属于外部系统与仓库治理动作，需要你或管理员执行。