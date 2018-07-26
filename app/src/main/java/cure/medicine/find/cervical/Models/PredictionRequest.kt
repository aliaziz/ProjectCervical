package cure.medicine.find.cervical.Models

data class PredictionRequest(val task_id: String, val records: ArrayList<PredictionRecords>)