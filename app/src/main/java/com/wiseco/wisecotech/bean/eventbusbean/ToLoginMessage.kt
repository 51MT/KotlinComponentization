package com.wiseco.wisecotech.bean.eventbusbean

/**
 * 登录EventBus的bean类
 */
class ToLoginMessage {
    private var type: Int = 0

    fun getType(): Int {
        return type
    }

    fun setType(type: Int): ToLoginMessage {
        this.type = type
        return this
    }
}
