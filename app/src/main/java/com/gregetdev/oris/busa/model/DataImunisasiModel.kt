package com.gregetdev.oris.busa.model

class DataImunisasiModel{

    var Nama_Imunisasi : String? = null
    var Tgl_imunisasi :String? = null
    var Keterangan : String? = null
    var umurPemberian : String? = null


    constructor()

    constructor(Nama_Imunisasi: String?, Tgl_imunisasi: String?, Keterangan: String?, umurPemberian: String?) {
        this.Nama_Imunisasi = Nama_Imunisasi
        this.Tgl_imunisasi = Tgl_imunisasi
        this.Keterangan = Keterangan
        this.umurPemberian = umurPemberian
    }

}