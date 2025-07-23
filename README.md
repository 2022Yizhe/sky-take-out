# 餐厅线上点餐系统

![项目logo](https://web-yizhe-sky.oss-cn-beijing.aliyuncs.com/%E7%BA%BF%E4%B8%8A%E7%82%B9%E9%A4%90%E7%B3%BB%E7%BB%9F-logo-mini.png)

## 项目简介

此线上点餐系统是一个基于Spring Boot的全栈外卖订餐管理系统，为餐饮企业提供从用户下单到订单配送的完整解决方案。

系统包含用户端、管理员端，实现了餐厅的手机点餐业务的核心功能流程。

## 技术栈

### 后端技术
- **核心框架**: Spring Boot 2.7.3
- **持久层**: MyBatis 2.2.0
- **数据库**: MySQL 8.0.+
- **缓存**: Redis 3.2.100
- **安全框架**: Spring Security + JWT
- **文档**: Swagger/Knife4j

### 前端技术
- **用户端**: 微信小程序
- **管理端**: Vue 3 + Element
- **商家端**: React 18

## 功能模块

### 用户端功能
- 用户注册/登录（微信授权登录）
- 餐厅浏览与搜索
- 菜品展示与分类
- 购物车管理
- 订单创建与支付（微信支付集成）


### 管理端功能
- 系统用户管理
- 菜单管理（菜品上架/下架）
- 订单管理

## 项目结构

```
sky-takeout/
├── sky-common          # 公共模块
├── sky-pojo            # 实体类模块
├── sky-server          # 核心业务模块

**/resources/           # 配置文件
```

## 快速开始

### 环境准备
略

### 部署步骤

1. **数据库初始化**
```bash
mysql -u root -p < sql/sky_takeout.sql
```

2. **配置修改**
   编辑 `application-dev.yml` 文件，配置数据库、Redis等连接信息

3. **启动服务**
```bash
mvn clean package
java -jar sky-server/target/sky-server.jar
```

4. **访问接口文档**
```
http://localhost:8080/doc.html
```

## 项目截图

### 用户端示意图

![用户端](https://web-yizhe-sky.oss-cn-beijing.aliyuncs.com/%E7%94%A8%E6%88%B7%E7%AB%AF.png)

### 管理端示意图

![管理端](https://web-yizhe-sky.oss-cn-beijing.aliyuncs.com/%E7%AE%A1%E7%90%86%E7%AB%AF.png)

