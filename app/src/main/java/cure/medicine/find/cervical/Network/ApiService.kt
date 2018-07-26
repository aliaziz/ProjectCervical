package cure.medicine.find.cervical.Network

import cure.medicine.find.cervical.Models.PredictionRequest
import cure.medicine.find.cervical.Models.PredictionResponse
import cure.medicine.find.cervical.Utils.KEYS
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @Headers(
            "Authorization: Token ${KEYS.token}",
            "Content-Type: application/json")
    @POST("classify")
    fun getPrediction(@Body predictionRequest: PredictionRequest) : Call<PredictionResponse>
}