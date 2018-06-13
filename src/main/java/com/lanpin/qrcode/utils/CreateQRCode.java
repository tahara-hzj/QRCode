package com.lanpin.qrcode.utils;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import com.swetake.util.Qrcode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * 二维码工具类
 */
public class CreateQRCode {
    public static Logger logger = LoggerFactory.getLogger(CreateQRCode.class);

    /**
     * 使用QRCode生成二维码
     * @param content 二维码链接内容
     * @param request
     * @return
     */
    public static Object CreateQRCode(String content, HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> map = new HashMap<>(5);
        try{
            logger.info("CreateQRCode()开始生成二维码：" + content);
            //计算二维码图片的高宽比，API文档规定计算图片宽高的方式，v是本次测试的版本号，公式 67+12*(版本号-1)
            int v =6;
            int width = 67 + 12 * (v - 1);
            int height = 67 + 12 * (v - 1);

            Qrcode qrcode = new Qrcode();
            /**
             * 纠错等级分为
             * level L : 最大 7% 的错误能够被纠正；
             * level M : 最大 15% 的错误能够被纠正；
             * level Q : 最大 25% 的错误能够被纠正；
             * level H : 最大 30% 的错误能够被纠正；
             */
            qrcode.setQrcodeErrorCorrect('L');
            qrcode.setQrcodeEncodeMode('B');//注意版本信息 N代表数字 、A代表 a-z,A-Z、B代表 其他)
            qrcode.setQrcodeVersion(v);//版本号  1-40

            //定义图片缓冲区（指定图片缓冲区宽和高，以及类型）
            BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
            //定义画板
            Graphics2D graphics = bufferedImage.createGraphics();
            graphics.setBackground(Color.WHITE);
            graphics.setColor(Color.BLACK);
            //初始化，并指定画板的宽和高
            graphics.clearRect(0, 0, width, height);
            //要绘制的内容（字节数组）
            byte[] contentBytes = content.getBytes("utf-8");

            //定义偏移量
            int pixoff = 2;
            /**
             * 容易踩坑的地方
             * 1.注意for循环里面的i，j的顺序，
             *   s[j][i]二维数组的j，i的顺序要与这个方法中的 gs.fillRect(j*3+pixoff,i*3+pixoff, 3, 3);
             *   顺序匹配，否则会出现解析图片是一串数字
             * 2.注意此判断if (d.length > 0 && d.length < 120)
             *   是否会引起字符串长度大于120导致生成代码不执行，二维码空白
             *   根据自己的字符串大小来设置此配置
             */
            //开始绘制:内容长度默认为124（超过124会报错）
            if (contentBytes.length > 0 && contentBytes.length < 120) {
                boolean[][] qr = qrcode.calQrcode(contentBytes);
                for (int i = 0; i < qr.length; i++) {
                    for (int j = 0; j < qr.length; j++) {
                        if (qr[j][i]) {
                            graphics.fillRect(j*3+pixoff, i * 3 + pixoff, 3, 3);
                        }
                    }
                }
            }

            //生成logo
            Image img = ImageIO.read(new File("D://logo.jpg"));  // 实例化一个Image对象。
            graphics.drawImage(img, (width-30)/2, (height-30)/2, 30, 30, null);       // 75,75是距离gs两个边的距离，50,50是中间logo的大小
            //收起画板
            graphics.dispose();
            bufferedImage.flush();
            //字节数组流
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            //图片输出流
            ImageOutputStream imageOutputStream = ImageIO.createImageOutputStream(outputStream);

            //将图片写入图片流
            ImageIO.write(bufferedImage, "jpg", imageOutputStream);
//            ImageIO.write(bufferedImage, "jpg", response.getOutputStream());
            //将图片转化成base64，web显示
            String base64 = Base64.encode(outputStream.toByteArray());
            logger.info("CreateQRCode()生成二维码成功：" + base64);
            map.put("success", "0000");
            map.put("imageBase64", base64);
        }catch (Exception e){
            e.printStackTrace();
            map.put("success", "9999");
        }
        return map;
    }
}






