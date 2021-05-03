package br.com.zup.autores

import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.Email
import javax.validation.constraints.Max
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size
import kotlin.math.max


@Introspected //Validação dos Campos
data class NovoAutorRequest(
    @field:NotBlank val nome: String,
    @field:NotBlank @field:Email val email: String,
    @field:NotBlank @field:Size(max = 400) val descricao: String
) {

    fun paraAutor(): Autor {
        return Autor(nome, email, descricao)
    }
}
