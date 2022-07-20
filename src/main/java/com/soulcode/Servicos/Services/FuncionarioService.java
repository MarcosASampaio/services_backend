package com.soulcode.Servicos.Services;

import com.soulcode.Servicos.Models.Cargo;
import com.soulcode.Servicos.Models.Chamado;
import com.soulcode.Servicos.Models.Funcionario;
import com.soulcode.Servicos.Repositories.CargoRepository;
import com.soulcode.Servicos.Repositories.FuncionarioRepository;
import com.soulcode.Servicos.Services.Exception.DataIntegrityViolationException;
import com.soulcode.Servicos.Services.Exception.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

//Quando se fala em serviços, estamos falando dos métodos do crud da tabela
@Service
public class FuncionarioService {

    // aqui se faz a injeção de dependência
    @Autowired
    FuncionarioRepository funcionarioRepository;

    @Autowired
    CargoRepository cargoRepository;

    //primeiro serviço na tabela de funcionários vai ser a leitura de todos os funcionarios cadastrados
    //findALL -> método do spring JPA -> busca todos os registros de uma tabela
    public List<Funcionario> mostrarTodosFuncionarios() {
        return funcionarioRepository.findAll();
    }
    //Vamos criar mais um serviço relacionado ao funcionário
    //criar um serviço de buscar apenas um funcionário pelo seu id(chave primária)

    public Funcionario mostrarUmFuncionararioPeloId(Integer idFuncionario) {
        Optional<Funcionario> funcionario = funcionarioRepository.findById(idFuncionario);
        return funcionario.orElseThrow(
                () -> new EntityNotFoundException("Funcionário não cadastrado: " + idFuncionario)
        );
    }

    //vamos criar mais um serviço para buscar um funcionário pelo seu email

    public Funcionario mostrarUmFuncionarioPeloEmail(String email){
        Optional<Funcionario> funcionario = funcionarioRepository.findByEmail(email);
        return funcionario.orElseThrow();
    }

    public List<Funcionario> mostrarTodosFuncionariosDeUmCargo(Integer idCargo){
        Optional<Cargo> cargo = cargoRepository.findById(idCargo);
        return funcionarioRepository.findByCargo(cargo);
    }

    //Vamos criar um serviço para cadastrar um novo funcionário

    public Funcionario cadastrarFuncionario(Funcionario funcionario, Integer idCargo){
        try {
            Cargo cargo = cargoRepository.findById(idCargo).get();
            funcionario.setCargo(cargo);
            return funcionarioRepository.save(funcionario);
        }catch (Exception e){
            throw new DataIntegrityViolationException("Erro ao cadastrar funcionário");
        }

    }

    public void excluirFuncionario(Integer idFuncionario){
        funcionarioRepository.deleteById(idFuncionario);
    }

    public Funcionario editarFuncionario(Funcionario funcionario){
        return funcionarioRepository.save(funcionario);
    }

    public Funcionario salvarFoto(Integer idFuncionario, String caminhoFoto){
        Funcionario funcionario = mostrarUmFuncionararioPeloId(idFuncionario);
        funcionario.setFoto(caminhoFoto);
        return funcionarioRepository.save(funcionario);
    }
}

