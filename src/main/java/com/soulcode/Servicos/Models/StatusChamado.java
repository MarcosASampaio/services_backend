package com.soulcode.Servicos.Models;

import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

public enum StatusChamado {

    RECEBIDO("Recebido"),
    ATRIBUIDO("Atribuido"),
    CONCLUIDO("Concluido"),
    ARQUIVADO("ARQUIVADO");

    private String conteudo;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_pagamento", unique = true)
    private Pagamento pagamento;

    StatusChamado(String conteudo){
        this.conteudo = conteudo;
    }

    public String getConteudo() {
        return conteudo;
    }


}
