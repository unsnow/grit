package com.hdjd.grit.service;

import com.google.gson.Gson;
import com.hdjd.grit.mapper.UserMapper;
import com.hdjd.grit.model.pojo.User;
import com.hdjd.grit.model.pojo.VO.AccessToken;

import com.hdjd.grit.model.pojo.VO.TemplateData;
import com.hdjd.grit.model.pojo.VO.WxMssVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;



@Service
@Slf4j
public class WxPushServiceQcl {
    //用来请求微信的get和post
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private UserMapper userMapper;

    private final String APPID = "wx1d5d8f1f8db9eef2";
    private final String APPSecret = "108c28be7cd5a40291d9dd91932c2744";
    /*
     * 微信小程序下单完推送单个用户
     * */
    public String   pushOneUser(String userId,String deliveryAddress,String userName
            ,String destination,String time,String telephone) {

        //通过userI查询用户openid
        User user = new User();
        if(userId != null){
            user = userMapper.getUserInfoById(userId);
        }
        String openid = user.getWechatId();
        //获取access_token
        String access_token = getAccess_token(APPID,APPSecret);
        String url = "https://api.weixin.qq.com/cgi-bin/message/subscribe/send" +
                "?access_token=" + access_token;
        System.out.println("access_token===="+access_token);
        //拼接推送的模版
        WxMssVo wxMssVo = new WxMssVo();
        wxMssVo.setTouser(openid);//用户openid
        wxMssVo.setTemplate_id("YW5tcmuX3-mRWO9U-pI-8bFqfgPaYwfS3qOgXGsMB-I");//模版id
//        wxMssVo.setForm_id(formid);//formid


        Map<String, TemplateData> m = new HashMap<>(5);

        m.put("thing3",new TemplateData(deliveryAddress));
        m.put("name7",new TemplateData(userName));
        m.put("thing10",new TemplateData(destination));
        m.put("date2",new TemplateData(time));
        m.put("phone_number9",new TemplateData(telephone));
        wxMssVo.setData(m);

        System.out.println("wxMssVo"+wxMssVo);
        ResponseEntity<String> responseEntity =
                restTemplate.postForEntity(url, wxMssVo, String.class);
        log.error("小程序推送结果={}", responseEntity.getBody());
        return responseEntity.getBody();
    }


    /*
     * 获取access_token
     * appid和appsecret到小程序后台获取，当然也可以让小程序开发人员给你传过来
     * */
    public String getAccess_token(String appid, String appsecret) {
        //获取access_token
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential" +
                "&appid=" + appid + "&secret=" + appsecret;
        String json = restTemplate.getForObject(url, String.class);
        AccessToken accessToken = new Gson().fromJson(json, AccessToken.class);
        return accessToken.getAccess_token();
    }

}