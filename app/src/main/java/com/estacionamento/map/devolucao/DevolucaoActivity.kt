package com.estacionamento.map.devolucao

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.estacionamento.R
import com.estacionamento.databinding.LayoutActivityDevolucaoBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class DevolucaoActivity : AppCompatActivity() {

    private lateinit var binding: LayoutActivityDevolucaoBinding
    private lateinit var viewModel: DevolucaoViewModel
    private lateinit var viewModelFactory: DevolucaoViewModelFactory
    private val row = 40;
    private val col = 12;
    private var numberLocations = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,
            R.layout.layout_activity_devolucao
        )
        viewModelFactory = DevolucaoViewModelFactory(applicationContext)
        viewModel =
            ViewModelProvider(this, viewModelFactory).get(DevolucaoViewModel::class.java)
        loadDataFromIntent(intent)
        loadFeatures()
    }

    private fun loadDataFromIntent(intent: Intent){
        viewModel.setCarId(intent.getIntExtra("carId", -1))
        Log.d("debug", "loadDataFromIntent: carId - ${viewModel.getCarId()}")
    }

    private fun loadFeatures(){
        viewModel.load()
        viewModel.liveData.observe(this, Observer { viewState ->
            when (viewState) {
                DevolucaoViewState.LoadingMap -> showLoadingView()
                is DevolucaoViewState.FinishedMap -> onMapLoaded()
                is DevolucaoViewState.Error -> showErrorView(viewState.exception)
                is DevolucaoViewState.SendingCar -> showSendingCarView()
                is DevolucaoViewState.CarSent -> carSent()
            }
        })
    }

    private fun showSendingCarView(){
        binding.loading.mapLoading.visibility = View.VISIBLE
    }

    private fun carSent(){
        Toast.makeText(this, "Carro devolvido com sucesso!", Toast.LENGTH_LONG).show()
        finish()
    }

    private fun showLoadingView(){
        Log.d("debug","showLoadingView")
        binding.loading.mapLoading.visibility = View.VISIBLE
        GlobalScope.launch {
            drawMap()
        }
    }

    private fun onMapLoaded(){
        Log.d("debug","onMapLoaded")
        binding.loading.mapLoading.visibility = View.INVISIBLE
        binding.banner.setRightButtonAction { binding.banner.dismiss() }
        binding.mapBtnSend.setOnClickListener { onSendCar() }
    }

    private fun showErrorView(exception: Exception){
        Log.d("debug","showErrorView: exception - ${exception}")
        MaterialAlertDialogBuilder(this)
            .setTitle("Ocorreu um erro")
            .setMessage("Tente novamente mais tarde")
            .setNegativeButton("Entendido") { dialog, which ->
                dialog.dismiss()
                finish()
            }
            .show()
    }

    private fun onSendCar(){
        confirmCarLocation()
    }

    private fun confirmCarLocation(){
        when(viewModel.getSelectedIndex())
        {
            -1 -> localizacaoInvalida()
            208, 168 -> localizacaoArvore()
            else -> localizacaoValida()
        }
    }

    private fun localizacaoInvalida(){
        MaterialAlertDialogBuilder(this)
            .setTitle("Localização não selecionada")
            .setMessage("Selecione a localização do carro")
            .setNegativeButton("Entendido") { dialog, which ->
                dialog.dismiss()
            }
            .show()
    }

    private fun localizacaoArvore(){
        MaterialAlertDialogBuilder(this)
            .setTitle("Espere um pouco")
            .setMessage("Parece que você estacionou muito próximo a árvore de jambeiro. Evite ao máximo estacionar em baixo dela para não manchar o carro, caso tenha por favor estacione em outro lugar. ")
            .setNegativeButton("Vou estacionar novamente") { dialog, which ->
                dialog.dismiss()
                finish()
            }
            .setPositiveButton("O carro está seguro") { dialog, which ->
                localizacaoValida()
            }
            .show()
    }

    private fun localizacaoValida(){
        MaterialAlertDialogBuilder(this)
            .setTitle("Confirmar localização")
            .setMessage("Você tem certeza que esta é a localização correta?")
            .setNegativeButton("Cancelar") { dialog, which ->
                dialog.dismiss()
            }
            .setPositiveButton("Confirmar") { dialog, which ->
                sendCar()
            }
            .show()
    }

    private fun sendCar(){
        viewModel.sendCar()
    }

    private fun selectLocation(id: Int){
        if(viewModel.getSelectedIndex() != -1) {
           changeOldLocation()
        }
        changeCurrentLocation(id)
        viewModel.setSelectedIndex(id)
    }

    private fun changeOldLocation(){
        Log.d("debug","changeOldLocation: location - ${viewModel.getSelectedIndex()}")
        val oldFrameLayout = binding.map.mapGrid[viewModel.getSelectedIndex()] as FrameLayout
        val oldViewStub = oldFrameLayout[1]
        oldViewStub.visibility = View.INVISIBLE
    }

    private fun changeCurrentLocation(id: Int){
        Log.d("debug","changeCurrentLocation: location - $id")
        val frameLayout = binding.map.mapGrid[id] as FrameLayout
        val viewStub = frameLayout[1]
        viewStub.visibility = View.VISIBLE
    }

    private suspend fun drawMap(){
        Log.d("debug", "drawMap")
        binding.map.mapGrid.setWillNotDraw(false)
        binding.map.mapGrid.columnCount = row
        binding.map.mapGrid.rowCount = col
        val bitmap = splitBitmap()
        for(x in 0 until col){
            for(y in 0 until row){
                val view: FrameLayout = createView(bitmap[y][x])
                runOnUiThread{
                    binding.map.mapGrid.addView(view)
                }
            }
        }
        runOnUiThread{
            viewModel.loadMap()
        }
    }

    private suspend fun createView(bitmap: Bitmap?) : FrameLayout {
        Log.d("debug", "createView")
        val frameLayout = FrameLayout(applicationContext)
        val frameParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        frameLayout.layoutParams = frameParams
        frameLayout.id = numberLocations
        numberLocations++;
        frameLayout.addView(createImageView(bitmap))
        frameLayout.addView(createViewStub())
        frameLayout.setOnClickListener {
            selectLocation(frameLayout.id)
        }
        Log.d("debug", "createView: Done - id ${frameLayout.id}")
        return frameLayout
    }

    private suspend fun createImageView(bitmap: Bitmap?): ImageView{
        val imageView = ImageView(applicationContext)
        runOnUiThread{
            Glide.with(applicationContext)
                .load(bitmap)
                .override(200)
                .into(imageView)
        }
        return imageView
    }

    private suspend fun createViewStub(): ViewStub{
        val viewStub = ViewStub(applicationContext)
        viewStub.layoutResource = R.layout.layout_map_indicator
        return viewStub
    }

    private suspend fun splitBitmap(): Array<Array<Bitmap?>> {
        Log.d("debug", "splitBitmap")
        val bitmap = BitmapFactory.decodeResource(resources, R.mipmap.map_sensedia)
        val bitmaps =
            Array(row) { arrayOfNulls<Bitmap>(col) }
        val width: Int
        val height: Int
        width = bitmap.width / row
        height = bitmap.height / col
        for (x in 0 until row) {
            for (y in 0 until col) {
                bitmaps[x][y] =
                    Bitmap.createBitmap(bitmap, x * width, y * height, width, height)
            }
        }
        Log.d("debug", "splitBitmap: Done")
        return bitmaps
    }

}
