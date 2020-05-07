package org.fasttrackit.curs22funexercisekotlin.domain

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.Resource
import java.io.IOException
import java.lang.RuntimeException
import java.util.*

data class Fight(val id: Int,
                 @JsonProperty val firstFighter: Int,
                 @JsonProperty val secondFighter: Int,
                 var result: FightResult?) {

    constructor(firstFighter: Int, secondFighter: Int) : this(0, firstFighter, secondFighter, FightResult())
    constructor(id: Int, firstFighter: Int, secondFighter: Int) : this(id, firstFighter, secondFighter, FightResult())
}

data class FightResult(val winner: String, val loser: String) {
    constructor() : this("", "")
}

class FightReader() {

    lateinit var file: Resource

    constructor(@Value("\$fights.location:default.txt") fileLocation: String) : this() {
        file = ClassPathResource(fileLocation)
        if (!file.exists()) throw RuntimeException("Could not find file in classpath $fileLocation")
    }

    fun read(): List<Fight> {
        val result = mutableListOf<Fight>()
        try {
            val inputStream = file.inputStream
            val scanner = Scanner(inputStream)
            scanner.useDelimiter("[|]")
            while (scanner.hasNextLine()) {
                val tokens = scanner.nextLine().split("|")
                result.add(Fight(
                        tokens[0].toInt(),
                        tokens[1].toInt()))
            }
            inputStream.close()
        } catch (exception: IOException) {
            System.err.println(exception.message)
        }
        return result.toList()
    }
}