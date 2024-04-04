package com.rinhajava.app.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rinhajava.app.domain.Pessoa;
import lombok.Data;

@Data
public class PessoaCadastroDB extends Pessoa {

    @JsonProperty("campo_query")
    private String campoQuery;

    public PessoaCadastroDB() {
        super();
    }
}
