package com.estacionamento.database.model

import androidx.room.Entity
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.util.*

@Entity (tableName = "localizacoes")
class Localizacao (@PrimaryKey (autoGenerate = true)
                   var id_localizacao: Int? = 1,

                   @ColumnInfo(name = "position")
                   var position: String?){
}