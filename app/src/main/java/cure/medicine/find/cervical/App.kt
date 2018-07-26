package cure.medicine.find.cervical

import android.app.Application
import cure.medicine.find.cervical.Network.ApiService
import cure.medicine.find.cervical.Network.URLS
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class App: Application() {
    fun getApiService(): ApiService {

        val logInterceptor = HttpLoggingInterceptor()
        logInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder().addInterceptor(logInterceptor).build()

        return Retrofit.Builder()
                .baseUrl(URLS.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
    }
}