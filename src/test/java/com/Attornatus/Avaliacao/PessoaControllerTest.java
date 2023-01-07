package com.Attornatus.Avaliacao;

import com.Attornatus.Avaliacao.Entities.Data;
import com.Attornatus.Avaliacao.Entities.Endereco;
import com.Attornatus.Avaliacao.Entities.Pessoa;
import com.Attornatus.Avaliacao.Repository.PessoaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

}
