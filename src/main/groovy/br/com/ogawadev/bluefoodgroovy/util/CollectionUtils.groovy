package br.com.ogawadev.bluefoodgroovy.util

import java.util.stream.Collectors

class CollectionUtils {
    @SuppressWarnings("unchecked")
    static <T> List<T> listOf(T... objs) {
        if(objs == null) {
            return Collections.emptyList()
        }
        return Arrays.stream(objs).collect(Collectors.toList())
    }
}
