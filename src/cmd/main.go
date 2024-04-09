package main

import (
	"fmt"
	"log"
	"os"

	"github.com/diegolikescode/rinha2023q1_sql/src/config"
	"github.com/diegolikescode/rinha2023q1_sql/src/server"
	"github.com/joho/godotenv"
)

func main() {
    fmt.Println("start program")
	var envErr error

    if len(os.Args) >= 2 && os.Args[1] == "docker" {
        envErr = godotenv.Load(".docker.env")
    } else {
        envErr = godotenv.Load(".local.env")
    }
	
	if envErr != nil {
		log.Println("No .env file found")
	}

	config.PostgresConn()

    server.Server()
}

