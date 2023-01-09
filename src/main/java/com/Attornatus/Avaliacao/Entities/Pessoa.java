package com.Attornatus.Avaliacao.Entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.util.AbstractList;
import java.util.ArrayList;
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


    @OneToMany(cascade = CascadeType.ALL)
    private List <Endereco> listaEndereco = new ArrayList<Endereco>();

    public Pessoa(){

    }

    public Pessoa (String nome, Data dataNascimento){
        this.nome = nome;
        this.dataNascimento = dataNascimento;
    }

    public Pessoa (String nome, Data dataNascimento, List <Endereco> endereco){
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.listaEndereco = endereco;
        attEnderecoAtual();
    }

    public Pessoa (String nome, Data dataNascimento, Endereco endereco){
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.listaEndereco.add(endereco);
        this.attEnderecoAtual();
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

    public List <Endereco> getListaEnderecoEndereco() {
        return this.listaEndereco;
    }

    public void setListaEndereco(List<Endereco> newListEndereco){
        this.listaEndereco = newListEndereco;
    }

    public Endereco setEnderecoById( Endereco endereco, Long id) {
        for(Endereco enderecolist: this.listaEndereco){
            if (enderecolist.getId().equals(endereco.getId())){
                return enderecolist = endereco;
            }
        }
        return null;
    }

    public Endereco cadastraEndeco(Endereco endereco){
        Endereco enderecoCadastrar = new Endereco(endereco.getLogradouro()
                ,endereco.getCep(), endereco.getNumero(), endereco.getCidade());
        this.listaEndereco.add(enderecoCadastrar);
        attEnderecoAtual();
        return endereco;
    }

    public boolean attEnderecoAtual(){
        for(Endereco ende: this.listaEndereco){
           if(ende.isAtual()){
               ende.setAtual(false);
           }
        }
        this.listaEndereco.get(this.listaEndereco.size() - 1).setAtual(true);
        return false;
    }

    public Endereco enderecoAtual(){
        for(Endereco end: this.listaEndereco){
            if(end.isAtual()){
                return end;
            }
        }
        return null;
    }

}
