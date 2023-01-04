package com.Attornatus.Avaliacao.Repository;

import com.Attornatus.Avaliacao.Entities.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PessoaRepository extends JpaRepository<Pessoa, Long> {
}
