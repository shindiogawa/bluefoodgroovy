package br.com.ogawadev.bluefoodgroovy.domain.restaurante

class RestauranteComparator implements Comparator<Restaurante>{
    private SearchFilter filter
    private String cep

    RestauranteComparator(SearchFilter filter, String cep) {

        this.filter = filter
        this.cep = cep
    }

    @Override
    int compare(Restaurante r1, Restaurante r2) {
        int result

        if(filter.order == SearchFilter.Order.Taxa) {
            result = r1.taxaEntrega.compareTo(r2.taxaEntrega)

        } else if(filter.order == SearchFilter.Order.Tempo) {
            result  = r1.tempoEntregaBase.compareTo(r2.calcularTempoEntrega(cep))
        } else  {
            throw new IllegalStateException("O valor de ordenação " + filter.order + " não é válido")
        }

        return filter.isAsc() ? result : -result
    }
}
