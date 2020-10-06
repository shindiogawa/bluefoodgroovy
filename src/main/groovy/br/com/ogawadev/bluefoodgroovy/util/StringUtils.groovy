package br.com.ogawadev.bluefoodgroovy.util

import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder

class StringUtils {
    static boolean isEmpty(String str) {
        if(str == null) {
            return true
        }
        return str.trim().length() == 0
    }

    static String encrypt(String rawString) {

        if(isEmpty(rawString)) {
            return null
        }

        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder()
        return encoder.encode(rawString)

    }

    static String concatenate(Collection<String> strings) {
        if(strings == null || strings.size() == 0) {
            return null
        }

        StringBuilder sb = new StringBuilder()
        String delimiter = ", "
        boolean first = true

        for (String  string : strings) {
            if(!first) {
                sb.append(delimiter)
            }

            sb.append(string)
            first = false
        }

        return sb.toString()
    }
}
