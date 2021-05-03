package br.com.zup.autores

import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank


@Introspected //Validação dos Campos
data class NovoAutorRequest(
    @field:NotBlank val nome: String,
    @field:NotBlank val email: String,
    @field:NotBlank val descricao: String
)
