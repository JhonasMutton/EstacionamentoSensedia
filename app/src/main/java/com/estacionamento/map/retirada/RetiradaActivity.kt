package com.estacionamento.map.retirada

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
import com.estacionamento.databinding.LayoutActivityRetiradaBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class RetiradaActivity : AppCompatActivity() {

    private lateinit var binding: LayoutActivityRetiradaBinding
    private lateinit var viewModel: RetiradaViewModel
    private lateinit var viewModelFactory: RetiradaViewModelFactory
    private val row = 40;
    private val col = 12;
    private var numberLocations = 0;

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,
            R.layout.layout_activity_retirada
        )
        viewModelFactory = RetiradaViewModelFactory(applicationContext)
        viewModel =
            ViewModelProvider(this, viewModelFactory).get(RetiradaViewModel::class.java)
        loadDataFromIntent(intent)
        loadFeatures()
    }

    private fun loadDataFromIntent(intent: Intent){
        viewModel.setCarId(intent.getIntExtra("carId", -1))
        viewModel.setCarLocation(intent.getIntExtra("localizacao", -1))
        viewModel.setCarLocationId(intent.getIntExtra("veiculoLocalizacao", -1))
        Log.d("debug", "loadDataFromIntent: carId - ${viewModel.getCarId()} location - ${viewModel.getCarLocation()} veiculoLocalizacao - ${viewModel.getCarLocationId()}")
    }

    private fun loadFeatures(){
        viewModel.load()
        viewModel.liveData.observe(this, Observer { viewState ->
            when (viewState) {
                RetiradaViewState.LoadingMap -> showLoadingView()
                is RetiradaViewState.FinishedMap -> onMapLoaded()
                is RetiradaViewState.Error -> showErrorView(viewState.exception)
                is RetiradaViewState.CarSent -> onDone()
                is RetiradaViewState.SendingCar -> showSendingCarView()
            }
        })
    }

    private fun showSendingCarView(){
        binding.loading.mapLoading.visibility = View.VISIBLE
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
        showCarLocation()
        binding.mapBtnSend.setOnClickListener { confirmCarLocation() }
    }

    private fun showCarLocation(){
        Log.d("debug", "showCarLocation: carLocation - ${viewModel.getCarLocation()}")
        if(viewModel.getCarLocation() != -1) {
            val frameLayout = binding.map.mapGrid[viewModel.getCarLocation()] as FrameLayout
            val viewStub = frameLayout[1]
            viewStub.visibility = View.VISIBLE
        }
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
            .show()    }

    private fun onDone(){
        Toast.makeText(this, "Carro retirado com sucesso!", Toast.LENGTH_LONG).show()
        finish()
    }

    private fun confirmCarLocation(){
        MaterialAlertDialogBuilder(this)
            .setTitle("Confirmar localização")
            .setMessage("Você tem certeza que decorou a localização e deseja retirar o carro?")
            .setNegativeButton("Cancelar") { dialog, which ->
                dialog.dismiss()
            }
            .setPositiveButton("Confirmar") { dialog, which ->
                viewModel.sendCar()
            }
            .show()
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

    private fun createViewStub(): ViewStub {
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
