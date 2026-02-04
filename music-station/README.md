# 音乐台系统 (Mono-Repo)

## 1. 建库与初始化

1. 确保本地已安装 MySQL 8。
2. 执行后端 `schema.sql`：

```bash
mysql -u root -p < backend/src/main/resources/schema.sql
```

## 2. 启动后端

1. 修改 `backend/src/main/resources/application.yml` 中的数据库账号密码与音乐扫描目录 `music.scan-path`。
2. 在 `backend` 目录启动：

```bash
cd backend
mvn spring-boot:run
```

## 3. 启动前端

1. 在 `frontend` 目录安装依赖并启动：

```bash
cd frontend
npm install
npm run dev
```

## 4. 主要功能

- 用户注册 / 登录（JWT）
- 在线歌曲播放（HTTP 流式）
- 歌单创建、添加歌曲、移除歌曲
- 管理员扫描本地音乐目录、启用/禁用歌曲
