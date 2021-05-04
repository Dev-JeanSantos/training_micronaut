package br.com.zup.autores

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.*
import io.micronaut.validation.Validated
import java.util.*
import javax.inject.Inject
import javax.transaction.Transactional
import javax.validation.Valid


@Validated //Validação do DTO
@Controller("/autores")
class AutorController(@Inject val autorRepository: AutorRepository) {
    @Post
    @Transactional
    fun cadastra(@Body @Valid request: NovoAutorRequest){
        println("DTO => $request")
        val autor = request.paraAutor()
        autorRepository.save(autor)
        println("Classe => ${autor.nome}")
    }

    @Get
    @Transactional
    fun lista(@QueryValue(defaultValue = "") email: String): HttpResponse<Any> {

        if(email.isBlank()){
            val autores = autorRepository.findAll()
            val resposta = autores.map { autor -> DetalhesDoAutorResponse(
                autor.nome,
                autor.email,
                autor.descricao
            ) }
            return HttpResponse.ok(resposta)
        }
        //Outra Opção com JPQL
        val possivelAutor = autorRepository.buscaPorEmail(email)

        //Outra Opção com Queries do Hibernate
        //val possivelAutor = autorRepository.findByEmail(email)

        if(possivelAutor.isEmpty){
            return HttpResponse.notFound()
        }

        val autor = possivelAutor.get()
        return HttpResponse.ok(DetalhesDoAutorResponse(
            autor.nome,
            autor.email,
            autor.descricao
        ))
    }

    @Put("/{id}")
    @Transactional
    fun atualiza(@PathVariable id: Long, nome: String, email: String, descricao: String) : HttpResponse<Any>{

        val possivelAutor : Optional<Autor> = autorRepository.findById(id)

        if (possivelAutor.isEmpty) {
            return HttpResponse.notFound()
        }
        val autor = possivelAutor.get()

        autor.nome = nome
        autor.email = email
        autor.descricao = descricao
        //autorRepository.update(autor)

        return HttpResponse.ok(DetalhesDoAutorResponse(
            autor.nome,
            autor.email,
            autor.descricao
        ))
    }

    @Delete("/{id}")
    @Transactional
    fun Deleta(@PathVariable id: Long) : HttpResponse<Any>{

        val possivelAutor = autorRepository.findById(id)

        if (possivelAutor.isEmpty) {
            return HttpResponse.notFound()
        }

        //Possivel deletar pela entidade ou por id
        val autor = possivelAutor.get()
        autorRepository.delete(autor)

        //Deletando pelo id
        //autorRepository.deleteById(id)
        return HttpResponse.ok()
    }

}