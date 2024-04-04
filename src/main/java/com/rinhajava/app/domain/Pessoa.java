package com.rinhajava.app.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import lombok.Data;

@Data
public class Pessoa {

    @JsonProperty("apelido")
    private String apelido;

    @JsonProperty("nome")
    private String nome;

    @JsonProperty("nascimento")
    private String nascimento;

    @JsonProperty("stack")
    private String[] stack;

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
