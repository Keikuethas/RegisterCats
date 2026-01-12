package com.keikuethas.registercats

import kotlinx.serialization.Serializable
import java.util.UUID
import kotlin.random.Random

@Serializable
data class Cat(
    var name: String,
    val type: Int = 0,
    var age: Int = Random.nextInt(0, 20),
    var tailLength: Int = 10,
    var favouriteFood: String = "Fish",
    val id: String = UUID.randomUUID().toString(),
    var clicks: Int = 0
) {
    val stringType: String
        get() = types[type]
    val imageType: Int
        get() = when (type) {
            0 -> R.mipmap.black_cat
            1 -> R.mipmap.white_cat
            2 -> R.mipmap.orange_cat
            else -> R.drawable.ic_launcher_background
        }

    companion object {
        val types = listOf("Black", "White", "Orange")

    }

    init {
        require(type in types.indices) { "type must be in 0..${types.size - 1}" }
    }
}
