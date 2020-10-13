package com.skfo763.component.extensions

fun <T> List<T>.containsAtLeast(list: List<T>): Boolean {
    list.forEach {
        if(contains(it)) return true
    }
    return false
}