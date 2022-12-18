package com.example.controller.filter;

import com.alibaba.fastjson.JSON;
import com.example.common.BaseContext;
import com.example.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/*")
@Slf4j
public class LoginFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req=(HttpServletRequest) servletRequest;
        HttpServletResponse resp=(HttpServletResponse)servletResponse;

        //0.判斷訪問資源路徑是否和登錄註冊相關
        String [] urls={
                //靜態資源
                "/backend/images/",
                "/backend/api/",
                "/backend/js/",
                "/backend/plugins/",
                "/backend/page/login/",
                "/backend/styles/",
                "/backend/favicon.ico",
                "/front/images/",
                "/front/api/",
                "/front/js/",
                "/front/page/login.html",
                "/front/styles/",
                //請求
                "/employee/login",
                "/employee/logout",
                "/user/sendMsg",
                "/user/login",
                "/user/loginout"
        };

        //獲取當前訪問的資源路徑
        String url = req.getRequestURL().toString();
        log.info("攔截到請求:{}",url);

        //循環判斷
        for (String u : urls) {
            if(url.contains(u)){
                //放行
                filterChain.doFilter(servletRequest,servletResponse);

                log.info("本次請求{}不需要處理",url);
                return; //拒絕執行放行後代碼
            }
        }
        //1.判斷使用者是否登錄成功
        HttpSession session = req.getSession();
        Long employeeId=(Long)session.getAttribute("employee");
        Long userId = (Long)session.getAttribute("user");

        if(employeeId != null || userId != null){
            if(employeeId != null){
                //登錄成功
                //將id存入ThreadLocal
                //放行

                Long empId = (Long) req.getSession().getAttribute("employee");
                BaseContext.setCurrentId(empId);

                filterChain.doFilter(servletRequest,servletResponse);

                log.info("員工已登錄，員工id為{}",employeeId);
            }

            if(userId != null){
                //登錄成功
                //將id存入ThreadLocal
                //放行

                Long userId2 = (Long) req.getSession().getAttribute("user");
                BaseContext.setCurrentId(userId2);

                filterChain.doFilter(servletRequest,servletResponse);

                log.info("用戶已登錄，用戶id為{}",userId2);
            }

        }else {
            //登錄失敗，檢查誰在越級訪問目錄
            String backendURL = "/backend/";
            String frontURL = "/front";

            if(url.contains(backendURL)){
                //員工試圖越級登錄
                //跳轉至後台登錄頁面
                resp.sendRedirect("/backend/page/login/login.html");
            }

            if(url.contains(frontURL)){
                //用戶試圖越級登錄
                //跳轉至前台登錄頁面
                resp.sendRedirect("/front/page/login.html");
            }
        }
    }

    @Override
    public void destroy() {

    }
}