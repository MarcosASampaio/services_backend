package com.soulcode.Servicos.Repositories;

import com.soulcode.Servicos.Models.Cargo;
import com.soulcode.Servicos.Models.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Integer> {

    Optional<Funcionario> findByEmail(String email);


    List<Funcionario> findByCargo(Optional<Cargo> cargo);


    //Optional<Funcionario> findbyNome(String nome);

    //Optional<Funcionario> findByNomeAndEmailAndFoto(String nome, String email, String foto);


}




