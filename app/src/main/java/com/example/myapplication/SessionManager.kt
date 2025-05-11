import android.content.Context
import android.content.Context.MODE_PRIVATE
import androidx.core.content.edit

object SessionManager{
    private const val KEY_PATIENT_ID = "patient_id"
    private const val KEY_DOCTOR_ID = "doctor_id"

    fun savePatientId(context: Context, id: Int){
        context.getSharedPreferences("AppPrefs", MODE_PRIVATE).edit(){
            putInt(KEY_PATIENT_ID, id)
            apply()
        }
    }

    fun saveDoctorId(context: Context, id: Int){
        context.getSharedPreferences("AppPrefs", MODE_PRIVATE).edit(){
            putInt(KEY_DOCTOR_ID, id)
            apply()
        }
    }
    fun getPatientId(context: Context): Int{
        return context.getSharedPreferences("AppPrefs", MODE_PRIVATE)
            .getInt(KEY_PATIENT_ID, -1)
    }
    fun getDoctorId(context: Context): Int{
        return context.getSharedPreferences("AppPrefs", MODE_PRIVATE)
            .getInt(KEY_DOCTOR_ID, -1)
    }
}