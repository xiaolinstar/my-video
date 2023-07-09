USE `dev_video`;

DROP TABLE IF EXISTS `video_record` ;

CREATE TABLE `video_record` (
                             `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '自增主键',
                             `user_id` bigint unsigned NOT NULL COMMENT '用户id',
                             `video_id` bigint unsigned NOT NULL COMMENT '视频id',
                             `interested` boolean DEFAULT FALSE COMMENT '感兴趣',
                             `watched` boolean DEFAULT FALSE COMMENT '看过',
                             `score`  decimal(3,1) DEFAULT 0 COMMENT '评分',
                             `comment` varchar(300) DEFAULT NULL COMMENT '评论',
                             `deleted` boolean DEFAULT FALSE COMMENT '逻辑删除',
                             `version` bigint unsigned DEFAULT NULL COMMENT '版本号',
                             `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                             `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                             `created_by_user_id` bigint unsigned NOT NULL COMMENT '创建用户id',
                             `updated_by_user_id` bigint unsigned NOT NULL COMMENT '更新用户id',
                             PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户视频记录';
