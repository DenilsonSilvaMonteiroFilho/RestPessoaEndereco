package com.Attornatus.Avaliacao.NotFound;

public class PessoaNotFoundException extends RuntimeException {
    public PessoaNotFoundException(Long id){
        super("Could not find " + id);
    }
}
