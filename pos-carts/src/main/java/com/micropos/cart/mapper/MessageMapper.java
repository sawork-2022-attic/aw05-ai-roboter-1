package com.micropos.cart.mapper;

import com.micropos.cart.dto.MessageDto;
import com.micropos.cart.model.Message;
import org.mapstruct.Mapper;


@Mapper
public interface MessageMapper {
    MessageDto toMessageDto(Message message);
}
