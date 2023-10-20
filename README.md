
<p align="center">
    <img src=https://cdn.pixabay.com/photo/2017/11/25/12/34/hamburg-2976711_640.jpg width=188/>
</p>
<h1 align="center">Fy-API 接口开放平台</h1>
<p align="center"><strong>Fy-API 接口开放平台是一个为用户和开发者提供全面API接口调用服务的平台 🛠</strong></p>
<div align="center">
<a target="_blank" href="https://github.com/grace-fanyu/fy-api">
    <img alt="" src="https://github.com/grace-fanyu/fy-api/badge/star.svg?theme=gvp"/>
</a>
<a target="_blank" href="https://github.com/grace-fanyu/fy-api">
    <img alt="" src="https://img.shields.io/github/stars/grace-fanyu/fy-api.svg?style=social&label=Stars"/>
</a>
    <img alt="Maven" src="https://raster.shields.io/badge/Maven-3.8.1-red.svg"/>
<a target="_blank" href="https://www.oracle.com/technetwork/java/javase/downloads/index.html">
        <img alt="" src="https://img.shields.io/badge/JDK-1.8+-green.svg"/>
</a>
    <img alt="SpringBoot" src="https://raster.shields.io/badge/SpringBoot-2.7+-green.svg"/>
<a href="https://github.com/grace-fanyu/fy-api" target="_blank">
    <img src='https://img.shields.io/github/forks/grace-fanyu/fy-api' alt='GitHub forks' class="no-zoom">
</a>
<a href="https://github.com/grace-fanyu/fy-api" target="_blank"><img src='https://img.shields.io/github/stars/grace-fanyu/fy-api' alt='GitHub stars' class="no-zoom">
</a>
</div>

## 项目介绍 🍧



**🍩 作为用户您可以通过注册登录账户，获取接口调用权限，并根据自己的需求浏览和选择适合的接口。您可以在线进行接口调试，快速验证接口的功能和效果。**

**🧋 作为开发者 我们提供了[客户端SDK: Fy-API-SDK](https://github.com/grace-fanyu/fy-api-sdk)， 通过[开发者凭证]()即可将轻松集成接口到您的项目中，实现更高效的开发和调用。**

**🍆 您可以将自己的接口接入到Fy-API 接口开放平台平台上，并发布给其他用户使用。 您可以管理和各个接口，以便更好地分析和优化接口性能。**

**👌 我们还提供了[开发者在线文档]()和技术支持，帮助您快速接入和发布接口。**

**🏁 无论您是用户还是开发者，Fy-API 接口开放平台都致力于提供稳定、安全、高效的接口调用服务，帮助您实现更快速、便捷的开发和调用体验。**

## 网站导航 🍓

- [**Fy-API 后端 🍚**](https://github.com/grace-fanyu/fy-api)
- [**Fy-API 前端 🍭**](https://github.com/grace-fanyu/fy-api-frontend)

-  **[Fy-API-SDK 🛠️](https://github.com/grace-fanyu/fy-api-sdk)** 

-  **[Fy-API 接口开放平台 🔗]()**

-  **[Fy-API-DOC 开发者文档 📖]()**


## 目录结构 🍎


| 目录                                                             | 描述           |
|----------------------------------------------------------------|--------------|
| **🥪 [fanyui-backend](./fanyui-backend)**                      | Fy-API后端服务模块 |
| **😊 [fanyui-common](./fanyui-common)**                        | 公共服务模块       |
| **🕸️ [fanyui-gateway](./fanyui-gateway)**                     | 网关模块         |
| **🔗 [fanyui-interface](./fanyui-interface)**                  | 接口模块         |
| **🛠 [fy-qpi-sdk](https://github.com/grace-fanyu/fy-api-sdk)** | 开发者调用sdk     |
| **📗 [fy-api-doc]()**                                          | 接口在线文档       |

## 项目流程 🍉

![Fy-API 接口开放平台]()


## 项目选型 🥩

### **后端**

- Spring Boot 2.7.0
- Spring MVC
- MySQL 数据库
- 腾讯云COS存储
- Dubbo 分布式（RPC、Nacos）
- Spring Cloud Gateway 微服务网关
- API 签名认证（Http 调用）
- IJPay-AliPay  支付宝支付
- WeiXin-Java-Pay  微信支付
- Swagger + Knife4j 接口文档
- Spring Boot Starter（SDK 开发）
- commons-email 邮箱通知、验证码
- Spring Session Redis 分布式登录
- Apache Commons Lang3 工具类
- MyBatis-Plus 及 MyBatis X 自动生成
- Hutool、Apache Common Utils、Gson 等工具库

### 前端

- React 18

- Ant Design Pro 5.x 脚手架

- Ant Design & Procomponents 组件库

- Umi 4 前端框架

- OpenAPI 前端代码生成



## 快速启动 🚀

### 前端

环境要求：Node.js >= 16

安装依赖：

```bash
yarn or  npm install
```

启动：

```bash
yarn run dev or npm run start:dev
```

部署：

```bash
yarn build or npm run build
```

### 后端

执行sql目录下fyapi.sql



## 功能介绍 📋

✨星琼和💎钻石，用于平台购买接口调用次数。

| **功能**                                                    | **普通用户** | **管理员** |
|-----------------------------------------------------------|-----|-----|
| [**Fy-API-SDK**](https://github.com/grace-fanyu/fy-api-sdk)使用 | ✅ |     ✅      |
| **[开发者API在线文档]()**                    | ✅ |     ✅      |
| 切换主题、深色、暗色                                                | ✅ | ✅ | ✅ |
| 微信支付宝付款                                                   | ✅ | ✅ |
| 在线调试接口                                                    | ✅ | ✅ |
| 每日签到得✨星琼                                                  | ✅ | ✅ |
| 接口大厅搜索接口、浏览接口                                             | ❌ | ✅ |
| 邮箱验证码登录注册                                                 | ✅ | ✅ |
| 💎钻石充值                                                    | ✅ | ✅ |
| 支付成功邮箱通知(需要绑定邮箱)                                          | ❌ | ✅ | ✅ |
| 更新头像                                                      | ✅ | ✅ |
| 绑定、换绑、解绑邮箱                                                | ✅ | ✅ |
| 取消订单、删除订单                                                 | ✅ | ✅ |
| 商品管理、上线、下架                                                | ❌ |✅|
| 用户管理、封号解封等                                                | ❌ | ✅ |
| 接口管理、接口发布审核、下架                                            | ❌ | ✅ |
| 退款                                                        | ❌| ❌ |

## 功能展示 ✨
