package com.east.evil.huxlyn.entity

class Error constructor(){
    var erroCode : Int = 0;
    var errorMsg : String? = null;
    var type : Int = 0;

    constructor(errorCode:Int,errorMsg :String) : this() {
        this.erroCode = erroCode;
        this.errorMsg = errorMsg;
    }

}