package com.ef.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;


@AllArgsConstructor
@Builder
@Getter
@ToString
public class Log {
    private String ip;
    private LocalDateTime date;
    private String verb;
    private String status;
    private String device;
}
