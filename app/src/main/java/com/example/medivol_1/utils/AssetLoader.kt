package com.example.medivol_1.utils


import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import java.io.IOException

/**
 * Un objeto utilitario para cargar datos JSON desde la carpeta 'assets'.
 */
object AssetLoader {

    private const val TAG = "AssetLoader"

    /**
     * Carga y parsea un archivo JSON de los assets en una lista de objetos del tipo especificado.
     *
     * @param context El contexto de la aplicación o actividad para acceder a los assets.
     * @param fileName El nombre del archivo JSON en la carpeta assets (ej. "estados_peru.json").
     * @param typeToken Un TypeToken que define el tipo de la lista esperada (ej. object : TypeToken<List<Estado>>() {}).
     * @return Una lista de objetos del tipo [T] si el parseo es exitoso, o una lista vacía en caso de error.
     */
    fun <T> loadListFromAsset(context: Context, fileName: String, typeToken: TypeToken<List<T>>): List<T> {
        val jsonString: String
        try {
            // Accede al archivo desde la carpeta assets usando el contexto proporcionado
            jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            Log.e(TAG, "Error al leer el archivo JSON: $fileName", ioException)
            // Puedes personalizar el mensaje de Toast o eliminarlo si prefieres un manejo silencioso
            Toast.makeText(context, "Error al cargar datos de $fileName", Toast.LENGTH_SHORT).show()
            return emptyList()
        }

        val gson = Gson()
        try {
            // Parsea el JSON string a la lista del tipo genérico T
            return gson.fromJson(jsonString, typeToken.type)
        } catch (jsonException: JsonSyntaxException) {
            Log.e(TAG, "Error de sintaxis JSON en el archivo: $fileName", jsonException)
            Toast.makeText(context, "Error de formato en el JSON de $fileName", Toast.LENGTH_SHORT).show()
            return emptyList()
        } catch (e: Exception) { // Captura cualquier otra excepción inesperada durante el parseo
            Log.e(TAG, "Error desconocido al parsear JSON de $fileName", e)
            Toast.makeText(context, "Error inesperado al cargar $fileName", Toast.LENGTH_SHORT).show()
            return emptyList()
        }
    }
}