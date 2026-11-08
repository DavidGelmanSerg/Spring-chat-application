package ru.gelman.spring_chat_application.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import ru.gelman.spring_chat_application.domain.exception.EmptyMemberListException;
import ru.gelman.spring_chat_application.domain.exception.GroupChatWithoutOwnerCreationException;
import ru.gelman.spring_chat_application.domain.service.GroupChatPolicy;

import java.util.Collections;
import java.util.List;
import java.util.stream.LongStream;

public class GroupChatPolicyTests {
    private final GroupChatPolicy groupChatPolicy = new GroupChatPolicy();

    @Test
    void shouldBeSuccessAssertionAndDoNothingTest() {
        List<Long> userIds = LongStream.range(1, 6).boxed().toList();
        Long ownerId = userIds.get(0);
        Assertions.assertDoesNotThrow(() -> groupChatPolicy.assertCanCreateGroupChatFor(userIds, ownerId));
    }

    @Test
    void shouldThrowEmptyMemberListExceptionTest() {
        Executable exec = () -> groupChatPolicy.assertCanCreateGroupChatFor(Collections.emptyList(), null);
        Assertions.assertThrows(EmptyMemberListException.class, exec);
    }

    @Test
    void shouldThrowGroupChatWithoutOwnerCreationException() {
        List<Long> userIds = LongStream.range(1, 6).boxed().toList();
        Executable execNullOwnerId = () -> groupChatPolicy.assertCanCreateGroupChatFor(userIds, null);
        Executable execNonNullOwnerId = () -> groupChatPolicy.assertCanCreateGroupChatFor(userIds, 100L);
        Assertions.assertThrows(GroupChatWithoutOwnerCreationException.class, execNullOwnerId);
        Assertions.assertThrows(GroupChatWithoutOwnerCreationException.class, execNonNullOwnerId);
    }
}
