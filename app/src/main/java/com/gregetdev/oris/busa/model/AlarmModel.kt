package com.gregetdev.oris.busa.model

class AlarmModel {

    var Ketarangan_alaram : String = ""
    var Tgl_alarm : String = ""
    var Waktu_alarm : String = ""
    var Status : Boolean = false
    var urutan : String = ""

    constructor()
    constructor(Ketarangan_alaram: String, Tgl_alarm: String, Waktu_alarm: String, Status: Boolean, urutan: String) {
        this.Ketarangan_alaram = Ketarangan_alaram
        this.Tgl_alarm = Tgl_alarm
        this.Waktu_alarm = Waktu_alarm
        this.Status = Status
        this.urutan = urutan
    }


}