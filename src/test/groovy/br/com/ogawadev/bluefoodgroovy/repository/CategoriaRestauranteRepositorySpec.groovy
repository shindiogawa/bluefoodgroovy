package br.com.ogawadev.bluefoodgroovy.repository

import br.com.ogawadev.bluefoodgroovy.domain.restaurante.CategoriaRestaurante
import br.com.ogawadev.bluefoodgroovy.domain.restaurante.CategoriaRestauranteRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

@DataJpaTest
@ActiveProfiles("test")
class CategoriaRestauranteRepositorySpec extends Specification{

    @Autowired
    private CategoriaRestauranteRepository categoriaRestauranteRepository

    def setup() {
        assert categoriaRestauranteRepository != null
    }

    def "test insert and delete RategoriaRestauranteRepository"() {

        given: "creating new restaurant"
        CategoriaRestaurante cr = new CategoriaRestaurante()

        and: "insert nome and imagem to categoriaRestaurant object"
        cr.nome = "Chinesa"
        cr.imagem = "chinesa.png"

        when: "save restaurant"
        categoriaRestauranteRepository.saveAndFlush(cr)

        and: "Find created categoriaRestaurante"
        CategoriaRestaurante cr2 = categoriaRestauranteRepository.findById(cr.id).orElseThrow({ -> new NoSuchElementException()})

        and: "Find all categoriaRestaurante size() + new categoriaRestaurante"
        List<CategoriaRestaurante> crList = categoriaRestauranteRepository.findAll()

        and: "Delete the created categoriaRestaurante"
        categoriaRestauranteRepository.delete(cr)

        and: "Find All categoriaRestaurante size() - new categoriaRestaurante "
        List<CategoriaRestaurante> crList2  =categoriaRestauranteRepository.findAll()

        then: "test categoriaRestauranteObjects"
        with(cr) {
            id != null
            nome == cr2.nome
        }
        with(crList) {
            size() == 7
        }
        with(crList2) {
            size() == 6
        }
    }

//    @Test
//    void testInsertAndDelete() throws Exception{
//
//        assertThat(categoriaRestauranteRepository).isNotNull()
//
//        CategoriaRestaurante cr = new CategoriaRestaurante()
//        cr.nome = "Chinesa"
//        cr.imagem = "chinesa.png"
//        categoriaRestauranteRepository.saveAndFlush(cr)
//
//        assertThat(cr.id).isNotNull()
//
//        CategoriaRestaurante cr2 = categoriaRestauranteRepository.findById(cr.id).orElseThrow({ -> new NoSuchElementException()})
//        assertThat(cr.nome).isEqualTo(cr2.nome)
//
//        assertThat(categoriaRestauranteRepository.findAll()).hasSize(7)
//
//        categoriaRestauranteRepository.delete(cr)
//
//        assertThat(categoriaRestauranteRepository.findAll()).hasSize(5)
//    }
}
