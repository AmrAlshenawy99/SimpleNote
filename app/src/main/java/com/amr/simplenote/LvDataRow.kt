package com.amr.simplenote

class LvDataRow {
    var id:String?=null
    var title:String?=null
    var noteText:String?=null
    var timestamp: String?=null


    constructor(){}
    constructor(
        id: String,
        title: String,
        noteText: String,
        timestamp: String
    ){
        this.id=id
        this.title=title
        this.noteText=noteText
        this.timestamp=timestamp

    }

}