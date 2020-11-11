package com.east.evil.huxlyn.entity

import android.os.Bundle
import java.io.Serializable

class Target private constructor(builder:Builder){
    var cls : Class<*>? = null;
    var bundle : Bundle;
    var isFinish : Boolean = false;

    init {
        this.cls = builder.cls;
        this.bundle = builder.bundle;
        this.isFinish = builder.isFinish;
    }

    class Builder{
        var cls : Class<*>;
        var bundle : Bundle;
        var isFinish : Boolean = false;
        constructor(cls : Class<*>){
            this.cls = cls;
            bundle = Bundle();
        }

        fun put(key:String,value : Int) : Builder{
            bundle.putInt(key,value);
            return this;
        }

        fun put(key : String,value:String): Builder {
            bundle.putString(key,value);
            return this;
        }

        fun put(key:String,value : Serializable): Builder {
            bundle.putSerializable(key,value);
            return this;
        }

        fun isFinish(isFinish : Boolean): Builder {
            this.isFinish = isFinish;
            return this;
        }

        fun builder(): Target {
            return Target(this);
        }
    }
}