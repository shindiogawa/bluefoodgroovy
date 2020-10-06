package br.com.ogawadev.bluefoodgroovy.infrastructure.web.converter

import br.com.ogawadev.bluefoodgroovy.util.FormatUtils
import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component

@Component
class BigDecimalToStringConverter implements Converter<BigDecimal, String>{

    @Override
    String convert(BigDecimal source) {
        return FormatUtils.formatCurrency(source)
    }
}
