package ssl.hanshake.retrofit

import ssl.hanshake.retrofit.network.NetworkListener
import ssl.hanshake.retrofit.network.NetworkTask
import ssl.hanshake.retrofit.network.RetrofitService


class MainActivity : SSLDemoActivity() {


    override fun onStartSSLDemo(isSSLPin: Boolean) {

        NetworkTask.taskResponse(String::class.java)
            .from(this)
            .call(object : NetworkListener<String> {
                override fun preCall() {
                    preView(isSSLPin)
                }

                @Throws(Exception::class)
                override fun onNetworkCall(): String {

                    val response = RetrofitService
                        .invokeFrom(context, isSSLPin)
                        .githubEvents
                        .execute()
                    return getResponseOutput(response)
                }

                override fun postCall(result: String) {
                    postView(isSSLPin, result)
                }

                override fun error(e: Exception) {
                    errorView(isSSLPin, e.message.toString())
                }
            })
    }

}
