package br.com.zup.autores

import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post


@Controller("/autores")
class CadastrarAutorController {
    @Post
    fun cadastra(@Body request: NovoAutorRequest){
        println(request)
    }
}