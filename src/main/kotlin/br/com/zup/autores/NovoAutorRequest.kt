package br.com.zup.autores

import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size


@Introspected //Validação dos Campos
data class NovoAutorRequest(
    @field:NotBlank val nome: String,
    @field:NotBlank @field:Email val email: String,
    @field:NotBlank @field:Size(max = 400) val descricao: String,
    //Inserir novos campos para receber a nova Api CEP
    @field:NotBlank val cep: String,
    @field:NotBlank val numero:String
) {

    fun paraAutor(enderecoResponse: EnderecoResponse): Autor {
        val endereco = Endereco(enderecoResponse , numero, cep)
        return Autor(nome, email, descricao, endereco)
    }
}
