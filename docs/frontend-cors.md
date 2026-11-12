# 前后端分离与跨域说明

## 部署关系

```text
浏览器
  ↓
Vue 3 + Vite 前端
  ↓  Axios / JSON
Java 21 后端 REST API
  ↓
MySQL
```

开发环境下前端运行在 `http://localhost:5173`，后端运行在 `http://localhost:8080`，两者端口不同，属于不同源，因此浏览器会触发同源策略。

## 开发环境：Vite 代理

在 `vite.config.js` 中配置代理：

```js
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

export default defineConfig({
  plugins: [vue()],
  server: {
    host: '0.0.0.0',
    port: 5173,
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true
      }
    }
  }
})
```

前端统一把 API 基础路径设置为 `/api`，浏览器看到的是同源请求，由 Vite 转发到后端。

## 生产环境：Nginx 反向代理

```nginx
server {
    listen 80;
    server_name example.com;

    location / {
        root /usr/share/nginx/html;
        try_files $uri $uri/ /index.html;
    }

    location /api/ {
        proxy_pass http://backend:8080/api/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    }
}
```

## 直接跨域：后端 CORS 白名单

后端只允许配置中的前端来源。对于带 `Authorization` 头的请求，浏览器会先发送 `OPTIONS` 预检请求。

```java
public Map<String, String> responseHeaders(String origin) {
    if (!isAllowedOrigin(origin)) {
        return Map.of();
    }
    return Map.of(
        "Access-Control-Allow-Origin", origin,
        "Vary", "Origin",
        "Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS",
        "Access-Control-Allow-Headers", "Content-Type, Authorization",
        "Access-Control-Max-Age", "3600"
    );
}
```

预检请求直接返回 204，不进入业务处理；生产环境不要使用 `Access-Control-Allow-Origin: *`，尤其是需要携带凭证时。

## 请求验证

浏览器开发者工具的 Network 中应看到：

```text
OPTIONS /api/v1/textbooks  → 204
GET /api/v1/textbooks      → 200
```

这样既能说明前端和后端是独立部署的，也能说明跨域请求已经按预检规则处理。
