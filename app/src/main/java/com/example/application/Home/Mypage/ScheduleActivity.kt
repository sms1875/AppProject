package com.example.application

import android.app.Activity
import android.graphics.drawable.Drawable
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.application.databinding.ActivityScheduleBinding
import com.google.gson.annotations.SerializedName
import com.prolificinteractive.materialcalendarview.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.POST
import java.text.SimpleDateFormat
import java.util.*


class ScheduleActivity : AppCompatActivity() {


    private var openedClassScheduleList = mutableListOf<scheduleData>()
    private var registeredClassScheduleList = mutableListOf<scheduleData>()
    private var schedulett = mutableListOf<schedulelist>()

    private lateinit var binding: ActivityScheduleBinding

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityScheduleBinding.inflate(layoutInflater)

        val materialCalendarView = binding.calendarView

        val TOKEN=SharedPreferences.prefs.getString("key","key")
        val getScheduleService = ServiceGenerator.createService(getScheduleInterface::class.java,TOKEN)

        getScheduleService.getSchedule().enqueue(object : Callback<scheduleDataList> {
            override fun onResponse(call: Call<scheduleDataList>, response: Response<scheduleDataList>) {
                if (response.body()?.opendClass!=null)
                    openedClassScheduleList = response.body()?.opendClass as ArrayList<scheduleData>
                if (response.body()?.registeredClass!=null)
                    registeredClassScheduleList = response.body()?.registeredClass as ArrayList<scheduleData>

                Log.d("33333", "성공 : ${openedClassScheduleList}")
                Log.d("44444", "성공 : ${registeredClassScheduleList}")

                for(i in 0 .. openedClassScheduleList.size-1) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        dateInt(openedClassScheduleList[i], materialCalendarView,binding.textView2)
                    }
                }
                for(i in 0 .. registeredClassScheduleList.size-1) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        dateInt(registeredClassScheduleList[i], materialCalendarView,binding.textView2)
                    }
                }

            }
            override fun onFailure(call: Call<scheduleDataList>, t: Throwable) {
                Log.d("결과:", "실패 : $t")
            }
        })



        //openedClassScheduleList = scdlist as MutableList<scheduleData>




        materialCalendarView.state().edit()
            .setMinimumDate(CalendarDay.from(getCurrentYear()-1, getCurrentMonth(),getCurrentDay() + 1))
            .setMaximumDate(CalendarDay.from(getCurrentYear()+1, getCurrentMonth(),getCurrentDay() + 1))
            .setCalendarDisplayMode(CalendarMode.MONTHS)
            .commit()


        setContentView(binding.root)
    }

    class CurrentDayDecorator(context: Activity?, currentDay: CalendarDay) : DayViewDecorator {
        private val drawable: Drawable?
        var myDay = currentDay
        override fun shouldDecorate(day: CalendarDay): Boolean {
            return day == myDay
        }

        override fun decorate(view: DayViewFacade) {
            view.setSelectionDrawable(drawable!!)
            view.setBackgroundDrawable(drawable)
        }

        init {
            drawable = ContextCompat.getDrawable(context!!, R.drawable.ic_launcher_background)
        }
    }

    // 현재 Year
    @RequiresApi(Build.VERSION_CODES.N)
    fun getCurrentYear(): Int = Calendar.getInstance().get(Calendar.YEAR)
    // 현재 Month
    @RequiresApi(Build.VERSION_CODES.N)
    fun getCurrentMonth(): Int = Calendar.getInstance().get(Calendar.MONTH) + 1
    // 현재 Day
    @RequiresApi(Build.VERSION_CODES.N)
    fun getCurrentDay(): Int = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)

    @RequiresApi(Build.VERSION_CODES.O)

    fun dateInt(strDate:scheduleData,materialCalendarView: MaterialCalendarView,textview:TextView) {

        val calList = ArrayList<CalendarDay>()
        if(strDate.start_date!=null || strDate.end_date!=null) {
            var format1: Date = SimpleDateFormat("yyyy-MM-dd").parse(strDate.start_date)
            val format2: Date = SimpleDateFormat("yyyy-MM-dd").parse(strDate.end_date)


            val cal = Calendar.getInstance()
            cal.time = format1

            val diffSec = (format2.time - format1.time) / 1000 //초 차이
            val diffDays: Long = diffSec / (24 * 60 * 60) //일자수 차이
            calList.add(CalendarDay.from(2022, 4, 1))

            for (i in 1..diffDays) {
                if (format1.date == cal.getMaximum(Calendar.DAY_OF_MONTH)) {
                    format1.month++
                    format1.date = 1
                }
                calList.add(CalendarDay.from(2022, format1.month, format1.date++))
                schedulett.add(schedulelist("class : ${strDate.className} \nstart : ${strDate.start_time} \n end : ${strDate.end_time}",
                    calList[i.toInt()].date))
            }


            format1 = SimpleDateFormat("yyyy-MM-dd").parse(strDate.start_date)

            materialCalendarView.setOnDateChangedListener { widget, date, selected ->
                for(i in schedulett.iterator()){
                    if(i.date==date.date) {
                        binding.textView2.text=i.schedule
                        break
                    }
                    else {
                        binding.textView2.text=""
                    }
                }
             }

            /*materialCalendarView.setOnDateChangedListener { widget, date, selected ->
                if (format1 <= date.date && date.date <= format2) {
                    Toast.makeText(this, "class : ${strDate.className} \nstart : ${strDate.start_time} \n end : ${strDate.end_time}",
                        Toast.LENGTH_SHORT).show()
                }
            }*/


            for (calDay in calList) {
                materialCalendarView.addDecorators(CurrentDayDecorator(this, calDay))
            }
        }
    }
}




interface getScheduleInterface {
    @POST("/db/class/getList")
    fun getSchedule(): Call<scheduleDataList>
}

