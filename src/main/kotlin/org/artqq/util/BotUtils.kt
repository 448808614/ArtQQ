package org.artqq.util

import kotlin.random.Random

object BotUtils {
    fun randomMd5(): String {
        return randomStr(16);
    }

    fun randomStr(len: Int): String{
        val str = "abcdefghijklmnopqrstuvwxyz0123456789"
        val sb = StringBuilder()
        for (i in (0 until len))
            sb.append(str[Random.nextInt(0, str.length)])
        return sb.toString()
    }

    fun randomNum(len: Int): String{
        val sb = StringBuilder()
        for (i in (0 until len)){
            sb.append(Random.nextInt(10))
        }
        return sb.toString()
    }

    fun regex(regex: String, text: String): String? {
        val r = Regex(regex)
        val find = r.find(text)
        return find?.value
    }

    fun regex(first: String, last: String , text: String): String? {
        val regex = "(?<=$first).*?(?=$last)"
        return regex(regex, text)
    }
}