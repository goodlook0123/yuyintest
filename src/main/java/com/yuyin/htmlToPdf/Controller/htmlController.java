package com.yuyin.htmlToPdf.Controller;

import com.yuyin.htmlToPdf.Service.DataService;
import com.yuyin.htmlToPdf.Utils.SnowflakeIdWorker;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

@Controller
public class htmlController {

    @Resource
    private DataService dataService;

    @RequestMapping(value = "/index",method = RequestMethod.GET)
    public ModelAndView returnIndex(ModelAndView model){

        model.addObject("dataDic",dataService.findDataDictionary());
        model.addObject("a","你好");
        SnowflakeIdWorker idWorker = new SnowflakeIdWorker(1,1);
        model.addObject("contractId",String.valueOf(idWorker.nextId()));
        model.setViewName("index");
        /*model.addAttribute("dataDic",dataService.findDataDictionary());
        model.addAttribute("nihao","你好");*/
        return model;
    }
}
