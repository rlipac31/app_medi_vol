// Archivo: com/example/medivol_1/api/TokenManager.kt

import android.content.Context
import android.util.Log
import com.auth0.android.jwt.JWT

object TokenManager {
    private const val PREFS_NAME = "MyAppPrefs"
    private const val ROLE_KEY = "user_role"

    fun saveToken(context: Context, token: String) {
        val sharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val userRole = getRoleFromToken(token)
        with(sharedPref.edit()) {
            putString("token", token)
            putString(ROLE_KEY, userRole)
            apply()
            Log.d("TokenSave", "objeto donde se guarda el token: $token")
            Log.d("TokenSave", "Rol guardado: $userRole")
        }
    }

    /**
     * Extrae el rol del usuario de un token JWT de forma segura.
     */
    fun getRoleFromToken(token: String): String? {
        return try {
            val jwt = JWT(token)

            // ¡Esto es lo más importante! Imprime todos los claims del token.
            // Revisa esta línea en el Logcat después de iniciar sesión.
            Log.d("TokenManager", "Claims del token decodificado: ${jwt.claims}")

            // Ahora, intenta obtener el rol basado en la clave que encontraste en el log.
            // Por ejemplo, si el log te muestra "user_role=Admin", cambia "rol" a "user_role".
            val roleClaim = jwt.getClaim("rol") // <--- Reemplaza "rol" con la clave correcta
            val role = roleClaim.asString()

            if (role != null) {
                Log.d("TokenManager", "Rol extraído: $role")
                return role
            } else {
                Log.e("TokenManager", "No se pudo extraer el rol del token. La clave podría ser incorrecta.")
                return null
            }

        } catch (e: Exception) {
            Log.e("TokenManager", "Error al decodificar el token JWT: ${e.message}")
            return null
        }
    }
    /*
    * Boraar esta linena
    *
    * SimpleGrantedAuthority // para rol
    * */

    fun getUserRole(context: Context): String? {
        val sharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPref.getString(ROLE_KEY, null)
    }

    fun clearToken(context: Context) {
        val sharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            remove("token")
            remove(ROLE_KEY)
            apply()
        }
    }
}