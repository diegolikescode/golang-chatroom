package config

import (
	"database/sql"
	"fmt"
	"log"
	"os"
    _ "github.com/lib/pq"
)

var Connection *sql.DB

func PostgresConn () {
    var err error

    host, exists := os.LookupEnv("DB_HOST")
    if !exists {
        log.Fatal("Problem looking up for DB_HOST in .env file")
    }
    port := 5432
    user := "postgres"
    pass := "postgres"
    dbname := "rinha"

    connInfo := fmt.Sprintf("host=%s port=%d user=%s password=%s dbname=%s sslmode=disable", host, port, user, pass, dbname)
    Connection, err = sql.Open("postgres", connInfo)
    if err != nil {
        log.Fatal("FATAL:: error in the database connection", err.Error())
    } else {
        log.Print("Database CONNECTED")
    }
}

