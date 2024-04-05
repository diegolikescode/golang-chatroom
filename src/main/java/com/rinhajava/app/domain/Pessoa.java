package com.rinhajava.app.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.UUID;

@Data
public class Pessoa {

    @JsonProperty("id")
    private UUID id;

    @JsonProperty("apelido")
    private String apelido;

    @JsonProperty("nome")
    private String nome;

    @JsonProperty("nascimento")
    private String nascimento;

    @JsonProperty("stack")
    private String[] stack;

    public Pessoa() {

    }

    public Pessoa(UUID id, String apelido, String nome, String nascimento, String[] stack) {
        this.id = id;
        this.apelido = apelido;
        this.nome = nome;
        this.nascimento = nascimento;
        this.stack = stack;
    }
}
