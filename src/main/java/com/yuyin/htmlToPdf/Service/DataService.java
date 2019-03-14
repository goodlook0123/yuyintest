package com.yuyin.htmlToPdf.Service;

import com.yuyin.htmlToPdf.Bean.DataDictionary;
import com.yuyin.htmlToPdf.dao.DataDictionaryMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class DataService {

    @Resource
    private DataDictionaryMapper dataDictionaryMapper;

    public List<DataDictionary> findDataDictionary(){
        return dataDictionaryMapper.findDataDictionary();
    }

}
