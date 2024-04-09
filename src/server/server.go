package server

import (
	"log"

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

    log.Fatal(app.Listen(":6969"))
}

