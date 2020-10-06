package br.com.ogawadev.bluefoodgroovy.util



import java.text.NumberFormat

class FormatUtils {

    private static final Locale LOCALE_BRAZIL = new Locale("pt", "BR")

    static NumberFormat newCurrencyFormat() {
        NumberFormat nf = NumberFormat.getNumberInstance(LOCALE_BRAZIL)

        nf.setMinimumFractionDigits(2)
        nf.setMaximumFractionDigits(2)
        nf.setGroupingUsed(false)

        return nf
    }

    static String formatCurrency(BigDecimal number) {
        if(number == null) {
            return null
        }

        return newCurrencyFormat().format(number)
    }
}
