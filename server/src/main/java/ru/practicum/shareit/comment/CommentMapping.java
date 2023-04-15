package ru.practicum.shareit.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CommentInfoDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CommentMapping {

    public Comment mapToComment(CommentDto commentDto, Long itemId, Long userId) {
        return new Comment(
                commentDto.getId(),
                commentDto.getText(),
                itemId,
                userId,
                LocalDateTime.now());
    }

    public CommentInfoDto mapToCommentInfoDto(Comment comment, UserDto user) {
        return new CommentInfoDto(
                comment.getId(),
                comment.getText(),
                comment.getItemId(),
                user.getName(),
                comment.getCreated()
        );
    }
}
