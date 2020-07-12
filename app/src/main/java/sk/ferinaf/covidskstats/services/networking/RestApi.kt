package sk.ferinaf.covidskstats.services.networking

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import sk.ferinaf.covidskstats.services.networking.models.CovidData

object RestApi {

    interface RestService {
        @GET("map_data")
        fun getData(): Call<CovidData>
    }

    private val client: RestService
    get() {
        val gson = GsonBuilder()
            .setLenient()
            .create()
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://mapa.covid.chat/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        return retrofit.create(RestService::class.java)
    }

    fun getData(onSuccess: (CovidData) -> Unit) {
        val call = client.getData()
        call.enqueue(object : retrofit2.Callback<CovidData> {
            override fun onFailure(call: Call<CovidData>, t: Throwable) {

            }

            override fun onResponse(call: Call<CovidData>, response: Response<CovidData>) {
                response.body()?.let(onSuccess)
            }

        })
    }

}

