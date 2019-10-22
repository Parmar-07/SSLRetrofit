package ssl.hanshake.retrofit

import android.graphics.Color
import android.view.View
import android.widget.TextView

fun TextView.progressText(text: String) {
    this.text = text
}

fun TextView.asResult(result: String) {
    this.text = result
    this.setTextColor(Color.parseColor("#ffffff"))
}

fun TextView.default() {
    this.text = "Result"
}

fun TextView.asError(error: String) {
    this.text = error
    this.setTextColor(Color.parseColor("#ffad1457"))
}

fun View.disableProgress() {
    setBackgroundColor(Color.parseColor("#aaaaaa"))
    isEnabled=false
}

fun View.enableProgress() {
    setBackgroundColor(Color.parseColor("#ffad1457"))
    isEnabled=false

}

fun View.stableProgress() {
    setBackgroundColor(Color.parseColor("#33b5e5"))
    isEnabled=true

}