package cure.medicine.find.cervical.Models

import com.google.gson.annotations.SerializedName

class PredictionResponse {
    val records: Array<Records>? = null
    val status: Status? = null
}

class Status(val code: Int, val text: String)