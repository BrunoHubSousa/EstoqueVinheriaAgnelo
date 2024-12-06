package br.com.fiap.agneloestoque.database.repository

import android.content.Context
import br.com.fiap.agneloestoque.database.dao.ProdutoDb
import br.com.fiap.agneloestoque.model.Produto

class ProdutoRepository(context: Context) {

    private val db = ProdutoDb.getDatabase(context).produtoDao()

    fun salvar(produto: Produto): Long {
        return db.salvar(produto)
    }

    fun atualizar(produto: Produto): Int {
        return db.atualizar(produto)
    }

    fun excluir(produto: Produto): Int {
        return db.excluir(produto)
    }

    fun listarProduto(): List<Produto> {
        return db.listarProduto()
    }

    fun buscarProdutopPeloId(id: Int): Produto {
        return db.buscarProdutoPeloId(id)
    }

}