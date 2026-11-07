package ru.gelman.spring_chat_application.infrastructure.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.gelman.spring_chat_application.domain.exception.ChatException;
import ru.gelman.spring_chat_application.domain.exception.SelfChatNotAllowedException;
import ru.gelman.spring_chat_application.domain.exception.UserAlreadyExistsException;
import ru.gelman.spring_chat_application.domain.exception.UserNotFoundException;

@ControllerAdvice
@Slf4j
public class MessengerExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<?> userAlreadyExistsExceptionHandler(UserAlreadyExistsException ex) {
        log.warn(ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> userNotFoundExceptionHandler(UserNotFoundException ex) {
        log.warn(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(SelfChatNotAllowedException.class)
    public ResponseEntity<?> selfChatNotAllowedException(SelfChatNotAllowedException ex) {
        log.error(ex.getMessage());
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}
