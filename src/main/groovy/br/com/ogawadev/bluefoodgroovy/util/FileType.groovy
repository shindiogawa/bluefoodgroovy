package br.com.ogawadev.bluefoodgroovy.util

enum FileType {

    PNG("image/png","png"),
    JPG("image/jpeg", "jpg")

    String mimeType
    String extension

    FileType(String mimeType, String extension) {
        this.mimeType = mimeType
        this.extension = extension
    }

    boolean sameOf(String mimeType) {
        return this.mimeType.equalsIgnoreCase(mimeType)
    }

    static FileType of(String mimeType) {

        for(FileType fileType : values()) {
            if(fileType.sameOf(mimeType)) {
                return fileType
            }
        }

        return null
    }
}