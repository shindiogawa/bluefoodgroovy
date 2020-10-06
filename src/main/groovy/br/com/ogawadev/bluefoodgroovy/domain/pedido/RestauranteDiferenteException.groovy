package br.com.ogawadev.bluefoodgroovy.domain.pedido

class RestauranteDiferenteException extends Exception{

    RestauranteDiferenteException() {
    }

    RestauranteDiferenteException(String message) {
        super(message)
    }
}
