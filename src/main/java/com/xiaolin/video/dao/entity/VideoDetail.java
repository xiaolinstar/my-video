package com.xiaolin.video.dao.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 视频详情
 * @author xlxing
 * @TableName video_detail
 */
@TableName(value ="video_detail")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VideoDetail implements Serializable {
    /**
     * 自增主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 导演
     */
    private String director;

    /**
     * 资源地址
     */
    private String resource;

    /**
     * 编剧
     */
    private String scriptWriter;

    /**
     * 主演
     */
    private String leadingActor;

    /**
     * 语言类型
     */
    private String language;

    /**
     * 发布日期
     */
    private LocalDate releaseDate;

    /**
     * 时长
     */
    private Integer duration;

    /**
     * 别名
     */
    private String alias;

    /**
     * 视频简介
     */
    private String description;

    /**
     * 逻辑删除
     */
    private Integer deleted;

    /**
     * 版本号
     */
    @Version
    private Long version;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 创建用户id
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createdByUserId;

    /**
     * 更新用户id
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updatedByUserId;

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}