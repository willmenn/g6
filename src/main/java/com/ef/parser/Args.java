package com.ef.parser;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@Getter
public class Args {
    private LocalDateTime startDate;
    private Integer dateRange;
    private Integer threshold;
    private String fileName;
}
