package cure.medicine.find.cervical.ViewModels

import android.util.Base64
import cure.medicine.find.cervical.DataHandlers.PredictionDataHandler
import cure.medicine.find.cervical.Models.PredictionRecords
import cure.medicine.find.cervical.Models.PredictionRequest
import cure.medicine.find.cervical.Utils.KEYS
import io.reactivex.Single
import java.io.File
import java.io.IOException

class PredictionViewModel {
    private val predictionDataHandler = PredictionDataHandler()

    fun predict(imagePath: String): Single<String?> {
        return Single.create {single ->
            predictionDataHandler.fetchPredictions(createRequestBody(imagePath), {
                response ->
                val statusCode = response?.status?.code
                val records = response?.records
                if ( statusCode == 200) {
                    if (records?.size!! > 0) {
                        records[0].bestLabel?.name?.let {
                            single.onSuccess(it)
                        }
                    }
                } else {
                    single.onError(Throwable("Failed", IOException()))
                }
            })
        }
    }

    /**
     * Creates an image encoded
     *
     * @param imageLocation [String]
     * @return [String]
     */
    private fun createImageEncoding(imageLocation: String): String {

        val file = File(imageLocation).readBytes()
        return Base64.encodeToString(file, Base64.DEFAULT)
    }

    /**
     * Creates task id in the request body
     *
     * @param imageLocation [String]
     *
     * @return [PredictionRequest]
     */
    private fun createRequestBody(imageLocation: String): PredictionRequest {
        val records = arrayListOf<PredictionRecords>()
        records.add(PredictionRecords(createImageEncoding(imageLocation)))
        return PredictionRequest(KEYS.taskId, records)
    }
}