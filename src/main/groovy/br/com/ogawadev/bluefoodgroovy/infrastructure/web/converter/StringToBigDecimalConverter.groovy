package br.com.ogawadev.bluefoodgroovy.infrastructure.web.converter

import br.com.ogawadev.bluefoodgroovy.util.StringUtils
import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component

@Component
class StringToBigDecimalConverter implements Converter<String, BigDecimal>{

    @Override
    BigDecimal convert(String source) {

        if(StringUtils.isEmpty(source)) {
            return null
        }

        source = source.replace(",", ".").trim()
        return BigDecimal.valueOf(Double.valueOf(source))
    }
}
