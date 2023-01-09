package com.Attornatus.Avaliacao.LoadDataBase;

import com.Attornatus.Avaliacao.Entities.Data;
import com.Attornatus.Avaliacao.Entities.Endereco;
import com.Attornatus.Avaliacao.Entities.Pessoa;
import com.Attornatus.Avaliacao.Repository.PessoaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadDataBasePessoa {
/*PreLoad para realizar teste pelo POSTMAN*/
    private static final Logger log = LoggerFactory.getLogger(LoadDataBasePessoa.class);

    @Bean
    CommandLineRunner initDatabase(PessoaRepository repository){
        return args -> {
            log.info("Pre carregando " + repository.save(new Pessoa("Pessoa 1", new Data(21,8,1999)
                    , new Endereco("logradouro 1","cep 1","numero 1","cidade 1"))));
            log.info("Pre carregando " + repository.save(new Pessoa("Pessoa 2", new Data(25,11,2004)
                    , new Endereco("logradouro 2","cep 2","numero 2","cidade 2"))));
            log.info("Pre carregando " + repository.save(new Pessoa("Pessoa 3", new Data(24,5,2013)
                    , new Endereco("logradouro 3","cep 3","numero 3","cidade 3"))));
        };
    }
}
