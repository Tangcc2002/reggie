package com.itheima.reggie.config;

import com.itheima.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/common")
public class CommonController {

    @Value("${reggie.image-path}")
    private String basePath;

    /**
     * 文件上传
     * @param file  上传的文件
     * @return  上传操作的结果
     */
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) throws IOException {
        log.info(file.toString());
        // 使用UUID生成随机ID，防止文件名重复造成文件覆盖
        String fileName = UUID.randomUUID().toString()+".png";
        // 文件保存的路径
        File filePath = new File(basePath + fileName);
        // 判断文件保存路径是否存在，不存在则创建
        if (!filePath.exists())
            filePath.mkdir();
        // 保存上传的文件
        file.transferTo(filePath);
        return R.success(fileName);
    }

    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) {
        try {
            // 通过输入流读取文件
            FileInputStream inputStream = new FileInputStream(new File(basePath + name));
            // 通过输出流向前端发送图片
            ServletOutputStream outputStream = response.getOutputStream();
            // 设置返回数据的格式
            response.setContentType("image/jpeg");
            // 存储图片字节码的byte数组
            byte[] bytes = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, len);
                outputStream.flush();
            }
            // 关闭资源
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
