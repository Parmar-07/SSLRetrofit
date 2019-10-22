@file:Suppress("DEPRECATION")

package ssl.hanshake.retrofit.network

import android.content.Context
import android.net.ConnectivityManager
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import ssl.hanshake.retofit.RetrofitHandShake
import ssl.hanshake.retofit.mode.HandShakeMode
import ssl.hanshake.retofit.mode.builder.PinBuilder
import ssl.hanshake.retofit.mode.builder.UnPinBuilder
import ssl.hanshake.retofit.pin_extractor.PinExtract
import ssl.hanshake.retofit.pin_extractor.PinExtractor
import ssl.hanshake.retrofit.BuildConfig
import java.util.concurrent.TimeUnit

interface RetrofitService {


    @get:GET("events")
    val githubEvents: Call<ResponseBody>



    companion object {

        private const val baseURL = "https://api.github.com/"




        fun invokeFrom(context: Context, pin: Boolean = false): RetrofitService {


            val networkInterceptor = Interceptor {

                val connectivityManager =
                    context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val activeNetworkInfo = connectivityManager.activeNetworkInfo
                val isNetworkPortEnable = activeNetworkInfo != null && activeNetworkInfo.isConnected

                if (!isNetworkPortEnable) {
                    throw Exception("No Internet is Connected!")
                }


                return@Interceptor it.proceed(it.request())

            }


            val handShakeMode: HandShakeMode = if (pin) {

                val extractPin: PinExtract? = PinExtractor.newBuilder(context, "sha256")
                    .open("github.crt")
                    .build()

                PinBuilder.newBuilder(baseURL)
                    .pinKey(extractPin)
                    .build()


            } else {
                UnPinBuilder.newBuilder()
                    .enableTLSVersion("SSL")
                    .build()
            }


            val client = RetrofitHandShake
                .mode(handShakeMode)
                .handshake(serviceClient(networkInterceptor))

            return Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build().create(RetrofitService::class.java)


        }



        private fun serviceClient(networkIntercept: Interceptor): OkHttpClient {

            val httpLoggingInterceptor = HttpLoggingInterceptor()
            if (BuildConfig.DEBUG) {
                httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            }



            return OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .followRedirects(false)
                .addInterceptor(networkIntercept)
                .addInterceptor(httpLoggingInterceptor)
                .followSslRedirects(false)
                .retryOnConnectionFailure(true)
                .cache(null)
                .build()
        }


    }


}