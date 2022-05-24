package com.example.application.ui.Home.Menu.Place



import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import retrofit2.Call
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast

import com.example.application.network.response.*
import com.example.application.SharedPreferences
import com.example.application.databinding.ActivityPlaceRegisterBinding
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.*
import java.util.*
import com.example.application.ServiceGenerator
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import androidx.recyclerview.widget.LinearLayoutManager


import kotlin.collections.ArrayList
import com.example.application.network.service.*

class PlaceRegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlaceRegisterBinding

    private var startDate = ""
    private var startTime = ""
    private var endDate = ""
    private var endTime = ""

    var path: ArrayList<String> = ArrayList()
    var list = ArrayList<Uri>()
    val adapter = MultiImageAdapter(list, this)


    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val TOKEN = SharedPreferences.prefs.getString("key", "token")
        val placeRegisterService =
            ServiceGenerator.createService(placeRegisterInterface::class.java, TOKEN)
        //retrofit

        binding = ActivityPlaceRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /******************************************************************************************/
        binding.btnStartDate.setOnClickListener {
            val cal = Calendar.getInstance()
            val dateSetListener =
                DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    startDate = "${year}-${month + 1}-${dayOfMonth}"
                    binding.btnStartDate.text = "${year}년 ${month + 1}월 ${dayOfMonth}일"
                }
            DatePickerDialog(
                this,
                dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }//시작날짜
        binding.btnStartTime.setOnClickListener {
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                startTime = "${hourOfDay}:${minute}"
                binding.btnStartTime.text = "${hourOfDay}시 ${minute}분"
            }
            TimePickerDialog(
                this,
                timeSetListener,
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                true
            ).show()
        }//시작시간
        binding.btnEndDate.setOnClickListener {
            val cal = Calendar.getInstance()
            val dateSetListener =
                DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    endDate = "${year}-${month + 1}-${dayOfMonth}"
                    binding.btnEndDate.text = "${year}년 ${month + 1}월 ${dayOfMonth}일"
                }
            DatePickerDialog(
                this,
                dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }//종료날짜
        binding.btnEndTime.setOnClickListener {
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                endTime = "${hourOfDay}:${minute}"
                binding.btnEndTime.text = "${hourOfDay}시 ${minute}분"
            }
            TimePickerDialog(
                this,
                timeSetListener,
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                true
            ).show()
        }//종료시간
        /******************************************************************************************/

        var recyclerview = binding.ivTest
        val layoutManager = LinearLayoutManager(this)
        recyclerview.layoutManager = layoutManager
        recyclerview.adapter = adapter


        binding.btnCaptureImage.setOnClickListener {

        }

        binding.btnUploadImgae.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(intent, 200)
        }
        /******************************************************************************************/
        binding.btnPlaceRegister.setOnClickListener {
            val placenameRequestBody: RequestBody =
                binding.editPlacename.text.toString().toPlainRequestBody()
            val postRequestBody: RequestBody =
                binding.editPosition.text.toString().toPlainRequestBody()
            val typeRequestBody: RequestBody = binding.editType.text.toString().toPlainRequestBody()
            val detailRequestBody: RequestBody =
                binding.editDetail.text.toString().toPlainRequestBody()
            val costRequestBody: RequestBody = binding.editCost.text.toString().toPlainRequestBody()
            val start_dateRequestBody: RequestBody = startDate.toPlainRequestBody()
            val start_timeRequestBody: RequestBody = startTime.toPlainRequestBody()
            val end_dateRequestBody: RequestBody = endDate.toPlainRequestBody()
            val end_timeRequestBody: RequestBody = endTime.toPlainRequestBody()
            val textHashMap = hashMapOf<String, RequestBody>()
            textHashMap["placename"] = placenameRequestBody
            textHashMap["position"] = postRequestBody
            textHashMap["type"] = typeRequestBody
            textHashMap["detail"] = detailRequestBody
            textHashMap["cost"] = costRequestBody
            textHashMap["start_date"] = start_dateRequestBody
            textHashMap["start_time"] = start_timeRequestBody
            textHashMap["end_date"] = end_dateRequestBody
            textHashMap["end_time"] = end_timeRequestBody
            textHashMap["placeCategory"] = "A".toRequestBody()

            var images = ArrayList<MultipartBody.Part>()

            for (index in 0..path.size - 1) {
                val file = File(path[index])
                val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
                images.add(MultipartBody.Part.createFormData("filename", file.name, requestFile))
            }

            with(placeRegisterService) {
                placeRegister(images, textHashMap).enqueue(object : Callback<resultResponse> {
                    override fun onResponse(
                        call: Call<resultResponse>,
                        response: Response<resultResponse>
                    ) {
                        val result = response.body()
                        when (result?.result) {
                            "success" -> {
                                val toast = Toast.makeText(
                                    applicationContext,
                                    "등록되었습니다",
                                    Toast.LENGTH_SHORT
                                )
                                toast.show()

                                finish()
                            }//로그인성공
                            "fail" -> {
                                val toast = Toast.makeText(
                                    applicationContext,
                                    "등록에 실패했습니다",
                                    Toast.LENGTH_SHORT
                                )
                                toast.show()
                            }//로그인실패
                            "internal" -> {
                                val toast = Toast.makeText(
                                    applicationContext,
                                    "잠시후 다시 시도해주세요",
                                    Toast.LENGTH_SHORT
                                )
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

    /******************************************************************************************/

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)



        if (resultCode == RESULT_OK && requestCode == 200) {
            list.clear()

            if (data?.clipData != null) { // 사진 여러개 선택한 경우
                val count = data.clipData!!.itemCount
                if (count > 10) {
                    Toast.makeText(applicationContext, "사진은 10장까지 선택 가능합니다.", Toast.LENGTH_LONG)
                    return
                }
                for (i in 0 until count) {
                    val imageUri = data.clipData!!.getItemAt(i).uri
                    list.add(imageUri)
                    path.add(getImagePath(imageUri).toString())
                }

            } else { // 단일 선택
                data?.data?.let {
                    val imageUri: Uri? = data.data
                    if (imageUri != null) {
                        list.add(imageUri)
                        path.add(getImagePath(imageUri).toString())
                    }
                }
            }//
            adapter.notifyDataSetChanged()
        }//갤러리 설정
    }

    @SuppressLint("Range")
    fun getImagePath(uri: Uri?): String? {
        var cursor = contentResolver.query(uri!!, null, null, null, null)
        cursor!!.moveToFirst()
        var document_id = cursor.getString(0)
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1)
        cursor.close()
        cursor = contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            null, MediaStore.Images.Media._ID + " = ? ", arrayOf(document_id), null
        )
        cursor!!.moveToFirst()
        val path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
        cursor.close()
        return path
    }//uri->path 경로





    companion object {
        private const val TAG = "MultiImageActivity"
    }

    /******************************************************************************************/
    private fun String?.toPlainRequestBody() = requireNotNull(this).toRequestBody("text/plain".toMediaTypeOrNull()) //저작권 l2hyunwoo
}


