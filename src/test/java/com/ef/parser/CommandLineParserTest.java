package com.ef.parser;


import org.junit.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static com.google.common.collect.Lists.newArrayList;
import static java.lang.Integer.parseInt;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertEquals;

public class CommandLineParserTest {

    @Test
    public void shouldBeAbleToParseStringGivenSpecificFormat() throws Exception {
        List<String> args = newArrayList();
        String date = "2017-01-01.13:00:00";
        args.add("--startDate=" + date);
        Optional<String> response = CommandLineParser.START_DATE.getParser().apply(args);
        assertEquals(date,response.get());
    }

    @Test
    public void shouldBeAbleToParseArgsPassedInTheAppStartUp() throws Exception {
        List<String> args = newArrayList();

        String date = "2017-01-01.13:00:10";
        args.add("--startDate=" + date);

        String duration = "hourly";
        args.add("--duration=" + duration);

        String threshold = "200";
        args.add("--threshold=" + threshold);

        Args argsParsed = CommandLineParser.parseArgs(args);

        DateTimeFormatter formatterSearch = DateTimeFormatter.ofPattern("yyyy-MM-dd.HH:mm:ss");
        LocalDateTime expectedDate = LocalDateTime.parse(date, formatterSearch);
        int durationIntervalOfHour = 1;
        String defaultFileName = "access.log";

        assertEquals(expectedDate,argsParsed.getStartDate());
        assertEquals(durationIntervalOfHour,argsParsed.getDateRange().intValue());
        assertEquals(parseInt(threshold),argsParsed.getThreshold().intValue());
        assertEquals(defaultFileName,argsParsed.getFileName());

    }

    @Test
    public void shouldBeAbleToParseArgsPassedInTheAppStartUpWithASpecificFileName() throws Exception {
        List<String> args = newArrayList();

        String date = "2017-01-01.13:00:10";
        args.add("--startDate=" + date);

        String duration = "hourly";
        args.add("--duration=" + duration);

        String threshold = "200";
        args.add("--threshold=" + threshold);

        String fileName = "newFileName.txt";
        args.add("--accesslog=" + fileName);

        Args argsParsed = CommandLineParser.parseArgs(args);

        DateTimeFormatter formatterSearch = DateTimeFormatter.ofPattern("yyyy-MM-dd.HH:mm:ss");
        LocalDateTime expectedDate = LocalDateTime.parse(date, formatterSearch);
        int durationIntervalOfHour = 1;

        assertEquals(expectedDate,argsParsed.getStartDate());
        assertEquals(durationIntervalOfHour,argsParsed.getDateRange().intValue());
        assertEquals(parseInt(threshold),argsParsed.getThreshold().intValue());
        assertEquals(fileName,argsParsed.getFileName());

    }

    @Test
    public void shouldNotBeAbleToParseArgsPassedInTheAppStartUpGivenNoStartDate() throws Exception {
        List<String> args = newArrayList();

        String duration = "hourly";
        args.add("--duration=" + duration);

        String threshold = "200";
        args.add("--threshold=" + threshold);

        assertThatThrownBy(() -> CommandLineParser.parseArgs(args))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Missing startDate.");
    }

    @Test
    public void shouldNotBeAbleToParseArgsPassedInTheAppStartUpGivenNoDuration() throws Exception {
        List<String> args = newArrayList();

        String date = "2017-01-01.13:00:10";
        args.add("--startDate=" + date);

        String threshold = "200";
        args.add("--threshold=" + threshold);

        assertThatThrownBy(() -> CommandLineParser.parseArgs(args))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Missing duration.");
    }

    @Test
    public void shouldNotBeAbleToParseArgsPassedInTheAppStartUpGivenNoThreshold() throws Exception {
        List<String> args = newArrayList();

        String date = "2017-01-01.13:00:10";
        args.add("--startDate=" + date);

        String duration = "hourly";
        args.add("--duration=" + duration);

        assertThatThrownBy(() -> CommandLineParser.parseArgs(args))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Missing threshold.");
    }
}
