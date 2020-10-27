package com.east.evil.huxlyn.entity

open class Loading{
    var message : String? = null;
    var time : Long = 0;
    var type : LoadingType = LoadingType.TYPE_TOAST;

    constructor(){
        type = LoadingType.TYPE_TOAST;
    }

    constructor(message:String,type: LoadingType = LoadingType.TYPE_TOAST){
        this.type = type;
        this.message = message;
    }

    constructor(message:String,type: LoadingType = LoadingType.TYPE_TOAST,time : Long = 0){
        this.type = type;
        this.message = message;
        this.time = time;
    }

    enum class LoadingType{
        TYPE_VIEW,
        TYPE_TOAST,
        TYPE_DIALOG;
    }
}