package cure.medicine.find.cervical.DataHandlers

import cure.medicine.find.cervical.App
import cure.medicine.find.cervical.Models.PredictionRequest
import cure.medicine.find.cervical.Models.PredictionResponse
import cure.medicine.find.cervical.Utils.KEYS
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PredictionDataHandler {

    /**
     * Fetch predictions from server
     */
    fun fetchPredictions(predictionRequest: PredictionRequest, fnResponse: (PredictionResponse?) -> Unit) {
        App().getApiService().getPrediction(predictionRequest).enqueue(callback { error, response ->
            if (error == null && response?.isSuccessful == true)
                response.let {fnResponse(it.body())}
        })
    }

    /**
     * Callback generic
     */
    private fun <T> callback(fn: (Throwable?, Response<T>?) -> Unit): Callback<T> {
        return object : Callback<T> {
            override fun onResponse(call: Call<T>?, response: Response<T>?) = fn(null, response)
            override fun onFailure(call: Call<T>?, t: Throwable?) = fn(t, null)
        }
    }
}