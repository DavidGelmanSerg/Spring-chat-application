package ru.gelman.spring_chat_application.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ru.gelman.spring_chat_application.model.dto.CreateUserRequest;
import ru.gelman.spring_chat_application.model.dto.UserResponse;
import ru.gelman.spring_chat_application.model.entity.User;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface UserMapper {
    User convert(CreateUserRequest request);

    UserResponse convert(User user);
}
