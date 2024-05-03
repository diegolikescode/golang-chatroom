package server

import (
	"log"
	"os"

	"github.com/gofiber/fiber/v3"
)

func Server() {
    app := fiber.New()

    app.Get("/", func (c fiber.Ctx) error { 
	c.Set("Content-Type", "text/html")
	return c.SendString("<h1>justin case</h1>") 
    })

    app.Post("/pessoas", CriarPessoa)
    app.Get("/pessoas/:id", DetalharPessoa)
    app.Get("/pessoas", BuscarTermoPessoa)
    app.Get("/contagem-pessoas", ContagemPessoas)

    port, exists := os.LookupEnv("PORT")
    if !exists {
	log.Fatal("PORT env var not declared")
    } else {
	log.Println("oh wait, THERE IS A PORT")
	log.Println(port)
    }

    log.Fatal(app.Listen(":"+port))
}

