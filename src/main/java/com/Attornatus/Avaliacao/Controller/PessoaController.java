package com.Attornatus.Avaliacao.Controller;

import com.Attornatus.Avaliacao.Entities.Endereco;
import com.Attornatus.Avaliacao.Entities.Pessoa;
import com.Attornatus.Avaliacao.Repository.EnderecoRepository;
import com.Attornatus.Avaliacao.Repository.PessoaRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PessoaController {

    private final PessoaRepository pessoaRepository;
    private EnderecoController enderecoController;

    public PessoaController(PessoaRepository pessoaRepository, EnderecoController enderecoController){
        this.pessoaRepository = pessoaRepository;
        this.enderecoController = enderecoController;
    }


    @GetMapping("/all_pessoa")
    List<Pessoa> all(){
        return pessoaRepository.findAll();
    }

    @GetMapping("/pessoa_ById/{id}")
    Pessoa onePessoa(@PathVariable Long id){
        return pessoaRepository.findById(id).orElseThrow();
    }

    @PostMapping("/cadastra_pessoa")
    Pessoa cadastraPessoa(@RequestBody Pessoa newPessoa){
        return pessoaRepository.save(newPessoa);
    }

    @PutMapping("/altera_pessoa/{id}")
    Pessoa alteraPessoa(@RequestBody Pessoa newPessoa, @PathVariable Long id){

        return pessoaRepository.findById(id).map(pessoa -> {
            pessoa.setNome(newPessoa.getNome());
            pessoa.setDataNascimento(newPessoa.getDataNascimento());
            pessoa.setEndereco(newPessoa.getEndereco());
            return pessoaRepository.save(pessoa);
        }).orElseGet(()->{
            newPessoa.setId(id);
            return pessoaRepository.save(newPessoa);
        });
    }

    @DeleteMapping("/deleta_pessoa/{id}")
    void deletaPessoa(@PathVariable Long id){
        pessoaRepository.deleteById(id);
    }
}
