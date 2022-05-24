package com.example.application.ui.Class




import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import retrofit2.Call
import android.util.Log
import android.widget.Toast
import com.example.application.SharedPreferences
import com.example.application.databinding.ActivityClassCreateBinding
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.*
import com.example.application.ServiceGenerator
import com.example.application.network.response.*
import com.example.application.network.service.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import retrofit2.http.PartMap
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.view.View
import android.widget.CompoundButton
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.http.POST
import retrofit2.http.Multipart
import java.io.File
import java.util.*
import kotlin.collections.HashMap




class ClassCreateActivity  : AppCompatActivity() {

    private var startDate = ""
    private var startTime = ""
    private var endDate = ""
    private var endTime = ""
    private var category = ""
    private var onoffline = ""

    private lateinit var binding: ActivityClassCreateBinding
    private var GALLERY =0
    var photoUri : Uri? = null
    var path :String? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClassCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val TOKEN = SharedPreferences.prefs.getString("key","key")
        val ClassCreateService = ServiceGenerator.createService(ClassCreateInterface::class.java, TOKEN)

        val topicId = intent.getExtras()?.getString("topicId")


        binding.btnNextPage.setOnClickListener{
            binding.createFirstPage.visibility= View.GONE
            binding.createFirstPage.isEnabled=false
            binding.createSecendPage.visibility= View.VISIBLE
            binding.createSecendPage.isEnabled=true

        }
        binding.btnCreateClassStartDate.setOnClickListener {
            val cal = Calendar.getInstance()
            val dateSetListener =
                DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    startDate = "${year}-${month + 1}-${dayOfMonth}"
                    binding.btnCreateClassStartDate.text = "${year}년 ${month + 1}월 ${dayOfMonth}일"
                }
            DatePickerDialog(
                this,
                dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }//시작날짜
        binding.btnCreateClassStartTime.setOnClickListener {
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                startTime = "${hourOfDay}:${minute}"
                binding.btnCreateClassStartTime.text = "${hourOfDay}시 ${minute}분"
            }
            TimePickerDialog(
                this,
                timeSetListener,
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                true
            ).show()
        }//시작시간
        binding.btnCreateClassEndDate.setOnClickListener {
            val cal = Calendar.getInstance()
            val dateSetListener =
                DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    endDate = "${year}-${month + 1}-${dayOfMonth}"
                    binding.btnCreateClassEndDate.text = "${year}년 ${month + 1}월 ${dayOfMonth}일"
                }
            DatePickerDialog(
                this,
                dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }//종료날짜
        binding.btnCreateClassEndTime.setOnClickListener {
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                endTime = "${hourOfDay}:${minute}"
                binding.btnCreateClassEndTime.text = "${hourOfDay}시 ${minute}분"
            }
            TimePickerDialog(
                this,
                timeSetListener,
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                true
            ).show()
        }//종료시간
        binding.btnImageSelect.setOnClickListener {
            val intent: Intent = Intent(Intent.ACTION_PICK)
            intent.setType("image/*")
            startActivityForResult(intent,GALLERY)
        }

        val checkboxListener = CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                when(buttonView){
                    binding.checkOnline -> {
                        onoffline="1"
                        binding.checkOffline.isChecked=false
                    }
                    binding.checkOffline -> {
                        onoffline="2"
                        binding.checkOnline.isChecked=false
                    }
                }
            }
        }
        binding.checkOnline.setOnCheckedChangeListener(checkboxListener)
        binding.checkOffline.setOnCheckedChangeListener(checkboxListener)
        //retrofit

        binding.btnCreateClass.setOnClickListener {
            if(binding.checkA.isChecked==true) category+="A "
            if(binding.checkB.isChecked==true) category+="B "
            if(binding.checkC.isChecked==true) category+="C "
            if(binding.checkD.isChecked==true) category+="D "

            val classnameRequestBody: RequestBody = binding.editClassName.text.toString().toPlainRequestBody()
            val classPriceRequestBody: RequestBody  = binding.editClassPrice.text.toString().toPlainRequestBody()
            val classInfoRequestBody: RequestBody  = binding.editClassInfo.text.toString().toPlainRequestBody()
            val start_dateRequestBody: RequestBody = startDate.toPlainRequestBody()
            val start_timeRequestBody: RequestBody = startTime.toPlainRequestBody()
            val end_dateRequestBody: RequestBody = endDate.toPlainRequestBody()
            val end_timeRequestBody: RequestBody = endTime.toPlainRequestBody()
            val classOnOfflineRequestBody: RequestBody = onoffline.toPlainRequestBody()
            val classAddressRequestBody: RequestBody = binding.editClassAddress.text.toString().toPlainRequestBody()
            val classCategoryRequestBody: RequestBody = category.toPlainRequestBody()



            val textHashMap = hashMapOf<String, RequestBody>()
            textHashMap["className"] = classnameRequestBody
            textHashMap["classPrice"] = classPriceRequestBody
            textHashMap["classInfo"] = classInfoRequestBody
            textHashMap["start_date"] = start_dateRequestBody
            textHashMap["start_time"] = start_timeRequestBody
            textHashMap["end_date"] = end_dateRequestBody
            textHashMap["end_time"] = end_timeRequestBody
            //textHashMap["day_time"] = end_timeRequestBody
            textHashMap["classOnOffline"] = classOnOfflineRequestBody
            textHashMap["classAddress"] = classAddressRequestBody
            textHashMap["classCategory"] = classCategoryRequestBody





            val file = File(path)
            val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
            val body = MultipartBody.Part.createFormData("filename", file.name, requestFile)

            Log.d("a", textHashMap.toString())

            if (binding.editClassName.text.toString() == "" || binding.editClassPrice.text.toString() == "") {
                val toast =
                    Toast.makeText(applicationContext, "빈칸 없이 입력하세요", Toast.LENGTH_SHORT)
                toast.show()
            }
            /*if ( binding.editClassInfo.text.toString().length < 10) {
                val toast = Toast.makeText(
                    applicationContext,
                    "수업 정보를 10자이상 입력해주세요",
                    Toast.LENGTH_SHORT
                )
                toast.show()
            }*/


            if(topicId!=null){
                Log.d("test516515","15615656156")
            }

            with(ClassCreateService) {
                creatClass(body,textHashMap).enqueue(object : Callback<resultResponse> {
                    override fun onResponse(call: Call<resultResponse>, response: Response<resultResponse>) {
                        val result = response.body()
                        when (result?.result) {
                            "success" -> {

                                val toast = Toast.makeText(applicationContext, "개설되었습니다", Toast.LENGTH_SHORT)
                                toast.show()

                                finish()
                            }//로그인성공
                            "fail" -> {
                                val toast = Toast.makeText(applicationContext, "개설에 실패했습니다", Toast.LENGTH_SHORT)
                                toast.show()
                            }//로그인실패
                            "internal" -> {
                                val toast = Toast.makeText(applicationContext, "잠시후 다시 시도해주세요", Toast.LENGTH_SHORT)
                                toast.show()
                            }//서버오류
                        }
                    }
                    override fun onFailure(call: Call<resultResponse>, t: Throwable) {
                        Log.e("실패", "${t}")
                    }
                })
            }
        }//장소 등록 버튼
    }

    override fun onActivityResult(requestCode: Int, resultCode:Int, data:Intent?){
        super.onActivityResult(requestCode,resultCode,data)
        if(requestCode==GALLERY)
            if(resultCode==Activity.RESULT_OK){
                photoUri=data?.data
                binding.ivClassImage.setImageURI(photoUri)
                path = absolutelyPath(photoUri!!)
            }else{
                finish()
            }
    }//갤러리 설정

    fun absolutelyPath(path: Uri): String {
        var proj: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
        var c: Cursor? = contentResolver.query(path, proj, null, null, null)
        var index = c!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        c.moveToFirst()
        var result = c.getString(index)
        return result
    }//갤러리 path https://machine-woong.tistory.com/174

    private fun String?.toPlainRequestBody() = requireNotNull(this).toRequestBody("text/plain".toMediaTypeOrNull())
    //저작권은 l2hyunwoo
}

