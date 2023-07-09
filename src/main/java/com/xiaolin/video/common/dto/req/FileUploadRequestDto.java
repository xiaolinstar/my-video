package com.xiaolin.video.common.dto.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author xingxiaolin xlxing@bupt.edu.cn
 * @Description
 * @create 2023/7/9
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileUploadRequestDto {
    private MultipartFile file;
    private String fileType;
}
