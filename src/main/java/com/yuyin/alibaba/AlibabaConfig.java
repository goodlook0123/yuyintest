package com.yuyin.alibaba;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nls.client.AccessToken;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;

public class AlibabaConfig {


    public static final String regionId = "cn-shanghai";
    public static final String accessKeyId = "LTAI2T1al9ECwCJ7";
    public static final String secret = "TmOFOS6vfzreDc4QD1E3DxVo6r3adc";
    public static final String appKey = "nej0hyn7JegshX1h";

    public static String token = "";
    public void AlibabaConfig() {
        AccessToken accessToken = null;
        try {
            accessToken = AccessToken.apply(accessKeyId, secret);
            token = accessToken.getToken();
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try{
           /* AccessToken accessToken = AccessToken.apply(accessKeyId, secret);
            String token = accessToken.getToken();
            long expireTime = accessToken.getExpireTime();*/
            // 创建DefaultAcsClient实例并初始化
            DefaultProfile profile = DefaultProfile.getProfile(
                    regionId,          // 您的地域ID
                    accessKeyId,      // 您的Access Key ID
                    secret); // 您的Access Key Secret
            IAcsClient client = new DefaultAcsClient(profile);
            CommonRequest request = new CommonRequest();
            request.setDomain("nls-meta.cn-shanghai.aliyuncs.com");
            request.setVersion("2018-05-18");
            request.setUriPattern("/pop/2018-05-18/tokens");
            request.setMethod(MethodType.POST);
            CommonResponse response = client.getCommonResponse(request);
            if (response.getHttpStatus() == 200) {
                JSONObject result = JSON.parseObject(response.getData());
                String token = result.getJSONObject("Token").getString("Id");
                long expireTime = result.getJSONObject("Token").getLongValue("ExpireTime");
                System.out.println(token + "*******************token,expireTime" + expireTime);
            }
        }catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

    }



}
