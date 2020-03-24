package com.levkorol.weightloss.data

import android.content.Context

object UserRepository {

    var premium: Boolean = false

    fun setPremium(context: Context, premium: Boolean) {
        this.premium = premium
        // TODO отправляем событие
        // TODO и на сервере обновляем ещё
    }

}