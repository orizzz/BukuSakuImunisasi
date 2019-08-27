package com.gregetdev.oris.busa.model

class DataImunisasiModel{

    var nama_Imunisasi : String? = "nama_Imunisasi"
    var tgl_imunisasi :String? = "tgl_imunisasi"
    var status : String? = "status"
    var umurPemberian : String? = "umurPemberian"


    constructor()

    constructor(Nama_Imunisasi: String?, Tgl_imunisasi: String?, Status: String?, umurPemberian: String?) {
        this.nama_Imunisasi = Nama_Imunisasi
        this.tgl_imunisasi = Tgl_imunisasi
        this.status = Status
        this.umurPemberian = umurPemberian
    }

}