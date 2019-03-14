package com.yuyin.htmlToPdf.Utils;

import java.math.BigDecimal;
import java.util.Scanner;

public  class ConvertMoneyCapital {


    // 实现金钱的数值转换
    public  static String getChinese(BigDecimal number) {

        String chinese = "";
        String decimalsChinese="";
        String intNumber ="";
        String decimals = "";
        String chineseUnit = "元=十=百=千=万=十万=百万=千万=亿=十亿=百亿=千亿=万亿";
        String chineseValue = "零壹贰叁肆伍陆柒捌玖";

        String srcNumber = number + "";// 把数值换为String 型
        // 分开整数与小数
        if(srcNumber.contains(".")){
            intNumber = srcNumber.substring(0, srcNumber.indexOf("."));
            decimals = srcNumber.substring(srcNumber.indexOf(".") + 1, srcNumber.length());
            System.out.println("整数部分：" + intNumber);
            System.out.println("小数部分：" + decimals);
        }else{
            intNumber=srcNumber;
        }
        String chineseUnit1[] = chineseUnit.split("=");
        // 转换整数部分
        for (int i = 0; i < intNumber.length(); i++) {
            Integer num = Integer.parseInt(srcNumber.charAt(i) + "");
            if(num != 0){
                chinese += chineseValue.charAt(num)
                        + chineseUnit1[intNumber.length() - 1 - i];
            }

        }
        // 转换小数部分
        for (int i = 0; i < decimals.length(); i++) {
            if(i==0){
                decimalsChinese += chineseValue.charAt(Integer.parseInt(decimals.substring(0, 1))) + "角";
            }
            if(i==1){
                decimalsChinese+=chineseValue.charAt(Integer.parseInt(decimals.substring(1,2)))+"分";
            }
            if(i==2){
                decimalsChinese+=chineseValue.charAt(Integer.parseInt(decimals.substring(2,3)))+"厘";
            }
        }
        chinese += decimalsChinese;
        return chinese;
    }
}
