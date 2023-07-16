package com.xiaolin.video.common.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author xingxiaolin xlxing@bupt.edu.cn
 * @Description 文件上传Dto，文件上传成功后返回给前端的数据
 * @create 2023/7/9
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileUploadDto {
    private Boolean uploadComplete;
    /**
     * 相对于项目根目录的全路径
     */
    private String fileName;
    private Integer chunk;
    private Integer chunks;
}
