package br.com.zup.autores

import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Get
import io.micronaut.http.client.annotation.Client


@Client("http://viacep.com.br/ws/")
interface EnderecoCliente {

    @Get("{cep}/json/", consumes = [MediaType.APPLICATION_JSON])
    fun consulta(cep: String) : HttpResponse<EnderecoResponse>

}
