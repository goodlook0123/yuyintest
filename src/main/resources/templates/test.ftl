<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:th="http://www.thymeleaf.org"
      xmlns:sec="http://www.thymeleaf.org/thymeleaf-extras-springsecurity3">
<head >
<meta http-equiv="Content-Type" content="text/html; charset=gb2312"/>
<meta http-equiv="Content-Style-Type" content="text/css"/>
<#--<style>
	 body{ padding:0; margin:0; font-family:SimSun;} 
 
</style>-->
    <style type="text/css">
        body {
            font-family: pingfang sc light;
        }
        .center{
            text-align: center;
            width: 100%;
        }
    </style>
 </head>	
		
<body>
    <div id="footer" style="font-family:SimSun;">
            <#if testName?? && testName != "">${testName}</#if>
    </div>
 </body>
 </html>
