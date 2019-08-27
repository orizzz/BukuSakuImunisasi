package com.gregetdev.oris.busa.model

class AlarmModel {

    var Ketarangan_alaram : String = "ketarangan_alaram"
    var Tgl_alarm : String = "tgl_alarm"
    var Waktu_alarm : String = "waktu_alarm"
    var Status : Boolean = false
    var urutan : String = "urutan"

    constructor()
    constructor(Ketarangan_alaram: String, Tgl_alarm: String, Waktu_alarm: String, Status: Boolean, urutan: String) {
        this.Ketarangan_alaram = Ketarangan_alaram
        this.Tgl_alarm = Tgl_alarm
        this.Waktu_alarm = Waktu_alarm
        this.Status = Status
        this.urutan = urutan
    }


}