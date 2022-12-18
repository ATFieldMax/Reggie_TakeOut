package com.example.controller;

import com.example.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
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

//文件上傳和下載

@Slf4j
@RestController
@RequestMapping("/common")
@Transactional
public class CommonController {

    @Value("${reggie.path}")
    private String basePath;

    //文件上傳
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file){ //方法參數固定為MultipartFile
    //                                             參數名需與瀏覽器請求頭Form Data下Content-Disposition中的name屬性一致
    //                                             file是一個臨時文件，需要轉存到指定位置，否則本次請求結束後將自動刪除
        log.info(file.toString());

        //原始文件名
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));

        //使用UUID重新生成文件名，防止文件名稱重複造成文件覆蓋
        String fileName = UUID.randomUUID().toString() + suffix;

        //判斷當前目錄是否存在
        File dir=new File(basePath);
        if(!dir.exists()){
            //目錄不存在，需要創建
            dir.mkdirs();
        }

        try {
            //將臨時文件轉存到指定位置
            file.transferTo(new File(basePath + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return R.success(fileName);
    }

    //文件下載
    @GetMapping("/download")
    public void download(String name, HttpServletResponse resp){
        try {
            //使用輸入流讀取文件內容
            FileInputStream fileInputStream=new FileInputStream(new File(basePath+name));

            //使用輸出流將文件寫回瀏覽器，在瀏覽器展示圖片
            ServletOutputStream outputStream = resp.getOutputStream();

            //設置響應文件類型
            resp.setContentType("image/jpeg");

            int len = 0;
            byte[] bytes=new byte[1024];
            while(len != -1){
                len=fileInputStream.read(bytes);
                outputStream.write(bytes,0,len);
            }

            fileInputStream.close();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
