package br.com.fiap.agneloestoque.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import br.com.fiap.agneloestoque.model.Produto

@Dao
interface ProdutoDao {

    @Insert
    fun salvar(produto: Produto): Long

    @Update
    fun atualizar(produto: Produto): Int

    @Delete
    fun excluir(produto: Produto): Int

    @Query("SELECT * FROM tbl_produto WHERE id = :id")
    fun buscarProdutoPeloId(id: Int): Produto

    @Query("SELECT * FROM tbl_produto ORDER BY nome ASC")
    fun listarProduto(): List<Produto>



}