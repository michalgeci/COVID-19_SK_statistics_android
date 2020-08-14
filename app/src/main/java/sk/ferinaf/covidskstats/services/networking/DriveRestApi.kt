package sk.ferinaf.covidskstats.services.networking

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

object DriveRestApi {

    interface DriveRestService {
        @GET("{fileId}/export")
        fun checkVersion(
            @Path("fileId") fileId: String,
            @Query("key") key: String,
            @Query("mimeType") mimeType: String
        ): Call<Float>
    }

    private val client: DriveRestService
        get() {
            val gson = GsonBuilder()
                .setLenient()
                .create()
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

            val retrofit = Retrofit.Builder()
                .baseUrl("https://www.googleapis.com/drive/v3/files/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()

            return retrofit.create(DriveRestApi.DriveRestService::class.java)
        }

    fun getVersion(onSuccess: (Float) -> Unit) {
        val call = client.checkVersion(
            "1ZSUiy_pJkmVjgwkzQgyVzkId9_o30-Tz_UgB4HbZlKk",
            "AIzaSyCKtkNiKsw__Pi0WYyf-Mqtyx6Z7ueuUEg",
            "text/plain"
        )

        call.enqueue( object : retrofit2.Callback<Float> {
            override fun onFailure(call: Call<Float>, t: Throwable) {

            }

            override fun onResponse(call: Call<Float>, response: Response<Float>) {
                response.body()?.let(onSuccess)
            }

        })
    }
}