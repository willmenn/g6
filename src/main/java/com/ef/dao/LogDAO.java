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
            "BETWEEN ? AND DATE_SUB(?, INTERVAL -? HOUR ) " +
            "group by ip " +
            "HAVING COUNT(ip) > ?";

    private static final String CONNECTION_ERROR = "The app was unable to connect into Mysql.";
    private static final String INSERT_SQL_ERROR = "A problem occur when trying to insert rows into Mysql.";
    private static final String SEARCH_SQL_ERROR = "A problem occur when trying to search rows into Mysql.";
    private static final String ERROR_PREPARE_STATEMENT = "Error in the prepareStatement.";

    private Connection connection;

    public LogDAO() {
        this.connection = getConnection();
    }

    public void insertLog(List<Log> log) {
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(INSERT);
            this.connection.setAutoCommit(false);

            log.forEach(l -> prepareBatchStatement(l, preparedStatement));

            preparedStatement.executeBatch();
            this.connection.commit();

        } catch (SQLException e) {
            throw new JdbcSQLException(INSERT_SQL_ERROR, e);
        }
    }

    private void prepareBatchStatement(Log log, PreparedStatement preparedStatement) {
        try {
            preparedStatement.setString(1, log.getIp());
            preparedStatement.setTimestamp(2, Timestamp.valueOf(log.getDate()));
            preparedStatement.setString(3, log.getVerb());
            preparedStatement.setString(4, log.getStatus());
            preparedStatement.setString(5, log.getDevice());
            preparedStatement.addBatch();
        } catch (SQLException e) {
            throw new JdbcSQLException(ERROR_PREPARE_STATEMENT, e);
        }
    }

    public Optional<List<String>> searchIpsBetweenDatesAndWithThreshold(LocalDateTime date, Integer dateRange,
                                                                        Integer threshold) {

        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(SEARCH_IPS);
            preparedStatement.setTimestamp(1, Timestamp.valueOf(date));
            preparedStatement.setTimestamp(2, Timestamp.valueOf(date));
            preparedStatement.setInt(3, dateRange);
            preparedStatement.setInt(4, threshold);

            ResultSet rs = preparedStatement.executeQuery();
            List<String> ips = new ArrayList<>();

            while (rs.next()) {
                String ip = rs.getString("ip");
                ips.add(ip);
            }

            return Optional.of(ips);
        } catch (SQLException e) {
            throw new JdbcSQLException(SEARCH_SQL_ERROR, e);
        }
    }

    private Connection getConnection() {
        try {
            return DriverManager.getConnection("jdbc:mysql://localhost/parser",
                    "parseruser", "password");
        } catch (SQLException e) {
            throw new JdbcConnectionException(CONNECTION_ERROR, e);
        }
    }
}
