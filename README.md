# 校园二手教材交易平台

这是一个面向校园的二手教材交易平台，采用前后端分离和分层单体的思路。

## 角色

- 学生用户：可以发布教材、搜索教材、购买教材和评价买家/卖家
- 管理员：审核教材、管理用户、处理举报

## 子系统

1. 用户与权限子系统
2. 教材与检索子系统
3. 交易与订单子系统
4. 运营管理子系统

## 后端结构

```text
com.example.textbook
├── application       业务接口与实现
├── domain            领域模型与仓库抽象
└── infrastructure    仓储实现与 Web 基础设施
```

后端通过 `ITextbookRepository` 将业务层与具体存储解耦，当前提供内存实现，后续可以替换为数据库实现。

## 构建

```powershell
mvn clean install
```

## 说明

前端方案采用 Vue 3 + Vite，前端开发服务器通过 Vite 代理访问后端，生产环境由 Nginx 统一托管静态资源并反向代理 `/api`。跨域处理说明见 `docs/frontend-cors.md`。
