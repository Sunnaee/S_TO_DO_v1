package com.example.sabrinatodo

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private lateinit var rutInput: EditText
    private lateinit var tareasOutput: TextView
    private lateinit var botonObtenerTareas: Button
    private lateinit var myImageView: ImageView

    private val urlBase = "http://54.210.65.125:8081/tareas/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        myImageView = findViewById(R.id.my_image_view)
        myImageView.setImageResource(R.drawable.iselda)

        rutInput = findViewById(R.id.rut_input)
        tareasOutput = findViewById(R.id.tareas_output)
        botonObtenerTareas = findViewById(R.id.boton_obtener_tareas)

        botonObtenerTareas.setOnClickListener {
            obtenerTareas(rutInput.text.toString())
        }
    }

    private fun obtenerTareas(rutUsuario: String) {
        val url = "$urlBase$rutUsuario"
        val requestQueue: RequestQueue = Volley.newRequestQueue(this)

        val jsonArrayRequest = JsonArrayRequest(Request.Method.GET, url, null,
            Response.Listener<JSONArray> { response ->
                val tareas = StringBuilder()
                for (i in 0 until response.length()) {
                    try {
                        val tarea: JSONObject = response.getJSONObject(i)
                        tareas.append("RUT: ${tarea.optString("RUT usuario","RUT no encontrado")} | ")
                            .append("ID: ${tarea.getString("_id")} | ")
                            .append("ID personal: ${tarea.getString("id")} | ")
                            .append("Nombre: ${tarea.optString("nombre", "Sin nombre")} | ")
                            .append("Descripción: ${tarea.optString("descripcion", "Sin descripción")} | ")
                            .append("Estado: ${tarea.optString("estado", "Sin estado")}\n\n")
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
                tareasOutput.text = tareas.toString()
            },
            Response.ErrorListener { error: VolleyError ->
                tareasOutput.text = "Error: ${error.message}"
            }
        )

        requestQueue.add(jsonArrayRequest)
    }
}