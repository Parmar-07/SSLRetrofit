package ssl.hanshake.retrofit

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import okhttp3.ResponseBody
import retrofit2.Response

abstract class SSLDemoActivity : AppCompatActivity() {


    protected lateinit var context: Context

    abstract fun onStartSSLDemo(isSSLPin: Boolean)

    private lateinit var pinBtn: Button
    private lateinit var unPinBtn: Button
    private lateinit var output: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        context = this.applicationContext

        pinBtn = findViewById(R.id.pinBtn)
        unPinBtn = findViewById(R.id.unPinBtn)
        output = findViewById(R.id.result)

        pinBtn.setOnClickListener { onStartSSLDemo(true) }
        unPinBtn.setOnClickListener { onStartSSLDemo(false) }
    }

    protected fun preView(isPin: Boolean) {

        if (isPin) {
            pinBtn.enableProgress()
            unPinBtn.disableProgress()
            pinBtn.progressText("Pining...")
        } else {
            unPinBtn.enableProgress()
            unPinBtn.progressText("UnPining...")
            pinBtn.disableProgress()
        }
        output.default()
    }

    protected fun postView(isPin: Boolean, result: String) {

        if (isPin) {

            pinBtn.stableProgress()
            pinBtn.progressText("Pin")
            unPinBtn.stableProgress()

        } else {
            unPinBtn.stableProgress()
            unPinBtn.progressText("UnPin")
            pinBtn.stableProgress()
        }
        output.asResult(result)
    }

    protected fun errorView(isPin: Boolean, error: String) {
        if (isPin) {
            pinBtn.stableProgress()
            pinBtn.progressText("Pin")
            unPinBtn.stableProgress()
        } else {
            unPinBtn.stableProgress()
            unPinBtn.progressText("UnPin")
            pinBtn.stableProgress()
        }

        output.asError(error)
    }

    protected fun getResponseOutput(response: Response<ResponseBody>): String {

        var output = "Status Code : " + response.code()
        output += if (response.isSuccessful) {
            "\n${Gson().toJson(response.body().toString())}"
        } else {
            "\n${Gson().toJson(response.errorBody().toString())}"
        }

        return output


    }


}