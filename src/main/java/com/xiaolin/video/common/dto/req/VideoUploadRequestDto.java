package com.xiaolin.video.common.dto.req;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * @author xingxiaolin xlxing@bupt.edu.cn
 * @Description
 * @create 2023/7/2
 */
@Data
@NoArgsConstructor
public class VideoUploadRequestDto {
    private String name;
    private String icon;
    private Integer type;
    private String district;
    private Integer year;
    private String tag;
    private String director;
    private String resource;
    private String scriptWriter;
    private String leadingActor;
    private String language;
    private LocalDate releaseDate;
    private Integer duration;
    private String alias;
    private String description;
}
