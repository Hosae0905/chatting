package com.example.backend.chat.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SocketDto {
    private String userName;
    private String content;
}
