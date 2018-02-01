package com.ef.parser;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@AllArgsConstructor
@Getter
public enum CommandLineParser {
    START_DATE(parseParameter("startDate")),
    DURATION(parseParameter("duration")),
    THRESHOLD(parseParameter("threshold")),
    FILE_NAME(parseParameter("accesslog"));

    private static Function<List<String>, Optional<String>> parseParameter(String parameter) {
        return (value) -> value.stream().filter(arg -> arg.contains(parameter))
                .map(arg1 -> arg1.split("=")[1])
                .findFirst();
    }

    private Function<List<String>, Optional<String>> parser;

    public static Args parseArgs(List<String> args) {
        LocalDateTime parse = parseStartDate(args);
        Integer dateRange = parseDuration(args);
        Integer thresholdInteger = parseThreshold(args);
        String fileName = parseFileName(args);

        return Args.builder()
                .startDate(parse)
                .dateRange(dateRange)
                .threshold(thresholdInteger)
                .fileName(fileName)
                .build();
    }

    private static Integer parseThreshold(List<String> args) {
        String threshold = CommandLineParser.THRESHOLD.getParser().apply(args)
                .orElseThrow(() -> new IllegalArgumentException("Missing threshold."));
        return Integer.parseInt(threshold);
    }

    private static Integer parseDuration(List<String> args) {
        String duration = CommandLineParser.DURATION.getParser().apply(args)
                .orElseThrow(() -> new IllegalArgumentException("Missing duration."));

        Integer dateRange = 0;
        if (duration.equals("hourly")) {
            dateRange = 1;
        } else if (duration.equals("daily")) {
            dateRange = 24;
        }
        return dateRange;
    }

    private static LocalDateTime parseStartDate(List<String> args) {
        String startDate = CommandLineParser.START_DATE.getParser().apply(args)
                .orElseThrow(() -> new IllegalArgumentException("Missing startDate."));
        DateTimeFormatter formatterSearch = DateTimeFormatter.ofPattern("yyyy-MM-dd.HH:mm:ss");
        return LocalDateTime.parse(startDate, formatterSearch);
    }

    private static String parseFileName(List<String> args) {
        return FILE_NAME.getParser().apply(args)
                .orElse("access.log");
    }
}
