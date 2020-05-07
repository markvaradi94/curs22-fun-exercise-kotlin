package org.fasttrackit.curs22funexercisekotlin.domain

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.Resource
import org.springframework.stereotype.Component
import java.io.IOException
import java.io.InputStream
import java.lang.RuntimeException
import java.util.*
import kotlin.properties.Delegates

data class Hero(val id: Int,
                @JsonProperty val name: String,
                @JsonProperty val skill: Int,
                @JsonProperty val stamina: Int,
                var wins: Int) {

    constructor(name: String, skill: Int, stamina: Int) : this(0, name, skill, stamina, 0)
    constructor(id: Int, name: String, skill: Int, stamina: Int) : this(id, name, skill, stamina, 0)

    fun addWin() = wins++
}

class HeroReader() {

    lateinit var file: Resource

    constructor(@Value("\$heroes.location:default.txt") fileLocation: String) : this() {
        file = ClassPathResource(fileLocation)
        if (!file.exists()) throw RuntimeException("Could not find the file in classpath $fileLocation")
    }

    fun read(): List<Hero> {
        val result = mutableListOf<Hero>()
        try {
            val inputStream: InputStream = file.inputStream
            val scanner = Scanner(inputStream)
            scanner.useDelimiter("[|]")
            while (scanner.hasNextLine()) {
                val tokens = scanner.nextLine().split("|")
                result.add(Hero(
                        tokens[0],
                        tokens[1].toInt(),
                        tokens[2].toInt()
                ))
            }
            inputStream.close()
        } catch (exception: IOException) {
            System.err.println(exception.message)
        }
        return result.toList()
    }
}