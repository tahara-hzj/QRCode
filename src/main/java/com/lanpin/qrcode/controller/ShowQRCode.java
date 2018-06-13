package com.lanpin.qrcode.controller;

import com.lanpin.qrcode.utils.CreateQRCode;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 显示生成的二维码
 */
@Controller
public class ShowQRCode {

    @GetMapping("/index")
    @ResponseBody
    public String index(){
        return "qrcode show page ....";
    }

    @GetMapping("/createQRCode")
    @ResponseBody
    public Object createQRCode(HttpServletRequest request, HttpServletResponse response){
        String content = "https://my.csdn.net/?c=9c6a2f8b2eb03b41962e3fc3107c1a78";
        Object map = CreateQRCode.CreateQRCode(content, request,response);
        return map;
    }
}
