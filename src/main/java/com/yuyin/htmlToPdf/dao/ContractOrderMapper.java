package com.yuyin.htmlToPdf.dao;


import com.yuyin.htmlToPdf.Bean.ContractOrder;
import org.apache.ibatis.annotations.Mapper;

public interface ContractOrderMapper {
    int insert(ContractOrder record);

    int insertSelective(ContractOrder record);
}