import android.content.Context
import com.example.medivol_1.Constants
import com.example.medivol_1.api.AuthInterceptor
import com.example.medivol_1.service.ConsultaService
import com.example.medivol_1.service.MedicoService
import com.example.medivol_1.service.PacienteService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    fun getRetrofit(context: Context): Retrofit {
        val client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(context))
            .build()

        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    fun getMedicoService(context: Context): MedicoService {
        return getRetrofit(context).create(MedicoService::class.java)
    }
    fun getPacienteService(context: Context): PacienteService {
        return getRetrofit(context).create(PacienteService::class.java)
    }
    fun getConsultaService(context: Context): ConsultaService {
        return getRetrofit(context).create(ConsultaService::class.java)
    }

}
