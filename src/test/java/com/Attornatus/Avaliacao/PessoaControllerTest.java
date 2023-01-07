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
        Endereco endereco = new Endereco("Logradouro 1", "cep 1","numero 1");
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
        assertEquals("Logradouro 1", pessoaCriada.getEndereco().getLogradouro());
        assertEquals("cep 1", pessoaCriada.getEndereco().getCep());
        assertEquals("numero 1", pessoaCriada.getEndereco().getNumero());

    }

    @Test
    public void testRead() throws Exception {
        //Criando os objetos
        Endereco endereco = new Endereco("Logradouro 1", "cep 1","numero 1");
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
        assertEquals("Logradouro 1", pessoaLida.getEndereco().getLogradouro());
        assertEquals("cep 1", pessoaLida.getEndereco().getCep());
        assertEquals("numero 1", pessoaLida.getEndereco().getNumero());
    }

    @Test
    public void testUpdate() throws Exception {
        //Criando os objetos
        Endereco endereco = new Endereco("Logradouro 1", "cep 1","numero 1");
        Data dataNascimento = new Data(21, 8,1999);
        Pessoa pessoa = new Pessoa("Pessoa 1",dataNascimento,endereco);

        //Salvando pessoa no banco
        pessoa = pessoaRepository.save(pessoa);

        // Atualizando a pessoa
        pessoa.setNome("Pessoa Atualizada");
        pessoa.getDataNascimento().setDia(20);
        pessoa.getEndereco().setLogradouro("Logradouro Atualizado");

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
        assertEquals("Logradouro Atualizado", pessoaAtualizada.getEndereco().getLogradouro());

    }

    @Test
    public void testDelete() throws Exception{
        //Criando os objetos
        Endereco endereco = new Endereco("Logradouro 1", "cep 1","numero 1");
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
}
