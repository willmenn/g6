package com.ef.dao;


public class JdbcConnectionException  extends RuntimeException{
    public JdbcConnectionException(String message, Throwable exception) {
        super(message, exception);
    }
}
