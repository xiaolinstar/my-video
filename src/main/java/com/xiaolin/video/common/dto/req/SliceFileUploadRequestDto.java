package com.xiaolin.video.common.dto.req;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author xingxiaolin xlxing@bupt.edu.cn
 * @Description 文件分片上传请求数据传输对象
 * @create 2023/7/8
 */
@Data
@NoArgsConstructor
public class SliceFileUploadRequestDto {
    /**
     * 传输文件体
     */
    private MultipartFile file;
    /**
     * 文件块号
     */
    private Integer chunk;
    /**
     * 文件总的分块数
     */
    private Integer chunks;
    /**
     * 文件名，表示最初传输的文件名
     */
    private String name;

    /**
     * 文件的md5值
     */
    @NotNull
    private String md5;

}
