package com.ef.dao;


public class JdbcSQLException extends RuntimeException {
    public JdbcSQLException(String msg, Throwable exception) {
        super(msg, exception);
    }
}
