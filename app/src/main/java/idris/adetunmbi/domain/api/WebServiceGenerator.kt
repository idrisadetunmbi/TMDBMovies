package idris.adetunmbi.domain.api

import android.content.Context
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import idris.adetunmbi.R
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

private const val BASE_URL = "https://api.themoviedb.org/3/"

class WebServiceGenerator(context: Context) {

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain: Interceptor.Chain ->
            val request = chain.request()
            val urlWithApiKey = request.url()
                .newBuilder()
                .addQueryParameter("api_key", context.getString(R.string.api_key))
                .build()
            val authdRequest = request.newBuilder().url(urlWithApiKey).build()
            chain.proceed(authdRequest)
        }
        .addInterceptor(HttpLoggingInterceptor()
            .apply { level = HttpLoggingInterceptor.Level.BODY })
        .build()

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
        .addConverterFactory(
            Json(JsonConfiguration.Stable.copy(strictMode = false))
                .asConverterFactory(MediaType.get("application/json"))
        )
        .build()

    inline fun <reified T> createService(): T {
        return retrofit.create(T::class.java)
    }
}
