package com.soulcode.Servicos.Services.Exception;

public class DataIntegrityViolationException extends RuntimeException{

    public DataIntegrityViolationException(String msg){
        super(msg);
    }
}
