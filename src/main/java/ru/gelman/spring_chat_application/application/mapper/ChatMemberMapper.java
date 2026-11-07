package ru.gelman.spring_chat_application.application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ru.gelman.spring_chat_application.application.dto.ChatMemberDetails;
import ru.gelman.spring_chat_application.domain.model.ChatMember;

import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ChatMemberMapper {
    default ChatMemberDetails convert(ChatMember member) {
        return new ChatMemberDetails(
                member.getId().getUserId(),
                member.getRole()
        );
    }

    List<ChatMemberDetails> convert(List<ChatMember> member);
}
