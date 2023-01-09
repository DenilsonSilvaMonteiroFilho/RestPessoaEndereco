package com.Attornatus.Avaliacao.Controller;

import com.Attornatus.Avaliacao.Entities.Endereco;
import com.Attornatus.Avaliacao.Entities.Pessoa;
import com.Attornatus.Avaliacao.NotFound.PessoaNotFoundException;
import com.Attornatus.Avaliacao.Repository.PessoaRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class PessoaController {

    private final PessoaRepository pessoaRepository;

    public PessoaController(PessoaRepository pessoaRepository) {
        this.pessoaRepository = pessoaRepository;
    }


    @GetMapping("/all_pessoa")
    List<Pessoa> all(){
        return pessoaRepository.findAll();
    }

    @GetMapping("/pessoa_ById/{id}")
    Pessoa onePessoa(@PathVariable Long id){
        return pessoaRepository.findById(id).
                orElseThrow(() -> new PessoaNotFoundException(id));
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
            pessoa.setListaEndereco(newPessoa.getListaEnderecoEndereco());
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

    //Operacoes para endereco
    @PostMapping("/cadastra_endereco/{id}")
    List <Endereco> cadastraEndereco(@RequestBody Endereco newEndereco,@PathVariable Long id){
        return pessoaRepository.findById(id).map(pessoa -> {
            pessoa.cadastraEndeco(newEndereco);
            pessoaRepository.save(pessoa);
            return pessoa.getListaEnderecoEndereco();
        }).orElseThrow(() -> new PessoaNotFoundException(id));
    }

    @GetMapping("/all_endereco_pessoa/{id}")
    List<Endereco> enderecosPessoa(@PathVariable Long id){
        return pessoaRepository.findById(id).map(pessoa -> pessoa.getListaEnderecoEndereco())
                .orElseThrow(() -> new PessoaNotFoundException(id));
    }

    @GetMapping("/endereco_pricipal_pessoa/{id}")
    Endereco eddePrincipal(@PathVariable Long id){
        return pessoaRepository.findById(id).map(pessoa -> pessoa.enderecoAtual())
                .orElseThrow(() -> new PessoaNotFoundException(id));
    }
}
