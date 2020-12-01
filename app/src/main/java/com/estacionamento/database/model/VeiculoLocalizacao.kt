package com.estacionamento.database.model

import androidx.room.*
//import java.time.LocalDateTime



@Entity (tableName = "parkingSpace")

class VeiculoLocalizacao(@PrimaryKey (autoGenerate = true)
                         var id_veiculo_localizacao: Int? = 1,
                         @ColumnInfo(name = "id_veiculo") var id_veiculo: Int,
                         @ColumnInfo(name = "id_localizacao") var id_localizacao: Int,
                         @ColumnInfo(name = "disponibilidade") var disponibilidade: Int

)

