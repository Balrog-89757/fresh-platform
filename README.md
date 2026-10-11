# 基于 Spring Boot 的生鲜平台销售系统

一个面向生鲜电商场景的全栈销售平台，包含**后台管理端**、**移动端**与**后端服务**三部分，支持商品管理、库存出入库、下单结算、物流跟踪、评价互动，并集成 AI 助手能力。

## 技术栈

| 模块 | 技术 |
| --- | --- |
| 后端 | Spring Boot 3.4.1、Java 17、MyBatis-Plus 3.5.7、Spring Security、JWT (java-jwt) |
| 数据库 / 缓存 | MySQL 8、Redis、Caffeine 本地缓存 |
| AI 能力 | Spring AI 1.0.0（OpenAI 兼容协议，默认对接 DeepSeek） |
| 其它后端组件 | Hutool、Spring Mail、Hibernate Validator |
| 后台管理端 | Vue 2.6、Element UI 2.15、Vue Router、Vuex、Axios、ECharts、WangEditor / Quill |
| 移动端 | uni-app (uvue)，面向 HarmonyOS / 小程序 |

## 目录结构

```
.
├── 数据库/
│   └── db_aps.sql                # 数据库结构脚本
└── 项目源码/
    ├── springboot/               # 后端服务（Maven 工程）
    │   ├── src/main/java/org/example/springboot/
    │   │   ├── controller/       # 接口层
    │   │   ├── service/          # 业务层
    │   │   ├── mapper/           # MyBatis-Plus 数据访问
    │   │   ├── entity/           # 实体
    │   │   ├── ai/               # AI 助手与工具调用
    │   │   └── util/             # 工具类
    │   ├── src/main/resources/   # 配置与 mapper XML
    │   └── files/                # 上传文件存储目录
    ├── vue/                      # 后台管理前端（Vue CLI）
    │   └── src/views/            # 各业务管理页面
    ├── harmony_fresh/            # 移动端（uni-app / uvue）
    │   └── pages/                # index / login / product / cart / order / user ...
    └── READEME.txt               # 原始说明（含默认测试账号）
```

## 功能模块

- **用户与权限**：注册、登录、JWT 鉴权、个人信息、密码找回、角色菜单
- **商品中心**：分类、商品、商品评价、收藏、推荐
- **交易链路**：购物车 → 下单 → 订单管理 → 物流跟踪 → 收货地址
- **库存管理**：入库、出库、库存台账
- **运营内容**：轮播图、文章、公告
- **数据统计**：经营数据看板（ECharts）
- **AI 助手**：基于 Spring AI 的智能问答与工具调用

## 快速开始

### 1. 准备数据库

```bash
mysql -u root -p < 数据库/db_aps.sql
```

### 2. 配置后端

敏感配置通过**环境变量**注入，请勿把真实密钥写进源码：

```bash
# Linux / macOS
export DB_PASSWORD=你的数据库密码
export MAIL_PASSWORD=你的邮箱授权码
export AI_API_KEY=你的 DeepSeek API Key
```

```powershell
# Windows PowerShell
$env:DB_PASSWORD="你的数据库密码"
$env:MAIL_PASSWORD="你的邮箱授权码"
$env:AI_API_KEY="你的 DeepSeek API Key"
```

配置文件位置：`项目源码/springboot/src/main/resources/application.properties`
默认端口：**1234**

### 3. 启动后端

```bash
cd 项目源码/springboot
mvn spring-boot:run
```

### 4. 启动后台管理端

```bash
cd 项目源码/vue
npm install
npm run serve
```

### 5. 启动移动端

用 HBuilderX 打开 `项目源码/harmony_fresh`，运行到 HarmonyOS 模拟器 / 真机或小程序模拟器。

## 默认测试账号

| 角色 | 账号 | 密码 |
| --- | --- | --- |
| 管理员 | Sadmin | 123456 |
| 商户 | farmer | 123456 |
| 用户 | user | 123456 |

> 上述为演示用初始账号，部署到公网前请务必修改。

## 安全说明

仓库已通过 `.gitignore` 排除以下内容，避免泄露：

- `target/`：Maven 构建产物（其中的 `application.properties` 会包含**明文**数据库密码与 API Key）
- `node_modules/`、`dist/`、`unpackage/`：依赖与构建输出
- `.idea/`、`.vscode/`、`*.iml`：IDE 配置

**请勿把真实密钥提交到仓库**，一律使用环境变量占位符（`${DB_PASSWORD:}`、`${AI_API_KEY:}`、`${MAIL_PASSWORD:}`）。

## 说明

- 仓库内的 `项目源码/READEME.txt` 为项目原始说明，保留了其原始编码。
- 上传文件默认保存于 `springboot/files/`，生产环境建议改用对象存储。
