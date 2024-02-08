package com.example.takevideo

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.MediaController
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import com.example.takevideo.databinding.ActivityMainBinding
import java.io.File

class MainActivity : AppCompatActivity() {
    private lateinit var file: File // variable que guarda el video almacenado en el provider
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.button.setOnClickListener() //evento click al presionar el boton
        {
            val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE).also{ //variable intent que utiliza la camara para grabar
                it.resolveActivity(packageManager).also{component-> //verifica si hay una actividad para capturar el video
                    createVideoFile() //crea el archivo del video
                    val VideoUri: Uri = FileProvider.getUriForFile(this,"com.example.takevideo.fileprovider", file)//variable que obtiene el uri del archivo del video
                    it.putExtra(MediaStore.EXTRA_OUTPUT, VideoUri) //agrega el uri del archivo del video al intent
                }
            }
           startForResult.launch(intent)//llama al launch pasando el intent modificado
        }
    }

    private fun createVideoFile() {
        val dir = getExternalFilesDir(Environment.DIRECTORY_MOVIES) //variable que almacena la ruta donde se guarda el video grabado
        file =  File.createTempFile("VID_${System.currentTimeMillis()}_",".mp4", dir) //crea el archivo temporal con extension mp4

    }

     val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
    {result: ActivityResult ->
        if(result.resultCode == Activity.RESULT_OK) //si la actividad es correcto
        {
            val mediaController = MediaController(this) //crea una instancia de la classe mediacontroller pasandole el contexto
            binding.videoView.setMediaController(mediaController)//asigna el mediacontroller al videoview del xml
            mediaController.setAnchorView(binding.videoView) //asigna la vista al videoview

            val  videoView = binding.videoView //variable que almacena el videoview del xml
            videoView.setVideoURI(Uri.parse(file.toString())) //asigna la uri del video grabado guardado en el archivo
            videoView.start() //empieza el video
        }
    }
}

