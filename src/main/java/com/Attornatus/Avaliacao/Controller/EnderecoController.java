package com.Attornatus.Avaliacao.Controller;

import com.Attornatus.Avaliacao.Entities.Endereco;
import com.Attornatus.Avaliacao.Repository.EnderecoRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class EnderecoController {

    private final EnderecoRepository enderecoRepository;

    public EnderecoController(EnderecoRepository enderecoRepository){
        this.enderecoRepository = enderecoRepository;
    }

    @GetMapping("/all_enderecos")
    List<Endereco> all(){
        return enderecoRepository.findAll();
    }

    @GetMapping("/endereco_ById/{id}")
    Endereco oneEndereco(@PathVariable Long id){
        return enderecoRepository.findById(id).orElseThrow();
    }

    @PostMapping("/cadastra_endereco")
    Endereco cadastraEndereco(@RequestBody Endereco newEndereco){
        return enderecoRepository.save(newEndereco);
    }

    @PutMapping("/altera_endereco/{id}")
    Endereco alteraEndereco(@RequestBody Endereco newEndereco, @PathVariable Long id){

        return enderecoRepository.findById(id).map(endereco -> {
            endereco.setLogradouro(newEndereco.getLogradouro());
            endereco.setCep(newEndereco.getCep());
            endereco.setNumero(newEndereco.getNumero());
            return enderecoRepository.save(endereco);
        }).orElseGet(()->{
            newEndereco.setId(id);
            return enderecoRepository.save(newEndereco);
        });
    }

    @DeleteMapping("/deleta_endereco/{id}")
    void deletaEndereco(@PathVariable Long id){
        enderecoRepository.deleteById(id);
    }

}
