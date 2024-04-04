package com.rinhajava.app.utils;

public enum Query {
    INSERT_PESSOA("SELECT * FROM insert_pessoa(?, ?, ?, ?, ?)"),
    SELECT_BY_ID("SELECT * FROM select_by_id(?)"),
    SELECT_BY_T("SELECT * FROM select_by_t(?)");

    private final String sql;

    Query(String sql) {
        this.sql = sql;
    }

    public String getSql() {
        return this.sql;
    }
}
