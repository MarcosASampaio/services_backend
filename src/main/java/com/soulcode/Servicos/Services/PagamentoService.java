package com.soulcode.Servicos.Services;

import com.soulcode.Servicos.Models.Chamado;
import com.soulcode.Servicos.Models.Pagamento;
import com.soulcode.Servicos.Models.StatusPagamento;
import com.soulcode.Servicos.Repositories.ChamadoRepository;
import com.soulcode.Servicos.Repositories.PagamentoRepository;
import com.soulcode.Servicos.Services.Exception.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PagamentoService {

    @Autowired
    PagamentoRepository pagamentoRepository;

    @Autowired
    ChamadoRepository chamadoRepository;

//primeiro serviço -> mostrar todos os registros de pagamento

    @Cacheable("pagamentosCache")
    public List<Pagamento> mostrarTodosPagamentos(){
        return pagamentoRepository.findAll();
    }
    @Cacheable(value = "pagamentosCache", key = "#idPagamento")
    public Pagamento mostrarPagamentoPeloId(Integer idPagamento){
        Optional<Pagamento> pagamento = pagamentoRepository.findById(idPagamento);
        return pagamento.orElseThrow(
                () -> new EntityNotFoundException("Pagamento não cadastrado" + idPagamento)
        );
    }

    @Cacheable(value = "pagamentosCache", key = "#status")
    public List<Pagamento> mostrarPagamentosPeloStatus(String status){
        return pagamentoRepository.findByStatus(status);
    }

    @CachePut(value = "pagamentosCache", key = "#idChamado")
    public Pagamento cadastrarPagamento(Pagamento pagamento, Integer idChamado) {
        Optional<Chamado> chamado = chamadoRepository.findById(idChamado);
        if (chamado.isPresent()) {
        pagamento.setIdPagamento(idChamado);
        pagamento.setStatus(StatusPagamento.LANCADO);
        pagamentoRepository.save(pagamento);

        chamado.get().setPagamento(pagamento);
        chamadoRepository.save(chamado.get());
        return pagamento;
        }else {
            throw new RuntimeException();
        }
    }

    @CacheEvict(value = "pagamentoCache", key = "#pagamento.idPagamento")
    public Pagamento editarPagamento(Pagamento pagamento){
        return pagamentoRepository.save(pagamento);
    }

//    public Pagamento quitarPagamento(Integer idPagamento){
//        Pagamento pagamento = mostrarPagamentoPeloId(idPagamento);
//        pagamento.setStatus(StatusPagamento.QUITADO);
//        return pagamentoRepository.save(pagamento);
//    }

    @CachePut(value = "pagamentosCache", key = "#idPagamento")
    public Pagamento modificarStatusPagamento(Integer idPagamento, String status){
        Pagamento pagamento = mostrarPagamentoPeloId(idPagamento);

            switch (status){
                case "LANCADO":
                    pagamento.setStatus(StatusPagamento.LANCADO);
                    break;
                case "QUITADO":
                    pagamento.setStatus(StatusPagamento.QUITADO);
                    break;
            }
            return pagamentoRepository.save(pagamento);
    }

    @Cacheable(value = "pagamentosCache", key = "orcamentoComServicoCliente")
    public List<List>orcamentoComServicoCliente(){
        return pagamentoRepository.orcamentoComServicoCliente();
    }

}
