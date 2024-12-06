package br.com.fiap.agneloestoque.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tbl_produto")
data class Produto(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    var nome: String,
    var tipo: String,
    var safra: Int,
    var preco: Double,
    var quantidade: Int
)