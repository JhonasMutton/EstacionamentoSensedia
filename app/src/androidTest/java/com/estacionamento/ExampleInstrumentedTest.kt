package com.estacionamento

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.estacionamento.database.LocalizacaoDao
import com.estacionamento.database.VeiculoLocalizacaoDao
import com.estacionamento.database.model.Localizacao
import com.estacionamento.database.model.VeiculoLocalizacao
import org.junit.Assert.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException


/**
 * This is not meant to be a full set of tests. For simplicity, most of your samples do not
 * include tests. However, when building the Room, it is helpful to make sure it works before
 * adding the UI.
 */

@RunWith(AndroidJUnit4::class)
class SleepDatabaseTest {

    private lateinit var localizacaoDao: LocalizacaoDao
//    private lateinit var veiculoDao: VeiculoDao
    private lateinit var veiculoLocalizacaoDao: VeiculoLocalizacaoDao
//    private lateinit var db: MyDataBase

    @Before
    fun createDb() {
//        val context = InstrumentationRegistry.getInstrumentation().targetContext
//        // Using an in-memory database because the information stored here disappears when the
//        // process is killed.
//        db = Room.inMemoryDatabaseBuilder(context, MyDataBase::class.java)
//            // Allowing main thread queries, just for testing.
//            .allowMainThreadQueries()
//            .build()
//        veiculoLocalizacaoDao = db.getVeiculoLocalizacaoDao()
//        veiculoDao = db.getVeiculoDao()
//        localizacaoDao = db.getLocalizacaoDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
//        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetLocalizacao() {
//        val localizacao = Localizacao(10,"180")
//        localizacaoDao.insert(localizacao)
//        val localizacaoReal = localizacaoDao.getLocalizacao(10)
//        assertEquals(localizacaoReal?.position, "180")
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetVeiculo() {
//        val veiculo = Veiculo(1,"ABC1234")
//        veiculoDao.insert(veiculo)
//        val veiculoReal = veiculoDao.getVeiculo( 1)
//        assertEquals(veiculoReal?.chapa, "ABC1234")
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetVeiculoLocalizacao() {
//        val veiculolocalizacao = VeiculoLocalizacao(1, 1,10, 1)
//        veiculoLocalizacaoDao.insert(veiculolocalizacao)
//        val veiculoLocalizacaoReal = veiculoLocalizacaoDao.getVeiculoLocalizacao( 1)
//        assertEquals(veiculoLocalizacaoReal?.id_veiculo_localizacao, "1")
    }
}