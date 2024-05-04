package com.example.lion_nav_barhomepage.doctor

data class Data(
    var id: Int ?=null,
    var name:String?=null,
    var spec: String?=null,
    var about:String?=null,
    var img_url:String?=null,
    var exp:String?=null,
    var avl: ArrayList<Int>?=null,
    var timeslots: List<List<Int>>?=null,
    var lang: String?=null,
    var fee: String ?=null
)