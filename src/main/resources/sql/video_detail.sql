USE `dev_video`;

DROP TABLE IF EXISTS `video_detail` ;

CREATE TABLE `video_detail` (
                             `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '自增主键',
                             `director` varchar(100) NOT NULL COMMENT '导演',
                             `resource` varchar(100) NOT NULL COMMENT '资源地址',
                             `script_writer` varchar(100) NOT NULL COMMENT '编剧',
                             `leading_actor` varchar(200) NOT NULL COMMENT '主演',
                             `language` varchar(50) NOT NULL COMMENT '语言类型',
                             `release_date` datetime NOT NULL COMMENT '发布日期',
                             `duration` int NOT NULL COMMENT '时长',
                             `alias` varchar(100) DEFAULT NULL COMMENT '别名',
                             `description` varchar(300) DEFAULT NULL COMMENT '视频简介',
                             `deleted` boolean DEFAULT FALSE COMMENT '逻辑删除',
                             `version` bigint unsigned DEFAULT NULL COMMENT '版本号',
                             `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                             `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                             `created_by_user_id` bigint unsigned DEFAULT NULL COMMENT '创建用户id',
                             `updated_by_user_id` bigint unsigned DEFAULT NULL COMMENT '更新用户id',
                             PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='视频详情';
