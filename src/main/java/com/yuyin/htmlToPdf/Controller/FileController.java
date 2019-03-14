package com.yuyin.htmlToPdf.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.itextpdf.text.DocumentException;
import com.yuyin.htmlToPdf.Bean.IdCardBean;
import com.yuyin.htmlToPdf.Bean.PartyMessage;
import com.yuyin.htmlToPdf.Service.EmailUtils;
import com.yuyin.htmlToPdf.Service.IdCardService;
import com.yuyin.htmlToPdf.Utils.ConvertMoneyCapital;
import com.yuyin.htmlToPdf.Utils.PDFTemplateUtilByEdit;
import com.yuyin.htmlToPdf.Utils.PDFTemplateUtilByHtml;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

@RestController
public class FileController {

    public static final String pdfTemplateFilePath = "C:\\个人借款合同.pdf";
    public static final String pdfCreateFinishPath = "D:\\";

    @Autowired
    private IdCardService idCardService;

    @RequestMapping(value = "/uploadFile",method = RequestMethod.POST)
    public Map<String, Object> upFile(@RequestParam("contractId") String contractId,
                                      @RequestParam("personType") Integer personType,HttpServletRequest request, HttpServletResponse response){
        Map<String, Object> json = new HashMap<String, Object>();
        try {
            request.setCharacterEncoding("UTF-8");
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            /** 页面控件的文件流* */
            MultipartFile multipartFile = null;
            Map map =multipartRequest.getFileMap();
            for (Iterator i = map.keySet().iterator(); i.hasNext();) {
                Object obj = i.next();
                multipartFile=(MultipartFile) map.get(obj);
            }
            Map<String,Object> resultMessage = idCardService.resultBaiduJson(multipartFile.getBytes(),contractId,personType);
            if(resultMessage!=null){
                return resultMessage;
            }

            /*List<Map<String, String>> attachfiles = new ArrayList<>();
            Map<String, String> attachfile = new HashMap<>();
            attachfile.put("name",idCardBean.getWords_result().getIdCardNum().getWords()+".pdf");
            attachfile.put("file","c://"+idCardBean.getWords_result().getIdCardNum().getWords()+".pdf");
            attachfiles.add(attachfile);
            boolean isSend = EmailUtils.sendEmail("这是一封测试邮件", new String[]{"1305840202@qq.com"}, null,
                    "<h3><a href='http://www.baidu.com'>百度一下，你就知道</a></h3>", attachfiles);*/
        }catch (Exception e){
            e.printStackTrace();
        }
        json.put("message", "应用上传成功");
        json.put("status", true);
        return json;
    }

    @RequestMapping("/createPdf")
    public String createPdf(@RequestParam("contractId") String contractId,@RequestParam("contractType") String contractType,
                            @RequestParam("borrowingBalance") String borrowingBalance,@RequestParam("interestRate") String interestRate,
                            @RequestParam("amortizationLoan") String amortizationLoan,@RequestParam("accountUsername") String accountUsername
            ,@RequestParam("accountNumber") String accountNumber) throws JsonProcessingException {




        //根据html生成pdf文件
            /*Map<String, Object> data = new HashMap<String, Object>();
            data.put("username", idCardBean.getWords_result().getName().getWords());
            FileOutputStream out = new FileOutputStream(new File("c://"+idCardBean.getWords_result().getIdCardNum().getWords()+".pdf"));


            PDFTemplateUtilByHtml pdfUtil = new PDFTemplateUtilByHtml();
            pdfUtil.createPDF(data, out);*/
        //根据pdf模板生成pdf文件
        PDFTemplateUtilByEdit pdfTemplateUtilByEdit = new PDFTemplateUtilByEdit();
        Map<String,String> param = new HashMap<>();
        boolean isSend = false;
        try {

            PartyMessage partyMessage = new PartyMessage();
            partyMessage.setContractId(contractId);
            List<PartyMessage> partyMessageList = idCardService.findPartyList(partyMessage);
            param = idCardService.resultPdfTemplateMessage(partyMessageList);
            param.put("contractId",contractId);
            param.put("money",borrowingBalance);
            param.put("capitalMoney",ConvertMoneyCapital.getChinese(new BigDecimal(borrowingBalance)));
            param.put("interestRate",interestRate);
            param.put("amortizationLoan",amortizationLoan);
            param.put("accountUsername",accountUsername);
            param.put("accountNumber",accountNumber);

            pdfTemplateUtilByEdit.createPdf(pdfTemplateFilePath,pdfCreateFinishPath,param);
            List<Map<String, String>> attachfiles = new ArrayList<>();
            Map<String, String> attachfile = new HashMap<>();
            attachfile.put("name",param.get("contractId")+".pdf");
            attachfile.put("file",pdfCreateFinishPath+param.get("contractId")+".pdf");
            attachfiles.add(attachfile);
            isSend = EmailUtils.sendEmail("测试邮件", new String[]{"906505882@qq.com"}, new String[]{"906505882@qq.com"},
                    "<h3><a href='http://www.baidu.com'>生成合同成功发送到您邮箱</a></h3>", attachfiles);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return "发送邮件:" + isSend;
    }

    @RequestMapping("/sendEmail")
    public String sendEmail() throws JsonProcessingException {

        List<Map<String, String>> attachfiles = new ArrayList<>();
        Map<String, String> attachfile = new HashMap<>();
        attachfile.put("name","ceshi.pdf");
        attachfile.put("file","D:\\testMail\\ceshi.pdf");
        attachfiles.add(attachfile);
        boolean isSend = EmailUtils.sendEmail("这是一封测试邮件", new String[]{"1305840202@qq.com"}, null,
                "<h3><a href='http://www.baidu.com'>百度一下，你就知道</a></h3>", attachfiles);
        return "发送邮件:" + isSend;
    }


}
