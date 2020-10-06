package br.com.ogawadev.bluefoodgroovy.domain.pedido

import br.com.ogawadev.bluefoodgroovy.domain.restaurante.ItemCardapio
import br.com.ogawadev.bluefoodgroovy.domain.restaurante.Restaurante


class Carrinho implements Serializable{

    List<ItemPedido> itens = new ArrayList<>()
    Restaurante restaurante

    void adicionarItem(
            ItemCardapio itemCardapio,
            Integer quantidade,
            String observacoes) throws RestauranteDiferenteException {

        if(itens.size() == 0) {
            restaurante = itemCardapio.restaurante
        } else if(!itemCardapio.restaurante.id.equals(restaurante.id)) {
            throw new RestauranteDiferenteException()
        }

        if(!exists(itemCardapio)) {
            ItemPedido itemPedido = new ItemPedido()
            itemPedido.itemCardapio = itemCardapio
            itemPedido.quantidade = quantidade
            itemPedido.observacoes = observacoes
            itemPedido.preco = itemCardapio.preco
            itens.add(itemPedido)
        }
    }

    void adicionarItem(ItemPedido itemPedido) {
        try{
            adicionarItem(itemPedido.itemCardapio, itemPedido.quantidade, itemPedido.observacoes)
        } catch(RestauranteDiferenteException e) {

        }
    }

    void removerItem(ItemCardapio itemCardapio) {
       for (Iterator<ItemPedido> iterator = itens.iterator(); iterator.hasNext();) {
           ItemPedido itemPedido = iterator.next()
           if(itemPedido.itemCardapio.id.equals(itemCardapio.id)) {
               iterator.remove()
               break
           }
       }

        if(itens.size() == 0) {
            restaurante = null
        }
    }

    private boolean exists(ItemCardapio itemCardapio) {
        for(ItemPedido itemPedido : itens) {
            if(itemPedido.itemCardapio.id.equals(itemCardapio.id)) {
                return true
            }
        }

        return false
    }

    BigDecimal getPrecoTotal(boolean adicionarTaxaEntrega) {
        BigDecimal soma = BigDecimal.ZERO

        for(ItemPedido item : itens) {
            soma = soma.add(item.precoCalculado)
        }

        if(adicionarTaxaEntrega) {
            soma = soma.add(restaurante.taxaEntrega)
        }

        return soma
    }

    void limpar() {
        itens.clear()
        restaurante = null
    }

    boolean vazio() {
        return itens.size() == 0
    }
}
