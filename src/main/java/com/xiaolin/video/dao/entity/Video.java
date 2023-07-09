package com.xiaolin.video.dao.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.annotations.Delete;

/**
 * 视频基本信息
 * @author xlxing
 * @TableName video
 */
@TableName(value ="video")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Video implements Serializable {
    /**
     * 自增主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 视频名
     */
    private String name;

    /**
     * 封面图
     */
    private String icon;

    /**
     * 评分
     */
    private BigDecimal rating;

    /**
     * 视频分类
     */
    private Integer type;

    /**
     * 地区
     */
    private String district;

    /**
     * 上映年份
     */
    private Integer year;

    /**
     * 标签
     */
    private String tag;

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