package com.east.evil.huxlyn.entity

import kotlin.properties.Delegates

class VMData {
    var code by Delegates.notNull<Code>();

    var requestCode : Int = 0;

    var msg:String? = null;

    var error : Error = Error();


    enum class Code(code : Int){
        CODE_SUCCESS(88),
        CODE_DEFAULT(0),
        CODE_SHOW_MSG(1),
        CODE_ERROR(-1);
    }

    companion object{
        const val REQUEST_CODE_DEFAULT = 0x00;
        const val ERROR_CODE_DEFAULT = -1;
        const val SUCCESS_CODE_DEFAULT = 88;
    }
}