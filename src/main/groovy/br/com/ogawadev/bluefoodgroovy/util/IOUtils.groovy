package br.com.ogawadev.bluefoodgroovy.util

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

class IOUtils {

    static void copy(InputStream inputStream, String fileName, String outputDir) throws IOException {
        Files.copy(inputStream, Paths.get(outputDir, fileName), StandardCopyOption.REPLACE_EXISTING)
    }
    
    static byte[] getBytes(Path path) throws IOException {
        return Files.readAllBytes(path)
    }
}
