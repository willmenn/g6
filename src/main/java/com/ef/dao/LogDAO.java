package com.ef.dao;


import com.ef.model.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LogDAO {

    private static final String INSERT = "INSERT INTO LOG (ip,log_date,verb,status,device) values(?,?,?,?,?)";

    private static final String SEARCH_IPS = "SELECT ip FROM LOG " +
            "WHERE LOG_DATE " +
            "BETWEEN ? AND DATE_SUB(?, INTERVAL -1 HOUR ) " +
            "group by ip " +
            "HAVING COUNT(ip) > ?";

    private Optional<Connection> getConnection() {
        try {
            return Optional.of(DriverManager.getConnection("jdbc:mysql://localhost/parser",
                    "parseruser", "password"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public void insertLog(List<Log> log) {
        getConnection().ifPresent(connection -> {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(INSERT);
                connection.setAutoCommit(false);
                log.forEach(l -> {
                    try {
                        preparedStatement.setString(1, l.getIp());
                        preparedStatement.setTimestamp(2, Timestamp.valueOf(l.getDate()));
                        preparedStatement.setString(3, l.getVerb());
                        preparedStatement.setString(4, l.getStatus());
                        preparedStatement.setString(5, l.getDevice());
                        preparedStatement.addBatch();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                });

                int[] result = preparedStatement.executeBatch();
                System.out.println("The number of rows inserted: " + result.length);
                connection.commit();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        });

    }

    public Optional<List<String>> searchIpsBetweenDatesAndWithThreshold(LocalDateTime date, Integer threshold) {

        try {
            PreparedStatement preparedStatement = getConnection().get().prepareStatement(SEARCH_IPS);
            preparedStatement.setTimestamp(1, Timestamp.valueOf(date));
            preparedStatement.setTimestamp(2, Timestamp.valueOf(date));
            preparedStatement.setInt(3, threshold);

            ResultSet rs = preparedStatement.executeQuery();
            List<String> ips = new ArrayList<>();
                while (rs.next()) {
                    String ip = rs.getString("ip");
                    ips.add(ip);
                }

                return Optional.of(ips);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }
}
