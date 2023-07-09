USE `dev_video`;

DROP TABLE IF EXISTS `video` ;

CREATE TABLE `video` (
                             `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '自增主键',
                             `name` varchar(50) NOT NULL COMMENT '视频名',
                             `icon` varchar(50) NOT NULL COMMENT '封面图',
                             `rating` decimal(3, 1) DEFAULT 0 COMMENT '评分',
                             `type` int NOT NULL COMMENT '视频分类',
                             `district` varchar(50) NOT NULL COMMENT '地区',
                             `year` int NOT NULL COMMENT '上映年份',
                             `tag` varchar(50) NOT NULL COMMENT '标签',
                             `deleted` boolean DEFAULT FALSE COMMENT '逻辑删除',
                             `version` bigint unsigned DEFAULT NULL COMMENT '版本号',
                             `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                             `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                             `created_by_user_id` bigint unsigned DEFAULT NULL COMMENT '创建用户id',
                             `updated_by_user_id` bigint unsigned DEFAULT NULL COMMENT '更新用户id',
                             PRIMARY KEY (`id`),
                             UNIQUE KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='视频基本信息';
