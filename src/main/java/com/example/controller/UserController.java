package com.example.controller;

import com.example.common.R;
import com.example.common.SMSUtils;
import com.example.common.ValidateCodeUtils;
import com.example.pojo.User;
import com.example.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/user")
@Transactional
public class UserController {
    @Autowired
    private UserService userService;

    //生成驗證碼
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session){
        //獲取手機號
        String phone = user.getPhone();

        if(StringUtils.isNotEmpty(phone)){
            //生成隨機的4位驗證碼
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("驗證碼:"+code);

            //調用阿里雲提供的短信服務API發送短信(本項目使用隨機生成驗證碼模擬此功能)
            //SMSUtils.sendMessage("瑞吉外賣","",phone,code);

            //將生成的驗證碼保存至Session
            session.setAttribute(phone,code);

            return R.success("手機短信驗證碼發送成功!");
        }

        return R.error("短信驗證碼發送失敗!");
    }

    //用戶登錄
    @PostMapping("/login")
    public R<User> login(@RequestBody Map map,HttpSession session){
        log.info(map.toString());

        //1.獲取手機號和驗證碼
        String phone = map.get("phone").toString();
        String code = map.get("code").toString();
        //2.從Session中獲取保存的驗證碼
        String codeInSession = session.getAttribute(phone).toString();

        //3.比對驗證碼
        if(codeInSession != null && code.equals(codeInSession)){
            //登錄成功
            //判斷當前手機號對應的用戶是否為新用戶，如果是新用戶則自動完成註冊
            User user = userService.selectUserByPhone(phone);
            if(user == null){
                //自動註冊新用戶
                User newUser=new User();
                newUser.setPhone(phone);
                newUser.setStatus(1);
                userService.save(newUser);
            }

            //新用戶註冊後須再按手機號查詢一次才能得到對象
            User user2 = userService.selectUserByPhone(phone);
            //將用戶id存入Session
            session.setAttribute("user",user2.getId());

            return R.success(user);
        }

        return R.error("登錄失敗!");
    }

    //退出登錄
    @PostMapping("/loginout")
    public R<String> logout(ServletResponse servletResponse,HttpSession session) throws IOException {
        HttpServletResponse resp=(HttpServletResponse)servletResponse;

        //清理Session中保存的用戶id、手機號、驗證碼
        session.removeAttribute("user");
        session.removeAttribute("phone");
        session.removeAttribute("code");

        //返回登錄頁面
        //resp.sendRedirect("/front/page/login.html");

        log.info("退出成功!");
        return R.success("退出成功!");
    }
}
