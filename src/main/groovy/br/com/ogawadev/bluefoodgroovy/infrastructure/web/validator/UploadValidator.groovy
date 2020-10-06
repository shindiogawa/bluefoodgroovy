package br.com.ogawadev.bluefoodgroovy.infrastructure.web.validator

import br.com.ogawadev.bluefoodgroovy.util.FileType
import org.springframework.web.multipart.MultipartFile

import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class UploadValidator implements ConstraintValidator<UploadConstraint, MultipartFile>{

    private List<FileType> acceptedFileTypes

    @Override
    void initialize(UploadConstraint constraintAnnotation) {
        acceptedFileTypes = Arrays.asList(constraintAnnotation.acceptedTypes())
    }

    @Override
    boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext context) {
        if(multipartFile == null) {
            return true
        }

        for(FileType fileType : acceptedFileTypes) {
            if(fileType.sameOf(multipartFile.getContentType())) {
                return true
            }
        }

        return false
    }
}
