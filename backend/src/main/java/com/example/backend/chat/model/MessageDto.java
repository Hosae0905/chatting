package com.example.backend.chat.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MessageDto {
    private String message;
    private String writer;
}
