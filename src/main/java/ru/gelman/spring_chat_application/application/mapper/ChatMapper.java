package ru.gelman.spring_chat_application.application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ru.gelman.spring_chat_application.application.dto.ChatMemberDetails;
import ru.gelman.spring_chat_application.application.dto.ChatDetails;
import ru.gelman.spring_chat_application.domain.model.Chat;

import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ChatMapper {
    ChatDetails convert(Chat chat, List<ChatMemberDetails> members);
}
