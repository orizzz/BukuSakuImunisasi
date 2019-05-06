package com.gregetdev.oris.busa

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
//var NamaBayi: String, var TglLahir: String, var Umur: String, var ParentID: String,var ImageURL: String,var Jekel :String
//constructor():this("","","","","","")
/*@Parcelize*/
class BayiModel{

    var namaBayi : String? = null
    var tglLahir : String? = null
    var umur : String? = null
    var parentID : String? = null
    var imageURL : String? = null
    var jekel : String? = null

    constructor()

    constructor(
        namaBayi: String?,
        tglLahir: String?,
        umur: String?,
        parentID: String?,
        imageURL: String?,
        jekel: String?
    ) {
        this.namaBayi = namaBayi
        this.tglLahir = tglLahir
        this.umur = umur
        this.parentID = parentID
        this.imageURL = imageURL
        this.jekel = jekel
    }
}




