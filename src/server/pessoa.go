package server

import "github.com/gofiber/fiber/v3"


func CriarPessoa (c fiber.Ctx) error {

    return c.SendStatus(200)
}

func DetalharPessoa (c fiber.Ctx) error {

    return c.SendStatus(200)
}

func BuscarTermoPessoa (c fiber.Ctx) error {

    return c.SendStatus(200)
}

func ContagemPessoas (c fiber.Ctx) error {

    return c.SendStatus(200)
}
