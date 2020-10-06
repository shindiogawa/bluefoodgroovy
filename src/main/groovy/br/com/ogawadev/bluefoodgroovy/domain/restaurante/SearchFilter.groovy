package br.com.ogawadev.bluefoodgroovy.domain.restaurante

import br.com.ogawadev.bluefoodgroovy.util.StringUtils
import lombok.Data

@Data
class SearchFilter {

    enum SearchType {
        Texto,
        Categoria
    }

    enum Order {
        Taxa,
        Tempo
    }

    enum Command {
        EntregaGratis,
        MaiorTaxa,
        MenorTaxa,
        MaiorTempo,
        MenorTempo
    }

    String texto
    SearchType searchType
    Integer categoriaId
    Order order = Order.Taxa
    boolean asc
    boolean entregaGratis

    void processFilter(String cmdString) {
        if(!StringUtils.isEmpty(cmdString)) {
            Command cmd = Command.valueOf(cmdString)
            if(cmd == Command.EntregaGratis){
                entregaGratis = !entregaGratis
            } else if (cmd == Command.MaiorTaxa) {
                order = Order.Taxa
                asc = false
            } else if(cmd == Command.MenorTaxa) {
                order = Order.Taxa
                asc = true
            } else if(cmd == Command.MaiorTempo) {
                order = Order.Tempo
                asc = false
            } else if(cmd == Command.MenorTempo) {
                order = Order.Tempo
                asc = true
            }
        }

        if(searchType == SearchType.Texto) {
            categoriaId = null
        } else if(searchType == SearchType.Categoria) {
            texto = null
        }
    }
}
