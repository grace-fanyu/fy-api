package com.fanyu.project.utils;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Random;
import java.util.regex.Pattern;

@Component
public class SendMailUtil  {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 随机生成指定长度字符串验证码
     *
     * @param length 验证码长度
     */
    public String generateVerifyCode(int length) {
        String strRange = "1234567890";
        StringBuilder strBuilder = new StringBuilder();

        for (int i = 0; i < length; ++i) {
            char ch = strRange.charAt((new Random()).nextInt(strRange.length()));
            strBuilder.append(ch);
        }
        return strBuilder.toString();
    }

    /**
     * 校验验证码（可用作帐号登录、注册、修改信息等业务）
     * 思路：先检查redis中是否有key位对应email的键值对，没有代表验证码过期；如果有就判断用户输入的验证码与value是否相同，进而判断验证码是否正确。
     */
    public Integer checkVerifyCode(String email, String code) {
        int result = 1;
        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
        String msgKey = "msg_" + email;
        String verifyCode = valueOperations.get(msgKey);
        /*校验验证码：0验证码错误、1验证码正确、2验证码过期*/
        if (verifyCode == null) {
            result = 2;
        } else if (!code.equals(verifyCode)) {
            result = 0;
        }
        // 如果验证码正确，则从redis删除
        if (result == 1) {
            stringRedisTemplate.delete(msgKey);
        }
        return result;
    }



    /**
     * 验证邮箱格式
     * @param email 邮箱
     * @return boolean
     */
    public  boolean isNotEmail(String email) {
        if (email == null || email.length() < 1 || email.length() > 256) {
            return false;
        }
        Pattern pattern = Pattern.compile("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");
        return !pattern.matcher(email).matches();
    }

    /**
     * 发送验证码(可以根据需求更改相应参数)
     * @param sender    发送人的邮箱
     * @param pwd          邮箱授权码
     * @param receiver      收件人
     * @param code          验证码
     * @return
     */
    public String sendEmail(String sender, String pwd, String receiver, String code){
        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp");       //使用smpt的邮件传输协议
        props.setProperty("mail.smtp.host", "smtpout.secureserver.net");        //主机地址
        props.setProperty("mail.smtp.auth", "true");        //授权通过
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        Session session = Session.getInstance(props);

        try {
            MimeMessage message = new MimeMessage(session);
            // 第二个参数可不填
            message.setFrom(new InternetAddress(sender,"sender"));      //设置发件人
            message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(receiver,"用户","utf-8"));     //设置收件人
            message.setSubject("verification code");        //设置主题
            message.setSentDate(new Date());

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            // 这是我们的邮件内容，可根据需求更改
            String str = "<!DOCTYPE html><html><head><meta charset='UTF-8'></head><body><p style='font-size: 20px;font-weight:bold;'>Dear ："+receiver+"</p>"
                    + "<p style='text-indent:2em; font-size: 20px;'>Your verification code this time is "
                    + "<span style='font-size:30px;font-weight:bold;color:red'>" + code + "</span>,Valid within 60 SECONDS, please use as soon as possible! If not operated by yourself, please ignore!</p>"
                    + "<p style='text-align:right; padding-right: 20px;'"
                    + "<a href='http://120.79.29.170' style='font-size: 18px'>aof labs</a></p>"
                    + "<span style='font-size: 18px; float:right; margin-right: 60px;'>" + sdf.format(new Date()) + "</span></body></html>";

            Multipart mul=new MimeMultipart();  //新建一个MimeMultipart对象来存放多个BodyPart对象
            BodyPart mdp=new MimeBodyPart();  //新建一个存放信件内容的BodyPart对象
            mdp.setContent(str, "text/html;charset=utf-8");
            mul.addBodyPart(mdp);  //将含有信件内容的BodyPart加入到MimeMultipart对象中
            message.setContent(mul); //把mul作为消息内容


            message.saveChanges();

            //创建一个传输对象
            Transport transport=session.getTransport("smtp");


            //建立与服务器的链接  465端口是 SSL传输
            transport.connect("smtpout.secureserver.net", 465, sender, pwd);

            //发送邮件
            transport.sendMessage(message,message.getAllRecipients());

            //关闭邮件传输
            transport.close();
            return "验证码发送成功";

        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
            return "验证码发送失败";
        }
    }
    /**生成随机的六位验证码*/
    public StringBuilder CreateCode() {
        String dates = "0123456789";
        StringBuilder code = new StringBuilder();
        Random r = new Random();
        for (int i = 0; i < 6; i++) {
            int index = r.nextInt(dates.length());
            char c = dates.charAt(index);
            code.append(c);
        }
        return code;
    }
}


