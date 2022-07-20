package com.soulcode.Servicos.Services.Exception;

public class EntityNotFoundException extends RuntimeException{
    public EntityNotFoundException(String msg){
        super(msg);
    }
}
