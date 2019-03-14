package com.yuyin.htmlToPdf.Utils;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Map;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.*;

public class PDFTemplateUtilByEdit {
    public void createPdf(String infilePath,String outfilePath,Map<String, String> param) throws IOException, DocumentException {
        // pdf模板
        //String fileName = "C:\\个人借款合同.pdf";
        PdfReader reader = new PdfReader(infilePath);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        //* 将要生成的目标PDF文件名称 *//*
        PdfStamper ps = new PdfStamper(reader, bos);
        PdfContentByte under = ps.getUnderContent(1);
        //* 使用中文字体 *//*
        BaseFont bf = BaseFont.createFont("/pdf/font/simsun.ttf", BaseFont.IDENTITY_H,BaseFont.NOT_EMBEDDED);
        ArrayList<BaseFont> fontList = new ArrayList<BaseFont>();
        fontList.add(bf);
        //* 取出报表模板中的所有字段 *//*
        AcroFields fields = ps.getAcroFields();
        fields.addSubstitutionFont(BaseFont.createFont("/pdf/font/simsun.ttf", BaseFont.IDENTITY_H,BaseFont.NOT_EMBEDDED));
        fields.setSubstitutionFonts(fontList);
        fillData(fields, param);
        //* 必须要调用这个，否则文档不会生成的 *//*
        ps.setFormFlattening(true);
        ps.close();
        //生成pdf路径
        OutputStream fos = new FileOutputStream(outfilePath + param.get("contractId")+".pdf");
        fos.write(bos.toByteArray());
        fos.flush();
        fos.close();
        bos.close();
    }

    /**
     *     * 填充模板
     *     *
     */
    public void fillData(AcroFields fields, Map<String, String> data)
            throws IOException, DocumentException {
        for (String key : data.keySet()) {
            String value = data.get(key);
            fields.setField(key, value); // 为字段赋值,注意字段名称是区分大小写的
        }
    }

}
