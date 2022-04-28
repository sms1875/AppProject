package com.example.application

import android.content.Context
import android.widget.ArrayAdapter

class SearchOptionAdapter(context: Context?, resource: Int, objects: Array<String?>?) :
    ArrayAdapter<String?>(context!!, resource, objects!!) {
    override fun getCount(): Int {
        return super.getCount()
    }
}//검색 옵션 어댑터 설정
