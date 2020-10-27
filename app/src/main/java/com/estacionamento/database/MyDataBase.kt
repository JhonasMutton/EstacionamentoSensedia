package com.estacionamento.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Veiculo::class,Localizacao::class,VeiculoLocalizacao::class], version = 1)
abstract class MyDataBase : RoomDatabase(){

    abstract fun getVeiculoDao(): VeiculoDao
    abstract fun getLocalizacaoDao(): LocalizacaoDao
    abstract fun getVeiculoLocalizacaoDao(): VeiculoLocalizacaoDao

    companion object {

      @Volatile  private var INSTANCE: MyDataBase? = null

        fun getInstance(context: Context): MyDataBase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        MyDataBase::class.java,
                        "sleep_history_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }

    }
}
