package br.com.ogawadev.bluefoodgroovy.application.service

class ApplicationServiceException extends RuntimeException{

    ApplicationServiceException(String message) {
        super(message)
    }

    ApplicationServiceException(String message, Throwable cause) {
        super(message, cause)
    }

    ApplicationServiceException(Throwable cause) {
        super(cause)
    }
}
