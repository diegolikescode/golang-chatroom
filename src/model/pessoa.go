package model


type Pessoa struct {
    Id          string `json:"id"`
    Apelido     string `json:"apelido"`
    Nome        string `json:"nome"`
    Nascimento  string `json:"nascimento"`
    Stack       []string `json:"stack"`
}

type PessoaCadastro struct {
    Id          string `json:"id"`
    Apelido     string `json:"apelido"`
    Nome        string `json:"nome"`
    Nascimento  string `json:"nascimento"`
    Stack       string `json:"stack"`
    CampoBusca  string
}
