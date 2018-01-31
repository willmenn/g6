package com.ef;


import com.ef.dao.LogDAO;
import com.ef.model.Log;
import com.ef.parser.Args;
import com.ef.parser.CommandLineParser;
import com.ef.parser.FileParser;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Parser {

    public static void main(String[] args) throws IOException {

        List<String> argString = Arrays.asList(args);
        Args argsParsed = CommandLineParser.parseArgs(argString);

        List<Log> logs = new FileParser().parseFile(argsParsed.getFileName());

        LogDAO dao = new LogDAO();
        dao.insertLog(logs);

        Optional<List<String>> ids = dao.searchIpsBetweenDatesAndWithThreshold(argsParsed.getStartDate(),
                argsParsed.getDateRange(),argsParsed.getThreshold());

        System.out.println(ids.toString());
    }
}
