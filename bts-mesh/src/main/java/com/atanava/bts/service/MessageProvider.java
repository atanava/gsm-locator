package com.atanava.bts.service;

import com.atanava.bts.dto.MessageDto;

import java.util.List;

public interface MessageProvider {

    List<MessageDto> getMessageList(List<MobileStation> mobiles, List<BaseStation> bases);
}
