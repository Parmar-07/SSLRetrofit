package ssl.hanshake.retrofit

import com.google.firebase.perf.metrics.AddTrace
import ssl.hanshake.retrofit.network.NetworkListener
import ssl.hanshake.retrofit.network.NetworkTask
import ssl.hanshake.retrofit.network.RetrofitService
import com.google.firebase.perf.FirebasePerformance
import com.google.firebase.perf.metrics.Trace


class MainActivity : SSLDemoActivity() {


    val pt = PerformanceTrace.getInstance()

   /* val sslDemo =  PerformanceTrace.getInstance().traceData("onStartSSLDemo")
    val sslDemoResult =  PerformanceTrace.getInstance().traceData("SSLDemoResult")
    val sslDemoError =  PerformanceTrace.getInstance().traceData("sslDemoError")
*/


    @AddTrace(name = "onStartSSLDemoFunctionClick", enabled = true)
    override fun onStartSSLDemo(isSSLPin: Boolean) {

       /* val sslDemo: Trace = FirebasePerformance.getInstance().newTrace("onStartSSLDemo Pinned $isSSLPin")
        sslDemo.start()*/

      /*  val sslDemo =  PerformanceTrace.getInstance().traceData("onStartSSLDemo").apply {
            setTraceData("onStartSSLDemo started")
            startTrace()
        }*/

        pt.traceData("t1").apply {
            setTraceData("onStartSSLDemo started")
            startTrace()
        }
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
                    pt.traceData("t2").apply {
                        setTraceData("onStartSSLDemo Pinned Result $result")
                        startTrace()
                        stopTrace()
                    }


                   /* val sslDemoStatus: Trace = FirebasePerformance.getInstance().newTrace("onStartSSLDemo Pinned Result $result")
                    sslDemoStatus.start()
                    sslDemoStatus.stop()*/
                    pt.traceData("t1").stopTrace()
                    postView(isSSLPin, result)
                }

                override fun error(e: Exception) {
                   /* val sslDemoStatus: Trace = FirebasePerformance.getInstance().newTrace("onStartSSLDemo Pinned Error ${e.message}")
                    sslDemoStatus.start()
                    sslDemoStatus.stop()*/
                    pt.traceData("t3").apply {
                        setTraceData("onStartSSLDemo Pinned Error ${e.message}")
                        startTrace()
                        stopTrace()
                    }
                   /* sslDemoError.setTraceData("onStartSSLDemo Pinned Error ${e.message}")
                    sslDemoError.startTrace()
                    sslDemoError.stopTrace()*/
                    pt.traceData("t1").stopTrace()
                    errorView(isSSLPin, e.message.toString())
                }
            })
    }



}
