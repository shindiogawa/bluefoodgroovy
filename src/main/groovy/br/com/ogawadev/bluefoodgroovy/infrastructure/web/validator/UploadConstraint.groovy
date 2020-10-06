package br.com.ogawadev.bluefoodgroovy.infrastructure.web.validator

import br.com.ogawadev.bluefoodgroovy.util.FileType

import javax.validation.Constraint
import javax.validation.Payload
import java.lang.annotation.Documented
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target
import java.lang.annotation.ElementType

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target([ElementType.FIELD,ElementType.METHOD])
@Constraint(validatedBy = UploadValidator.class)
@interface UploadConstraint {

    String message() default "Arquivo inválido"

    FileType[] acceptedTypes()

    Class<?>[] groups() default []
    Class<? extends Payload>[] payload() default []

}