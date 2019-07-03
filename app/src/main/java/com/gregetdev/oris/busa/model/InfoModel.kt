package com.gregetdev.oris.busa.model

class InfoModel {

    var Nama_Imunisasi : String? = null
    var Deskripsi : String? = null
    var Manfaat  : String? = null
    var EfekSamping   : String? = null

    constructor()
    constructor(Nama_Imunisasi: String?, Deskripsi: String?, Manfaat: String?, EfekSamping: String?) {
        this.Nama_Imunisasi = Nama_Imunisasi
        this.Deskripsi = Deskripsi
        this.Manfaat = Manfaat
        this.EfekSamping = EfekSamping
    }


}