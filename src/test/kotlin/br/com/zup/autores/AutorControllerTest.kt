package br.com.zup.autores

import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import javax.inject.Inject

//Define queo teste vai rodar sobre o contexto do micronaut
@MicronautTest
internal class AutorControllerTest{

    //Acesso ao banco
    @field:Inject
    lateinit var autorRepository: AutorRepository

    //Injeção do httpClient
    @field:Inject
    //atende as requisiçoes na raiz da aplicação
    @field:Client("/")
    lateinit var client: HttpClient

    lateinit var autor: Autor

    @BeforeEach
    internal fun setup() {
        val enderecoResponse = EnderecoResponse("Rua Manoel Novis", "Tanguá", "RJ")
        autor = Autor(
            "Jean Charles Batista Santos",
            "jeancbsan@gmail.com",
            "Estudar sempre ajuda",
            Endereco(EnderecoResponse("Rua Manoel Novis", "Tanguá", "RJ"),"201", "24890-000")
        )

        autorRepository.save(autor)
    }

    @AfterEach
    internal fun tearDown() {
        autorRepository.deleteAll()
    }

    @Test
    internal fun `deve retornar os detalhes de um autor`() {

        val resposta = client.toBlocking().exchange("/autores?email=${autor.email}", DetalhesDoAutorResponse::class.java)

        assertEquals(HttpStatus.OK, resposta.status)
        assertNotNull(resposta.body())
        assertEquals(autor.nome, resposta.body()!!.nome)
        assertEquals(autor.email, resposta.body()!!.email)
        assertEquals(autor.descricao, resposta.body()!!.descricao)
    }
}