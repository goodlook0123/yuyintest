package com.yuyin.htmlToPdf.Utils;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;
import com.lowagie.text.DocumentException;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.Locale;
import com.itextpdf.layout.Document;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.lowagie.text.pdf.BaseFont;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class PDFTemplateUtilByHtml {

    /**
     * classpath路径
     */
    private String classpath = getClass().getResource("/").getPath();

    /**
     * 指定FreeMarker模板文件的位置
     */
    private String templatePath = "/pdf";

    /**
     * freeMarker模板文件名称
     */
    //private String templateFileName = "pdf.ftl";
    private String templateFileName = "IndividualLoanContract.ftl";

    /**
     * 图片路径 —— 默认是classpath下面的images文件夹
     */
    private String imagePath = "/images/";

    /**
     * 字体资源文件 存放路径
     */
    private String fontPath = "pdf/font/";

    /**
     * 字体   [宋体][simsun.ttc]   [黑体][simhei.ttf]
     */
    private String font = "simsun.ttc";
    private String heiFont = "simhei.ttf";

    /**
     * 指定编码
     */
    private String encoding = "UTF-8";


    /**
     * 生成pdf
     * @param data  传入到freemarker模板里的数据
     * @param out   生成的pdf文件流
     */
    public void createPDF(Object data, OutputStream out) {
        // 创建一个FreeMarker实例, 负责管理FreeMarker模板的Configuration实例
        Configuration cfg = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        // 指定FreeMarker模板文件的位置
        cfg.setClassForTemplateLoading(getClass(), templatePath);

        ITextRenderer renderer = new ITextRenderer();
        try {
            // 设置 css中 的字体样式（暂时仅支持宋体和黑体）
            //renderer.getFontResolver().addFont(classpath + fontPath + font, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            renderer.getFontResolver().addFont(classpath + fontPath + font, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);

            //renderer.getFontResolver().addFontDirectory("c:/Windows/Fonts/", BaseFont.EMBEDDED);
            /*fontResolver.addFontDirectory("C:\\Windows\\Fonts", BaseFont.EMBEDDED);*/
            /*String fileSeparator = System.getProperty("file.separator");
            String windir = System.getenv("windir");
            if (windir != null && fileSeparator != null) {
                addFontDirectory(windir + fileSeparator + "fonts", BaseFont.NOT_EMBEDDED, fontResolver);
            }
            addFontDirectory("C:\\Windows\\Fonts", BaseFont.EMBEDDED, fontResolver);*/
            // 设置模板的编码格式
            cfg.setEncoding(Locale.CHINA, encoding);
            // 获取模板文件 template.ftl
            Template template = cfg.getTemplate(templateFileName, encoding);
            StringWriter writer = new StringWriter();
            // 将数据输出到html中
            template.process(data, writer);
            writer.flush();

            String html = writer.toString();
            // 把html代码传入渲染器中
            renderer.setDocumentFromString(html.replace("&nbsp;"," ").replace("宋体","SimSun").replace("黑体","SimSun").replace("仿宋","SimSun"));

            // 解决图片的相对路径问题 ##必须在设置document后再设置图片路径，不然不起作用
            // 如果使用绝对路径依然有问题，可以在路径前面加"file:/"
            renderer.getSharedContext().setBaseURL(classpath + imagePath);
            renderer.layout();

            renderer.createPDF(out, false);
            renderer.finishPDF();
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void setClasspath(String classpath) {
        this.classpath = classpath;
    }


    public void setTemplatePath(String templatePath) {
        this.templatePath = templatePath;
    }


    public void setTemplateFileName(String templateFileName) {
        this.templateFileName = templateFileName;
    }


    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }


    public void setFontPath(String fontPath) {
        this.fontPath = fontPath;
    }


    public void setFont(String font) {
        this.font = font;
    }


    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }


    @Override
    public String toString() {
        return   "[templatePath] : " + templatePath + " \r\n "
                +"[templateFileName] : " + templateFileName + " \r\n "
                +"[imagePath] : " + imagePath + " \r\n "
                +"[fontPath] : " + fontPath + " \r\n "
                +"[font] : " + font + " \r\n "
                +"[encoding] : " + encoding;
    }

    /**
     * 添加表格
     * @param
     * @param Element e
     * */
    public void addTable(Document doc, Element e) throws Exception{
        Elements eTag=e.children();//获得thead和tobody
        Table table=new Table(e.children().last().children().last().childNodeSize());//tobody最后一列的tr获得行数，这里可能会有些出入
        for(Element tBady: eTag){
            Elements tCellList=tBady.children();//获得tr行
            for(Element tCell: tCellList){
                Elements tDList=tCell.children();//获得td格子
                for(Element td:tDList){
                    String oldRowspan=td.attr("rowspan");//获得跨行
                    String oldColspan=td.attr("colspan");//获得跨列
                    int rowspan=0;
                    int colspan=0;
                    if(oldRowspan!="" && oldRowspan.length()>0){
                        rowspan=Integer.valueOf(td.attr("rowspan"));
                    }
                    if(oldColspan!="" && oldColspan.length()>0){
                        colspan=Integer.valueOf(td.attr("colspan"));
                    }

                    Cell cell=new Cell(rowspan,colspan);
                    table.addCell(cell);
                }
            }

        }
        PdfFont sysFont = PdfFontFactory.createFont("c://windows//fonts//simsun.ttc,1", PdfEncodings.IDENTITY_H, false);
        doc.add(table.setFont(sysFont).setAutoLayout());
    }

}
