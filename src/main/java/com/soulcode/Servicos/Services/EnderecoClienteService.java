package com.soulcode.Servicos.Services;

import com.soulcode.Servicos.Models.Cliente;
import com.soulcode.Servicos.Models.EnderecoCliente;
import com.soulcode.Servicos.Repositories.ClienteRepository;
import com.soulcode.Servicos.Repositories.EnderecoClienteRepository;
import com.soulcode.Servicos.Services.Exception.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EnderecoClienteService {


    @Autowired
    EnderecoClienteRepository enderecoClienteRepository;

    @Autowired
    ClienteRepository clienteRepository;

    @Cacheable("enderecoClienteCache")
    public List<EnderecoCliente> mostrarTodosEnderecosCliente(){
        return enderecoClienteRepository.findAll();
    }
    @Cacheable(value = "enderecoClienteCache", key ="#idEnderecoCliente")
    public EnderecoCliente mostrarUmEnderecoClientePeloId(Integer idEnderecoCliente){
        Optional<EnderecoCliente> enderecoCliente = enderecoClienteRepository.findById(idEnderecoCliente);
        return enderecoCliente.orElseThrow(
                () -> new EntityNotFoundException("nenhum endereço cadastrado" + idEnderecoCliente)
        );
    }

    @Cacheable(value = "enderecoClienteCache", key = "#idEnderecoCliente")
    public EnderecoCliente mostrarUmEnderecoClientePelaRua(String rua){
        Optional<EnderecoCliente> enderecoCliente = enderecoClienteRepository.findByRua(rua);
        return enderecoCliente.orElseThrow();
    }

    // CADASTRO DE UM NOVO ENDEREÇO
    //regra 1 -> Para cadastrar um endereço, o cliente já deve estar cadastrado no database
    //regra 2 -> No momento do cadastro do enredeço, precisamos passar o ID do cliente dono desse endereço
    //regra 3 -> O ID do endereço vai ser o mesmo id do cliente
    //regra 4 -> Não permitir que um endereço seja salvo sem a existência do respectivo cliente

    @CachePut(value = "enderecoClienteCache", key = "#idCliente")
    public EnderecoCliente cadastrarEnderecoDoCliente(EnderecoCliente enderecoCliente, Integer idCliente) throws Exception {

        //estamos declarando um optional de cliente e atribuindo para este os dados do cliente que receberá o novo endereço

        Optional<Cliente> cliente = clienteRepository.findById(idCliente);
        if(cliente.isPresent()){
            enderecoCliente.setIdEndereco(idCliente);
            enderecoClienteRepository.save(enderecoCliente);


            cliente.get().setEnderecoCliente(enderecoCliente);
            clienteRepository.save(cliente.get());
            return enderecoCliente;

        }else {
            throw new Exception();
        }

    }

    @Cacheable(value = "enderecoClienteCache", key = "#enderecoCliente.idEnderecoCliente")
    public EnderecoCliente editarEndereco(EnderecoCliente enderecoCliente){
        return enderecoClienteRepository.save(enderecoCliente);
    }

}
