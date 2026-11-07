package ru.gelman.spring_chat_application.application.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ru.gelman.spring_chat_application.application.dto.CreateUserCommand;
import ru.gelman.spring_chat_application.application.dto.UserDetails;
import ru.gelman.spring_chat_application.domain.model.User;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface UserMapper {
    User convert(CreateUserCommand request);

    UserDetails convert(User user);
}
