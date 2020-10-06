package br.com.ogawadev.bluefoodgroovy.application.service

import br.com.ogawadev.bluefoodgroovy.util.IOUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

import java.nio.file.Paths

@Service
class ImageService {

    @Value('${bluefoodgroovy.files.logotipo}')
    String logotiposDir

    @Value('${bluefoodgroovy.files.categoria}')
    String categoriasDir

    @Value('${bluefoodgroovy.files.comida}')
    String comidasDir

    void uploadLogotipo(MultipartFile multipartFile, String fileName) {
        try{
            IOUtils.copy(multipartFile.getInputStream(), fileName, logotiposDir)
        } catch (IOException e) {
            throw new ApplicationServiceException(e)
        }
    }

    void uploadCategoria(MultipartFile multipartFile, String fileName) {
        try{
            IOUtils.copy(multipartFile.getInputStream(), fileName, categoriasDir)
        } catch (IOException e) {
            throw new ApplicationServiceException(e)
        }
    }

    void uploadComida(MultipartFile multipartFile, String fileName) {
        try{
            IOUtils.copy(multipartFile.getInputStream(), fileName, comidasDir)
        } catch (IOException e) {
            throw new ApplicationServiceException(e)
        }
    }

    byte[] getBytes(String type, String imgName) {
        try{
            String dir
            if(type?.equals("comida")) {
                dir = comidasDir
            } else if(type?.equals("logotipo")) {
                dir = logotiposDir
            } else if(type?.equals("categoria")) {
                dir = categoriasDir
            } else {
                throw new Exception(type + " não é um tipo de imagem válido")
            }

            return IOUtils.getBytes(Paths.get(dir, imgName))
        } catch(Exception e) {
            throw new ApplicationServiceException(e)
        }
    }
}
