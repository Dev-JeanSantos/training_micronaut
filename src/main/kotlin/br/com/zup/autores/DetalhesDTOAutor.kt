package br.com.zup.autores

class DetalhesDTOAutor(autor: Autor) {
    val nome = autor.nome
    val email = autor.email
    val descriacao = autor.descricao
}