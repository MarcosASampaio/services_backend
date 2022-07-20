package com.soulcode.Servicos.Services;

import com.soulcode.Servicos.Models.Cliente;
import com.soulcode.Servicos.Repositories.ClienteRepository;
import com.soulcode.Servicos.Services.Exception.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {
    @Autowired
    ClienteRepository clienteRepository;

    @Cacheable("clientesCache") //Só chama o return se o cache expirar
    public List<Cliente> mostrarTodosClientes() {
        return clienteRepository.findAll();
    }

    @Cacheable(value = "clientesCache", key = "#idCliente") //clientesCache::1
    public Cliente mostrarUmClientePeloId(Integer idCliente) {
        Optional<Cliente> cliente = clienteRepository.findById(idCliente);
        return cliente.orElseThrow(
                () -> new EntityNotFoundException("Cliente não cadastrado: " + idCliente)
        );
    }

    public Cliente mostrarUmClientePeloEmail(String email) {
        Optional<Cliente> cliente = clienteRepository.findByEmail(email);
        return cliente.orElseThrow();
    }

    public Cliente cadastrarCliente(Cliente cliente) {
        cliente.setIdCliente(null);
        return clienteRepository.save(cliente);
    }

    @CacheEvict(value = "clienteCache", key = "#idCliente", allEntries = true)
    public void excluirCliente(Integer idCliente) {
        mostrarUmClientePeloId(idCliente);
        clienteRepository.deleteById(idCliente);
    }

    @CachePut(value = "clientesCache", key = "#cliente.idCliente")
    //atualiza(substitui) a info no cache de acordo com a key
    public Cliente editarCliente(Cliente cliente) {
        mostrarUmClientePeloId(cliente.getIdCliente());
        return clienteRepository.save(cliente); //FAZ O CACHE DESSE RETURN
    }

    @CachePut(value = "clientesCache", key = "#cliente.idCliente")
    //atualiza(substitui) a info no cache de acordo com a key
    public Cliente inserirCliente(Cliente cliente) {
        cliente.setIdCliente(null);
        return clienteRepository.save(cliente);
    }
}
