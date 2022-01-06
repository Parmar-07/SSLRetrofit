package ssl.hanshake.retrofit

import com.google.firebase.perf.FirebasePerformance
import com.google.firebase.perf.metrics.Trace

class PerformanceTrace private constructor() {

    companion object {

        private val instance = PerformanceTrace()
        fun getInstance(): PerformanceTrace {
            return instance
        }

    }

    private val firebasePerformance = FirebasePerformance.getInstance()

    private val traces = ArrayList<TraceData>()


    class TraceData(internal val key: String, private val firebasePerformance: FirebasePerformance) {
        private var trace: Trace? = null
        fun setTraceData(data: String) {
            trace = firebasePerformance.newTrace(data)
        }

        fun startTrace() {
            if (trace != null) {
                trace!!.start()
            }
        }

        fun stopTrace() {
            if (trace != null) {
                trace!!.stop()
            }
        }

        override fun equals(other: Any?): Boolean {
            return key == (other as TraceData).key
        }

        override fun hashCode(): Int {
            var result : Int = key.hashCode()
            result = 31 * result + firebasePerformance.hashCode()
            result = 31 * result + trace.hashCode()
            return result
        }

    }


    private fun addTrace(traceData: TraceData) {
        if (!traces.contains(traceData)) {
            traces.add(traceData)
        }
    }


    fun traceData(key: String): TraceData {
        var traceData: TraceData? = null
        traces.forEach {
            if (it.key == key) {
                traceData = it
                return@forEach
            }
        }

        if (traceData == null) {
            traceData = TraceData(key, firebasePerformance)
            addTrace(traceData!!)
        }


        return traceData!!

    }


}