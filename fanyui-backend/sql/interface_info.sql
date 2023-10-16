-- 创建库
create database if not exists fyapi;

-- 切换库
use fyapi;

-- 用户表
create table if not exists user
(
    `id`           bigint auto_increment comment 'id' primary key,
    `userAccount`  varchar(256)                           null comment '账号',
    `userPassword` varchar(512)                           null comment '密码',
    `email`        varchar(256)                           null comment '邮箱',
    `unionId`      varchar(256)                           null comment '微信开放平台id',
    `mpOpenId`     varchar(256)                           null comment '公众号openId',
    `userName`     varchar(256)                           null comment '用户昵称',
    `userAvatar`   varchar(1024)                          null comment '用户头像',
    `userProfile`  varchar(512)                           null comment '用户简介',
    `userRole`     varchar(256) default 'user'            not null comment '用户角色：user/admin/ban',
    `userStar`     bigint       default 0                 not null comment '星琼',
    `userDiamond`  bigint       default 0                 not null comment '钻石',
    `accessKey`    varchar(512)                           not null comment 'accessKey',
    `secretKey`    varchar(512)                           not null comment 'secretKey',
    `createTime`   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    `updateTime`   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    `isDelete`     tinyint      default 0                 not null comment '是否删除',
    index idx_unionId (unionId)
) comment '用户' collate = utf8mb4_unicode_ci;
INSERT INTO user (id, userAccount, userPassword, unionId, mpOpenId, userName, userAvatar, userProfile, userRole,
                  accessKey, secretKey, createTime, updateTime, isDelete)
VALUES (1709891310753886209, 'fanyu', 'cb1a274fbb3cd987568b8a63ac987e5a', null, null, '凡雨',
        'https://cdn.pixabay.com/photo/2017/11/25/12/34/hamburg-2976711_640.jpg', null, 'admin',
        'a5f2056edf0a95db8e24b1205636104b', '5c1983b7d28fc028c09d165a2ccd7108', '2023-10-05 11:20:31',
        '2023-10-05 11:21:53', 0);

-- 接口信息表
create table if not exists `interface_info`
(
    `id`             bigint                             not null auto_increment comment '接口id' primary key,
    `name`           varchar(256)                       not null comment '接口名称',
    `description`    varchar(256)                       null comment '接口描述',

    `url`            varchar(512)                       not null comment '接口地址',
    `requestParams`  text                               not null comment '请求参数',
    `requestHeader`  text                               null comment '请求头',
    `responseHeader` text                               null comment '响应头',
    `status`         int      default 0                 not null comment '接口状态（0-关闭，1-开启）',
    `method`         varchar(256)                       not null comment '请求类型',

    `priceStar`      bigint   default 10                not null comment '星琼/100次',
    `priceDiamond`   bigint   default 1                 not null comment '钻石/100次',
    `userId`         bigint                             not null comment '创建人',
    `createTime`     datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    `updateTime`     datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    `isDelete`       tinyint  default 0                 not null comment '是否删除（0-未删，1-已删）'
) comment '接口信息';
insert into `interface_info` (`name`, `description`, `url`, `requestParams`, `requestHeader`, `responseHeader`,
                              `status`, `method`, `userId`)
values ('许擎宇', '薛聪健', 'www.cary-king.net', 'www.cary-king.net', '潘博涛', '谭聪健', 0, '石炫明', 9500534531);
insert into `interface_info` (`name`, `description`, `url`, `requestParams`, `requestHeader`, `responseHeader`,
                              `status`, `method`, `userId`)
values ('陆弘文', '白志强', 'www.leslee-kuhn.net', 'www.cary-king.net', '潘懿轩', '马鸿涛', 0, '陈峻熙', 3982575846);
insert into `interface_info` (`name`, `description`, `url`, `requestParams`, `requestHeader`, `responseHeader`,
                              `status`, `method`, `userId`)
values ('毛建辉', '罗文', 'www.rosaria-kilback.io', 'www.cary-king.net', '冯子默', '彭哲瀚', 0, '赵远航', 121776355);
insert into `interface_info` (`name`, `description`, `url`, `requestParams`, `requestHeader`, `responseHeader`,
                              `status`, `method`, `userId`)
values ('彭雨泽', '蔡煜祺', 'www.norris-bergstrom.biz', 'www.cary-king.net', '董思源', '田晓博', 0, '潘擎宇', 740);
insert into `interface_info` (`name`, `description`, `url`, `requestParams`, `requestHeader`, `responseHeader`,
                              `status`, `method`, `userId`)
values ('傅志强', '陈梓晨', 'www.jordan-reinger.com', 'www.cary-king.net', '金志强', '熊锦程', 0, '邓睿渊', 35542559);
insert into `interface_info` (`name`, `description`, `url`, `requestParams`, `requestHeader`, `responseHeader`,
                              `status`, `method`, `userId`)
values ('吕黎昕', '孔越彬', 'www.fe-okon.info', 'www.cary-king.net', '万伟宸', '林昊然', 0, '孟荣轩', 1445);
insert into `interface_info` (`name`, `description`, `url`, `requestParams`, `requestHeader`, `responseHeader`,
                              `status`, `method`, `userId`)
values ('夏雪松', '许子骞', 'www.lashawna-legros.co', 'www.cary-king.net', '蔡昊然', '胡鹏涛', 0, '钟立辉', 34075514);
insert into `interface_info` (`name`, `description`, `url`, `requestParams`, `requestHeader`, `responseHeader`,
                              `status`, `method`, `userId`)
values ('严钰轩', '阎志泽', 'www.kay-funk.biz', 'www.cary-king.net', '莫皓轩', '郭黎昕', 0, '龚天宇', 70956);
insert into `interface_info` (`name`, `description`, `url`, `requestParams`, `requestHeader`, `responseHeader`,
                              `status`, `method`, `userId`)
values ('萧嘉懿', '曹熠彤', 'www.margarette-lindgren.biz', 'www.cary-king.net', '田泽洋', '邓睿渊', 0, '梁志强', 98);
insert into `interface_info` (`name`, `description`, `url`, `requestParams`, `requestHeader`, `responseHeader`,
                              `status`, `method`, `userId`)
values ('杜驰', '冯思源', 'www.vashti-auer.org', 'www.cary-king.net', '黎健柏', '武博文', 0, '李伟宸', 9);
insert into `interface_info` (`name`, `description`, `url`, `requestParams`, `requestHeader`, `responseHeader`,
                              `status`, `method`, `userId`)
values ('史金鑫', '蔡鹏涛', 'www.diann-keebler.org', 'www.cary-king.net', '徐烨霖', '阎建辉', 0, '李烨伟', 125);
insert into `interface_info` (`name`, `description`, `url`, `requestParams`, `requestHeader`, `responseHeader`,
                              `status`, `method`, `userId`)
values ('林炫明', '贾旭尧', 'www.dotty-kuvalis.io', 'www.cary-king.net', '梁雨泽', '龙伟泽', 0, '许智渊', 79998);
insert into `interface_info` (`name`, `description`, `url`, `requestParams`, `requestHeader`, `responseHeader`,
                              `status`, `method`, `userId`)
values ('何钰轩', '赖智宸', 'www.andy-adams.net', 'www.cary-king.net', '崔思淼', '白鸿煊', 0, '邵振家', 7167482751);
insert into `interface_info` (`name`, `description`, `url`, `requestParams`, `requestHeader`, `responseHeader`,
                              `status`, `method`, `userId`)
values ('魏志强', '于立诚', 'www.ione-aufderhar.biz', 'www.cary-king.net', '朱懿轩', '万智渊', 0, '唐昊强', 741098);
insert into `interface_info` (`name`, `description`, `url`, `requestParams`, `requestHeader`, `responseHeader`,
                              `status`, `method`, `userId`)
values ('严君浩', '金胤祥', 'www.duane-boyle.org', 'www.cary-king.net', '雷昊焱', '侯思聪', 0, '郝思', 580514);
insert into `interface_info` (`name`, `description`, `url`, `requestParams`, `requestHeader`, `responseHeader`,
                              `status`, `method`, `userId`)
values ('姚皓轩', '金鹏', 'www.lyda-klein.biz', 'www.cary-king.net', '杜昊强', '邵志泽', 0, '冯鸿涛', 6546);
insert into `interface_info` (`name`, `description`, `url`, `requestParams`, `requestHeader`, `responseHeader`,
                              `status`, `method`, `userId`)
values ('廖驰', '沈泽洋', 'www.consuelo-sipes.info', 'www.cary-king.net', '彭昊然', '邓耀杰', 0, '周彬', 7761037);
insert into `interface_info` (`name`, `description`, `url`, `requestParams`, `requestHeader`, `responseHeader`,
                              `status`, `method`, `userId`)
values ('赖智渊', '邓志泽', 'www.emerson-mann.co', 'www.cary-king.net', '熊明哲', '贺哲瀚', 0, '田鹏', 381422);
insert into `interface_info` (`name`, `description`, `url`, `requestParams`, `requestHeader`, `responseHeader`,
                              `status`, `method`, `userId`)
values ('许涛', '陆致远', 'www.vella-ankunding.name', 'www.cary-king.net', '贾哲瀚', '莫昊焱', 0, '袁越彬', 4218096);
insert into `interface_info` (`name`, `description`, `url`, `requestParams`, `requestHeader`, `responseHeader`,
                              `status`, `method`, `userId`)
values ('吕峻熙', '沈鹏飞', 'www.shari-reichel.org', 'www.cary-king.net', '郭鸿煊', '覃烨霖', 0, '熊黎昕', 493);


-- 用户接口调用信息表
create table if not exists `user_interface_info`
(
    `id`              bigint                             not null auto_increment comment '主键' primary key,
    `userId`          bigint                             not null comment '调用用户 id',
    `interfaceInfoId` bigint                             not null comment '接口 id',
    `totalNum`        int      default 0                 not null comment '总调用次数',
    `leftNum`         int      default 0                 not null comment '剩余调用次数',
    `status`          int      default 0                 not null comment '0-正常，1-禁用',
    `createTime`      datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    `updateTime`      datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    `isDelete`        tinyint  default 0                 not null comment '是否删除(0-未删, 1-已删)'
) comment '用户接口调用信息表';

-- 用户订单信息表
create table if not exists `order`
(
    `id`              bigint                             not null auto_increment comment '主键' primary key,
    `userId`          bigint                             not null comment '调用用户 id',
    `interfaceInfoId` bigint                             not null comment '接口 id',
    `totalNum`        int      default 0                 not null comment '购买的调用次数',
    `priceStar`       bigint   default 0                 not null comment '花费的星琼',
    `priceDiamond`    bigint   default 0                 not null comment '花费的钻石',
    `status`          int      default 0                 not null comment '0-未支付，1-已支付',
    `createTime`      datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    `updateTime`      datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    `isDelete`        tinyint  default 0                 not null comment '是否删除(0-未删, 1-已删)'
) comment '用户订单信息表';
-- 基本设置信息表
create table if not exists `settings`
(
    `id`          bigint                             not null auto_increment comment '主键' primary key,
    `dailyStar`   bigint                             not null comment '每日签到获取星琼数量',
    `starDiamond` bigint                             not null comment '星琼与钻石兑换比例：钻石=？星琼',
    `starImg`     varchar(512)                       not null comment '星琼默认图片',
    `diamondImg`  varchar(512)                       not null comment '钻石默认图片',
    `userImg`     varchar(512)                       not null comment '用户默认图片',
    `updateTime`  datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    `isDelete`    tinyint  default 0                 not null comment '是否删除(0-未删, 1-已删)'
) comment '基本设置信息表';
INSERT INTO fyapi.settings (id, dailyStar, starDiamond, starImg, diamondImg, userImg, updateTime, isDelete) VALUES (1, 100, 10, 'https://upload-bbs.miyoushe.com/upload/2023/10/11/80823548/77bf8de438cf33605ed621a124cb4685_4075675081832803657.jpeg', 'https://cdn.pixabay.com/photo/2014/08/14/11/15/diamond-417896_640.png', 'https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_640.png', '2023-10-13 13:20:44', 0);

