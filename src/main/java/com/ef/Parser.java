package com.ef;


import com.ef.dao.LogDAO;
import com.ef.model.Log;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public class Parser {

    public static void main(String[] args) throws IOException {


        DateTimeFormatter formatterSearch = DateTimeFormatter.ofPattern("yyyy-MM-dd.HH:mm:ss");
        LocalDateTime parse = LocalDateTime.parse("2017-01-01.15:00:00", formatterSearch);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        Path path = Paths.get("access.log");
        List<Log> logs = Files.lines(path)
                .map(line -> {
                    String[] split = line.split("\\|");
                    return Log.builder()
                            .ip(split[1])
                            .date(LocalDateTime.parse(split[0], formatter))
                            .verb(split[2])
                            .status(split[3])
                            .device(split[4])
                            .build();
                }).collect(toList());
        LogDAO dao = new LogDAO();
        dao.insertLog(logs);

        Optional<List<String>> ids = dao.searchIpsBetweenDatesAndWithThreshold(parse, 200);

        System.out.println(ids.toString());


    }

}
