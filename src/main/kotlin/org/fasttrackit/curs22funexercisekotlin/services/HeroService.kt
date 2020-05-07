package org.fasttrackit.curs22funexercisekotlin.services

import org.fasttrackit.curs22funexercisekotlin.domain.Hero
import org.fasttrackit.curs22funexercisekotlin.domain.HeroReader
import org.fasttrackit.curs22funexercisekotlin.exceptions.ResourceNotFoundException
import org.springframework.stereotype.Service

@Service
class HeroService {

    val heroes = mutableListOf<Hero>()
    private final val reader = HeroReader(fileLocation = "heroes.txt")

    init {
        reader.read().forEach { hero -> addHero(hero) }
    }

    fun getAllHeroes() = heroes.toList()

    fun addHero(hero: Hero): Hero {
        val newHero = Hero(fetchLatestId(), hero.name, hero.skill, hero.stamina)
        addHeroInList(newHero)
        return newHero
    }

    fun deleteHero(id: Int): Hero {
        val heroToDelete = getOrThrow(id)
        heroes.remove(heroToDelete)
        return heroToDelete
    }

    fun updateHero(id: Int, hero: Hero): Hero {
        val heroToUpdate = getOrThrow(id)
        heroes.remove(heroToUpdate)
        val newHero = Hero(id, hero.name, hero.skill, hero.stamina, heroToUpdate.wins)
        addHeroInList(newHero)
        return newHero
    }

    fun getHeroById(id: Int) = getOrThrow(id)

    private fun fetchLatestId(): Int {
        val existingIds = heroes.map { it.id }.toSet()
        val allInts = (1..Int.MAX_VALUE).asSequence()
        return allInts.first { !existingIds.contains(it) }
    }

    private fun getOrThrow(id: Int): Hero = heroes.find { it.id == id }
            ?: throw ResourceNotFoundException("Could not find hero with id $id")

    private fun addHeroInList(hero: Hero) = heroes.add(hero.id - 1, hero)
}