package com.example.constraintlayout

import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.DecimalFormat
import java.util.Locale

class MainActivity : AppCompatActivity() , TextWatcher, TextToSpeech.OnInitListener {
    private lateinit var tts: TextToSpeech
    var resultado = "R$ 0.00"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val moneyString = getString(R.string.money)
        resultado = "$moneyString 0.00"
        setContentView(R.layout.activity_main)
        val edtConta = findViewById<EditText>(R.id.edtConta)
        edtConta.addTextChangedListener(this)
        val edtPessoas = findViewById<EditText>(R.id.edtPessoas)
        edtPessoas.addTextChangedListener(this)
        val textResult = findViewById<TextView>(R.id.ResultView)
        textResult.text = resultado

        tts = TextToSpeech(this, this)

        val buttonShare = findViewById<FloatingActionButton>(R.id.floatingActionButton)
        buttonShare.setOnClickListener {
            val sendIntent = Intent()
            val valueString = getString(R.string.value)
            sendIntent.action = Intent.ACTION_SEND
            sendIntent.putExtra(Intent.EXTRA_TEXT, "$valueString $resultado.")
            sendIntent.type = "text/plain"
            sendIntent.setPackage("com.whatsapp")
            startActivity(Intent.createChooser(sendIntent, ""))
            startActivity(sendIntent)
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
       Log.d("PDM23","Antes de mudar")
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        val df = DecimalFormat("0.00")
        val textResult = findViewById<TextView>(R.id.ResultView)
        val edtConta = findViewById<EditText>(R.id.edtConta)
        val edtPessoas = findViewById<EditText>(R.id.edtPessoas)
        var numeroDePessoas = 0.00
        var conta = 0.00

        if (edtPessoas.text.toString().isNotEmpty()) numeroDePessoas = edtPessoas.text.toString().toDouble()

        if (edtConta.text.toString().isNotEmpty()) conta = edtConta.text.toString().toDouble()

        if(numeroDePessoas != 0.00 && conta != 0.00) {
            Log.d ("PDM23", "2")
            val result = df.format(conta / numeroDePessoas)
            val moneyString = getString(R.string.money)
            resultado = "$moneyString $result"
            textResult.setText(resultado)
        }
    }

    override fun afterTextChanged(s: Editable?) {
        Log.d ("PDM23", "Depois de mudar")
        Log.d ("PDM23", s.toString())
    }

    fun clickFalar(v: View){
        tts.speak(resultado, TextToSpeech.QUEUE_FLUSH, null, null)
    }
    fun clickCompartilhar(v: View){
        tts.speak(resultado, TextToSpeech.QUEUE_FLUSH, null, null)
    }
    override fun onDestroy() {
        // Release TTS engine resources
        tts.stop()
        tts.shutdown()
        super.onDestroy()
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            // TTS engine is initialized successfully
            tts.language = Locale.getDefault()
            Log.d("PDM23","Sucesso na Inicialização")
        } else {
            // TTS engine failed to initialize
            Log.e("PDM23", "Failed to initialize TTS engine.")
        }
    }
}

