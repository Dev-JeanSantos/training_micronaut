package br.com.zup.autores

import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import javax.inject.Inject

//Define queo teste vai rodar sobre o contexto do micronaut
@MicronautTest
internal class AutorControllerTest{

    //Acesso ao enderecoCliente
    @field:Inject
    lateinit var enderecoClient: EnderecoCliente

    //Acesso ao banco
    @field:Inject
    lateinit var autorRepository: AutorRepository

    //Injeção do httpClient
    @field:Inject
    //atende as requisiçoes na raiz da aplicação
    @field:Client("/")
    lateinit var client: HttpClient

    lateinit var autor: Autor
    lateinit var enderecoResponse: EnderecoResponse

    @BeforeEach
    internal fun setup() {

        enderecoResponse = EnderecoResponse("Rua Manoel Novis", "Tanguá", "RJ")
        val endereco = Endereco(enderecoResponse,"201", "24890-000")
        autor = Autor(
            "Jean Charles Batista Santos",
            "jeancbsan@gmail.com",
            "Estudar sempre ajuda",
            endereco
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


    @Test
    internal fun `deve cadastrar um novo autor`() {

        //cenário
        val novoAutorRequest = NovoAutorRequest(
            "Marcio Cardoso",
            "marciocardoso@gmail.com",
            "Deus acima de Tudo",
            "24890-000",
            "300"
        )

        Mockito.`when`(enderecoClient.consulta(novoAutorRequest.cep)).thenReturn(HttpResponse.ok(enderecoResponse))
        val request = HttpRequest.POST("/autores", novoAutorRequest)

        //acao

        val response = client.toBlocking().exchange(request, Any::class.java)

        //Corretude
        assertEquals(HttpStatus.CREATED, response.status)
        assertTrue(response.headers.contains("Location"))
        assertTrue(response.header("Location")!!.matches("/autores/\\d".toRegex()))
    }

    @MockBean(EnderecoCliente::class)
    fun enderecoMock(): EnderecoCliente {
        return Mockito.mock(EnderecoCliente::class.java)
    }

}