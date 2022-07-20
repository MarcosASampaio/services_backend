package com.soulcode.Servicos.Repositories;

import com.soulcode.Servicos.Models.EnderecoCliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EnderecoClienteRepository extends JpaRepository<EnderecoCliente, Integer> {
    Optional<EnderecoCliente> findByRua(String rua);
}
