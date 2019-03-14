package com.yuyin.htmlToPdf.dao;


import com.yuyin.htmlToPdf.Bean.PartyMessage;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PartyMessageMapper {
    int insert(PartyMessage record);

    int insertSelective(PartyMessage record);

    List<PartyMessage> findPartyMessage(PartyMessage partyMessage);
}