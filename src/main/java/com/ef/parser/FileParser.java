package com.ef.parser;


import com.ef.model.Log;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

public class FileParser {

    public List<Log> parseFile(String filePath){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        Path path = Paths.get(filePath);
        try {
            return Files.lines(path)
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
        } catch (IOException e) {
            throw new IllegalArgumentException(format("Could not parse or find the file %s.",filePath));
        }
    }
}
