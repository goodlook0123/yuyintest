package com.yuyin.htmlToPdf.Service;

import com.alibaba.fastjson.JSON;
import com.baidu.aip.ocr.AipOcr;
import com.yuyin.htmlToPdf.Bean.IdCardBean;
import com.yuyin.htmlToPdf.Bean.PartyMessage;
import com.yuyin.htmlToPdf.Utils.SnowflakeIdWorker;
import com.yuyin.htmlToPdf.dao.PartyMessageMapper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class IdCardService implements Serializable{



    // 设置APPID/AK/SK：百度云自己的帐号信息
    public static final String APP_ID = "15511048";
    public static final String API_KEY = "GCPPIqk81ZcFBvZ5TdYRdr1x";
    public static final String SECRET_KEY = "zXDy1BThSDUQukARsVitRkNr3d9np0C6";

    @Resource
    private PartyMessageMapper partyMessageMapper;

    /**
     * 返回身份证信息
     * @return
     */
    public Map<String,Object> resultBaiduJson(byte[] idcardImage,String contractId,Integer personType){
        //返回值：
        Map<String,Object> resultMessage = null;
        // 初始化一个AipOcr
        AipOcr client = new AipOcr(APP_ID, API_KEY, SECRET_KEY);

        HashMap<String, String> options = new HashMap<String,String>();

        options.put("detect_direction", "true");
        /**
         * 是否开启身份证风险类型(身份证复印件、临时身份证、身份证翻拍、修改过的身份证)功能，
         * 默认不开启，即：false。
         * 可选值:true-开启；false-不开启
         */
        options.put("detect_risk", "false");
        /**
         * front：身份证含照片的一面
         * back：身份证带国徽的一面
         * 必须正确声明，否则"error_msg": "recognize id card error"
         */
        String idCardSide = "front";
        //本地图片
        //String path = "C:\\曹清.jpg";
        //idcard 表示读取图片的类型是身份证
        //JSONObject res = client.idcard(path,idCardSide,options);
        JSONObject res = client.idcard(idcardImage,idCardSide,options);
        IdCardBean idCardFront = JSON.parseObject(res.toString(),IdCardBean.class);
        //验证身份证信息
        resultMessage = resultMessage(idCardFront);
        if(resultMessage != null){
            return resultMessage;
        }

        if(idCardFront.getImage_status() == "normal" || idCardFront.getImage_status().equals("normal")){
            Map<String,Object> resultMap = checkContractMessage(contractId,idCardFront);
            if(resultMap != null && !resultMap.isEmpty()){
                return resultMap;
            }
            saveIdcard(idCardFront,contractId,personType);
        }
        if(idCardFront.getImage_status() == "reversed_side" || idCardFront.getImage_status().equals("reversed_side")){
            idCardFront = new IdCardBean();
            JSONObject resBack = client.idcard(idcardImage,"back",options);
            idCardFront = JSON.parseObject(resBack.toString(),IdCardBean.class);
            Map<String,Object> resultMap = checkContractMessage(contractId,idCardFront);
            if(resultMap != null){
                return resultMap;
            }
            saveIdcard(idCardFront,contractId,personType);
        }
        return resultMessage;
    }

    public Map<String,Object> checkContractMessage(String contractId,IdCardBean idCardBean){
        Map<String,Object> resultMessage = new HashMap<>(2);
        //查询该身份证信息和该合同信息是否重复上传
        PartyMessage partyMessage = new PartyMessage();
        partyMessage.setContractId(contractId);
        partyMessage.setIdcard(idCardBean.getWords_result().getIdCardNum().getWords());
        List<PartyMessage> partyMessageList = partyMessageMapper.findPartyMessage(partyMessage);
        if(partyMessageList != null && !partyMessageList.isEmpty() && partyMessageList.size()!=0){
            resultMessage.put("error","该身份证在该合同已存在");
            resultMessage.put("status",false);
            return resultMessage;
        }
        return resultMessage;
    }

    public Map<String,Object> resultMessage(IdCardBean idCardBean){

        Map<String,Object> json = new HashMap<String,Object>();
        if(idCardBean.getImage_status().equals("blurred") || idCardBean.getImage_status()=="blurred"){
            json.put("error", "身份证太模糊，请重新拍摄");
            json.put("status", false);
            return json;
        }
        if(idCardBean.getImage_status().equals("non_idcard") || idCardBean.getImage_status()=="non_idcard"){
            json.put("error", "上传的图片中不包含身份证");
            json.put("status", false);
            return json;
        }
        if(idCardBean.getImage_status().equals("other_type_card") || idCardBean.getImage_status()=="other_type_card"){
            json.put("error", "您上传的是其他类型证件");
            json.put("status", false);
            return json;
        }
        if(idCardBean.getImage_status().equals("over_exposure") || idCardBean.getImage_status()=="over_exposure"){
            json.put("error", "您上传的身份证关键字段反光或者曝光过度");
            json.put("status", false);
            return json;
        }
        if(idCardBean.getImage_status().equals("unknown") || idCardBean.getImage_status()=="unknown"){
            json.put("error", "您上传的文件无法识别，请重新上传身份证");
            json.put("status", false);
            return json;
        }
        return null;
    }


    public void saveIdcard(IdCardBean idCardBean,String contractId,Integer personType){
        PartyMessage partyMessage = new PartyMessage();
        SnowflakeIdWorker idWorker = new SnowflakeIdWorker(1, 1);
        partyMessage.setId(idWorker.nextId()+"");
        partyMessage.setContractId(contractId);
        partyMessage.setName(isNullObject(idCardBean.getWords_result().getName())?"":idCardBean.getWords_result().getName().getWords());
        partyMessage.setNation(isNullObject(idCardBean.getWords_result().getNation())?"":idCardBean.getWords_result().getNation().getWords());
        partyMessage.setSex(isNullObject(idCardBean.getWords_result().getSex())?"":idCardBean.getWords_result().getSex().getWords());
        partyMessage.setBirth(isNullObject(idCardBean.getWords_result().getBirth())?"":idCardBean.getWords_result().getBirth().getWords());
        partyMessage.setIdcard(isNullObject(idCardBean.getWords_result().getIdCardNum())?"":idCardBean.getWords_result().getIdCardNum().getWords());
        partyMessage.setAddress(isNullObject(idCardBean.getWords_result().getAddress())?"":idCardBean.getWords_result().getAddress().getWords());
        //partyMessage.setPhono(idCardBean.getWords_result().getWords());
        partyMessage.setCreateTime(new Date());
        partyMessage.setFlag(1);
        partyMessage.setPersonType(personType);
        partyMessage.setIssueDate(isNullObject(idCardBean.getWords_result().getIssueDate())?"":idCardBean.getWords_result().getIssueDate().getWords());
        partyMessage.setAuthority(isNullObject(idCardBean.getWords_result().getAuthority())?"":idCardBean.getWords_result().getAuthority().getWords());
        partyMessage.setExpiryDate(isNullObject(idCardBean.getWords_result().getExpiryDate())?"":idCardBean.getWords_result().getExpiryDate().getWords());
        partyMessageMapper.insertSelective(partyMessage);
    }

    /**
     * 查询人员信息
     * @param partyMessage
     * @return
     */
    public List<PartyMessage> findPartyList(PartyMessage partyMessage){
        return partyMessageMapper.findPartyMessage(partyMessage);
    }

    public Map<String,String> resultPdfTemplateMessage(List<PartyMessage> partyMessageList){

        Map<String,String> param = new HashMap<>();
        for(PartyMessage partyMessage:partyMessageList){
            //借款人
            if(partyMessage.getPersonType() == 1){
                param.put("borrower","    "+partyMessage.getName());
                param.put("borrowerIdcard",partyMessage.getIdcard());
                param.put("borrowerAddress",partyMessage.getAddress());
            }
            //偿还人
            if(partyMessage.getPersonType() == 2){

            }
            //担保人
            if(partyMessage.getPersonType() == 3){
                param.put("coBorrower","    "+partyMessage.getName());
                param.put("coBorrowerIdcard",partyMessage.getIdcard());
                param.put("coBorrowerAddress",partyMessage.getAddress());
            }
        }

        return param;
    }


    public boolean isNullObject(Object object){
        return object==null;
    }
   /* public static void main(String[] args) {

        // 初始化一个AipOcr
        AipOcr client = new AipOcr(APP_ID, API_KEY, SECRET_KEY);

        HashMap<String, String> options = new HashMap<String,String>();

        options.put("detect_direction", "true");
        *//**
         * 是否开启身份证风险类型(身份证复印件、临时身份证、身份证翻拍、修改过的身份证)功能，
         * 默认不开启，即：false。
         * 可选值:true-开启；false-不开启
         *//*
        options.put("detect_risk", "false");
        *//**
         * front：身份证含照片的一面
         * back：身份证带国徽的一面
         * 必须正确声明，否则"error_msg": "recognize id card error"
         *//*
        String idCardSide = "front";
        //本地图片
        String path = "C:\\2.png";
        //idcard 表示读取图片的类型是身份证
        JSONObject res = client.idcard(path,idCardSide,options);
        IdCardBean idCardFront = JSON.parseObject(res.toString(),IdCardBean.class);
        System.out.println(res.toString(1));
    }*/
}
