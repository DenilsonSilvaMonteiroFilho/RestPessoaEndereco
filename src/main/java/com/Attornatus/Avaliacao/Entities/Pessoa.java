package com.Attornatus.Avaliacao.Entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
public class Pessoa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    @OneToOne(cascade = CascadeType.ALL)
    private Data dataNascimento;


    @OneToOne(cascade = CascadeType.ALL)
    private Endereco endereco;

    public Pessoa(){

    }

    public Pessoa (String nome, Data dataNascimento, Endereco endereco){
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.endereco = endereco;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public String getNome(){ return this.nome;}

    public void setNome(String nome){
        this.nome = nome;
    }

    public Data getDataNascimento() {
        return this.dataNascimento;
    }

    public void setDataNascimento(Data dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public Endereco getEndereco() {
        return this.endereco;
    }

    public void setEndereco( Endereco endereco) {
        this.endereco = endereco;
    }
}
