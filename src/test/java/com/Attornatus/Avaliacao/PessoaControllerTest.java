package com.Attornatus.Avaliacao;

import com.Attornatus.Avaliacao.Entities.Data;
import com.Attornatus.Avaliacao.Entities.Endereco;
import com.Attornatus.Avaliacao.Entities.Pessoa;
import com.Attornatus.Avaliacao.Repository.PessoaRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PessoaControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private PessoaRepository pessoaRepository;
    @Autowired
    private ObjectMapper objectMapper;


    @Test
    public void testCreate() throws Exception {
        // Ciando os objetos
        Endereco endereco = new Endereco("Logradouro 1", "cep 1","numero 1","cidade 1");
        Data dataNascimento = new Data(21, 8,1999);
        Pessoa pessoa = new Pessoa("Pessoa 1",dataNascimento,endereco);

        endereco.setId(1L);
        dataNascimento.setId(1L);
        pessoa.setId(1L);

        // Convertendo o objeto para Json
        String json = objectMapper.writeValueAsString(pessoa);

        // Realizando a requisicao POST
        MvcResult result = mockMvc.perform(post("/cadastra_pessoa")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andReturn();

        // Convertendo o resultado da requisicao
        Pessoa pessoaCriada = objectMapper.readValue
                (result.getResponse().getContentAsString(), Pessoa.class);

        //Testes
        assertEquals("Pessoa 1", pessoaCriada.getNome());
        assertEquals(dataNascimento.toString(), pessoaCriada.getDataNascimento().toString());
        assertEquals("Logradouro 1", pessoaCriada.getListaEnderecoEndereco().get(0).getLogradouro());
        assertEquals("cep 1", pessoaCriada.getListaEnderecoEndereco().get(0).getCep());
        assertEquals("numero 1", pessoaCriada.getListaEnderecoEndereco().get(0).getNumero());
        assertEquals("cidade 1", pessoaCriada.getListaEnderecoEndereco().get(0).getCidade());

    }

    @Test
    public void testRead() throws Exception {
        //Criando os objetos
        Endereco endereco = new Endereco("Logradouro 1", "cep 1","numero 1","cidade 1");
        Data dataNascimento = new Data(21, 8,1999);
        Pessoa pessoa = new Pessoa("Pessoa 1",dataNascimento,endereco);

        //Salvando pessoa no banco
        pessoa = pessoaRepository.save(pessoa);

        //Enviando a requisicao GET
        MvcResult result = mockMvc.perform(get("/pessoa_ById/" + pessoa.getId()))
                .andExpect(status().isOk())
                .andReturn();

        // Convertendo o resultado da requisição para o objeto Pessoa
        Pessoa pessoaLida = objectMapper.readValue(result.getResponse().getContentAsString(), Pessoa.class);

        // Testes
        assertEquals("Pessoa 1", pessoaLida.getNome());
        assertEquals(dataNascimento.toString(), pessoaLida.getDataNascimento().toString());
        assertEquals("Logradouro 1", pessoaLida.getListaEnderecoEndereco().get(0).getLogradouro());
        assertEquals("cep 1", pessoaLida.getListaEnderecoEndereco().get(0).getCep());
        assertEquals("numero 1", pessoaLida.getListaEnderecoEndereco().get(0).getNumero());
        assertEquals("cidade 1", pessoaLida.getListaEnderecoEndereco().get(0).getCidade());
    }

    @Test
    public void testUpdate() throws Exception {
        //Criando os objetos
        Endereco endereco = new Endereco("Logradouro 1", "cep 1","numero 1","cidade 1");
        Data dataNascimento = new Data(21, 8,1999);
        Pessoa pessoa = new Pessoa("Pessoa 1",dataNascimento,endereco);

        //Salvando pessoa no banco
        pessoa = pessoaRepository.save(pessoa);

        // Atualizando a pessoa
        pessoa.setNome("Pessoa Atualizada");
        pessoa.getDataNascimento().setDia(20);
        pessoa.getListaEnderecoEndereco().get(0).setLogradouro("Logradouro Atualizado");

        // Convertendo o resultado da requisicao
        String json = objectMapper.writeValueAsString(pessoa);

        // Enviando uma requisição PUT
        MvcResult result = mockMvc.perform(put("/altera_pessoa/" + pessoa.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andReturn();

        // Convertendo o resultado da requisição
        Pessoa pessoaAtualizada = objectMapper.readValue(result.getResponse().getContentAsString(), Pessoa.class);

        // Testes
        assertEquals("Pessoa Atualizada", pessoaAtualizada.getNome());
        assertEquals(20, pessoaAtualizada.getDataNascimento().getDia());
        assertEquals("Logradouro Atualizado", pessoaAtualizada.getListaEnderecoEndereco().get(0).getLogradouro());

    }

    @Test
    public void testDelete() throws Exception{
        //Criando os objetos
        Endereco endereco = new Endereco("Logradouro 1", "cep 1","numero 1","cidade 1");
        Data dataNascimento = new Data(21, 8,1999);
        Pessoa pessoa = new Pessoa("Pessoa 1",dataNascimento,endereco);

        //Salvando pessoa no banco
        pessoa = pessoaRepository.save(pessoa);

        // Enviando uma requisição DELETE
        mockMvc.perform(delete("/deleta_pessoa/" + pessoa.getId()))
                .andExpect(status().isOk());

        // Testes
        Optional<Pessoa> pessoaOptional = pessoaRepository.findById(pessoa.getId());
        assertFalse(pessoaOptional.isPresent());
    }

    //Testes para operacoes com o endereco do objeto pessoa

    @Test
    public void testeCadastraEndereco() throws Exception{
        //Criando os objetos
        Endereco endereco = new Endereco("Logradouro 1", "cep 1","numero 1","cidade 1");
        Data dataNascimento = new Data(21, 8,1999);
        Pessoa pessoa = new Pessoa("Pessoa 1",dataNascimento,endereco);

        endereco.setId(1L);
        dataNascimento.setId(1L);
        pessoa.setId(1L);

        Endereco enderecoNovo = new Endereco("Logradouro 2", "cep 2","numero 2","cidade 2");
        enderecoNovo.setId(2L);

        //Salvando pessoa no banco
        pessoa = pessoaRepository.save(pessoa);

        // Convertendo o objeto para Json
        String json = objectMapper.writeValueAsString(enderecoNovo);


        // Realizando a requisicao POST para cadastrar o endereco no objeto pesssoa
        MvcResult result = mockMvc.perform(post("/cadastra_endereco/" + pessoa.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andReturn();

        // Enviando a requisicao GET
        result = mockMvc.perform(get("/pessoa_ById/" + pessoa.getId()))
                .andExpect(status().isOk())
                .andReturn();

        // Convertendo o resultado da requisição para o objeto Pessoa
        Pessoa pessoaEndCadastrado = objectMapper.readValue(result.getResponse().getContentAsString(), Pessoa.class);


        // Testes
        assertEquals(2, pessoaEndCadastrado.getListaEnderecoEndereco().size());
        assertEquals(true, pessoaEndCadastrado.getListaEnderecoEndereco().get(1).isAtual());
        assertEquals("Logradouro 2", pessoaEndCadastrado.getListaEnderecoEndereco().get(1).getLogradouro());
        assertEquals("cep 2", pessoaEndCadastrado.getListaEnderecoEndereco().get(1).getCep());
        assertEquals("numero 2", pessoaEndCadastrado.getListaEnderecoEndereco().get(1).getNumero());
        assertEquals("cidade 2", pessoaEndCadastrado.getListaEnderecoEndereco().get(1).getCidade());

    }

    @Test
    public void testeEndenrecosPessoa() throws Exception {
        //Criando os objetos
        Endereco endereco = new Endereco("Logradouro 1", "cep 1","numero 1","cidade 1");
        Endereco endereco2 = new Endereco("Logradouro 2", "cep 2","numero 2","cidade 2");
        Endereco endereco3 = new Endereco("Logradouro 3", "cep 3","numero 3","cidade 3");
        Data dataNascimento = new Data(21, 8,1999);
        Pessoa pessoa = new Pessoa("Pessoa 1",dataNascimento,endereco);

        endereco.setId(1L);
        endereco2.setId(2L);
        endereco3.setId(3L);
        dataNascimento.setId(1L);
        pessoa.setId(1L);

        //Salvando pessoa no banco e cadastrando mais um endereco
        pessoa = pessoaRepository.save(pessoa);
        pessoa.cadastraEndeco(endereco2);

        //Enviando a requisicao GET
        MvcResult result = mockMvc.perform(get("/all_endereco_pessoa/" + pessoa.getId()))
                .andExpect(status().isOk())
                .andReturn();

        // Enviando a requisicao GET
        result = mockMvc.perform(get("/pessoa_ById/" + pessoa.getId()))
                .andExpect(status().isOk())
                .andReturn();

        // Convertendo o resultado da requisição para o objeto Pessoa
        Pessoa pessoaEndCadastrado = objectMapper.readValue(result.getResponse().getContentAsString(), Pessoa.class);


        // Testes
        assertEquals(true, pessoaEndCadastrado.getListaEnderecoEndereco().get(1).isAtual());
        assertEquals(2, pessoa.getListaEnderecoEndereco().size());
        // Cadastra um novo endereco, para fazer a verificacao se esta ocorrendo a troca de endereco atual quando um novo e cadastrado
        pessoa.cadastraEndeco(endereco3);
        assertEquals(true, pessoaEndCadastrado.getListaEnderecoEndereco().get(2).isAtual());
        assertEquals(3, pessoa.getListaEnderecoEndereco().size());
    }
}
