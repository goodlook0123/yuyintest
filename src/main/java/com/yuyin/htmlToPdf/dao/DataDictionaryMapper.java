package com.yuyin.htmlToPdf.dao;

import com.yuyin.htmlToPdf.Bean.DataDictionary;

import java.util.List;

public interface DataDictionaryMapper {
    int insert(DataDictionary record);

    int insertSelective(DataDictionary record);

    List<DataDictionary> findDataDictionary();
}