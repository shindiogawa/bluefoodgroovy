package br.com.ogawadev.bluefoodgroovy.infrastructure.web.controller

import br.com.ogawadev.bluefoodgroovy.domain.restaurante.CategoriaRestaurante
import br.com.ogawadev.bluefoodgroovy.domain.restaurante.CategoriaRestauranteRepository
import org.springframework.data.domain.Sort
import org.springframework.ui.Model

class ControllerHelper {

    static void setEditMode(Model model, boolean isEdit) {

        model.addAttribute("editMode", isEdit)

    }

    static void addCategoriasToRequest(CategoriaRestauranteRepository repository, Model model) {

        List<CategoriaRestaurante> categorias = repository.findAll(Sort.by("nome"))
        model.addAttribute("categorias", categorias)

    }
}
