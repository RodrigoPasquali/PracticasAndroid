package com.example.tp2formularioregistro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.tp2formularioregistro.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var nombre: String
    private var edad: Int = 0
    private lateinit var genero: Genero


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        enviar()
    }

    private fun enviar() {
        binding.botonEnviar.setOnClickListener {
            if(datosValidos()) {
                val formulario = generarFormulario()
                generarMensaje("Formulario cargado con exito!!")
            }
        }
    }

    private fun datosValidos(): Boolean =
        nombreEsValido()
                && edadEsValida()
                && generoEsValido()
                && terminosAceptados()

    private fun nombreEsValido(): Boolean = if (binding.nombre.text.toString().isEmpty()) {
        Toast.makeText(this, getString(R.string.mensaje_nombre_incompleto), Toast.LENGTH_SHORT)
            .show()
        false
    } else {
        nombre = binding.nombre.text.toString()
        true
    }

    private fun edadEsValida(): Boolean = if (binding.edad.text.toString().isEmpty()) {
        generarMensaje(getString(R.string.menasaje_edad_incompleto))
        false
    } else if (binding.edad.text.toString().toInt() < 0 || binding.edad.text.toString().toInt() > 120) {
        generarMensaje(getString(R.string.mensaje_edad_invalida))
        false
    } else {
        edad = binding.edad.text.toString().toInt()
        true
    }

    private fun generoEsValido(): Boolean = if (binding.genero.checkedRadioButtonId == -1) {
        generarMensaje(getString(R.string.mensaje_genero_incompleto))
        false
    } else {
        genero = if (binding.masculino.isChecked) {
            Genero.MASCULINO
        } else if (binding.femenino.isChecked) {
            Genero.FEMENINO
        } else {
            Genero.OTRO
        }
        true
    }

    private fun terminosAceptados(): Boolean = if (!binding.terminos.isChecked) {
        generarMensaje(getString(R.string.debe_aceptar_terminos_y_condiciones))
        false
    } else {
        true
    }

    private fun generarMensaje(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }

    private fun generarFormulario(): FormularioDeRegistro =
        FormularioDeRegistro(nombre, edad, genero)
}