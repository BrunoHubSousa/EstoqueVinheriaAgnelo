package br.com.fiap.agneloestoque.database.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import br.com.fiap.agneloestoque.model.Produto

@Database(entities = [Produto::class], version = 1)
abstract class ProdutoDb : RoomDatabase() {

    abstract fun produtoDao(): ProdutoDao

    companion object {

        private lateinit var instance: ProdutoDb

        fun getDatabase(context: Context): ProdutoDb {
            if (!::instance.isInitialized) {
                instance = Room
                    .databaseBuilder(
                        context,
                        ProdutoDb::class.java,
                        "tbl_produto"
                    )
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return instance
        }
    }
}