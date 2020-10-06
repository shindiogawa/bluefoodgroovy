package br.com.ogawadev.bluefoodgroovy.application.service

class PagamentoException extends  Exception{


    PagamentoException() {
    }

    PagamentoException(String var1) {
        super(var1)
    }

    PagamentoException(String var1, Throwable var2) {
        super(var1, var2)
    }

    PagamentoException(Throwable var1) {
        super(var1)
    }
}
