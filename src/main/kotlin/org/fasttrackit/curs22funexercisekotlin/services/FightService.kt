package org.fasttrackit.curs22funexercisekotlin.services

import org.fasttrackit.curs22funexercisekotlin.domain.Fight
import org.fasttrackit.curs22funexercisekotlin.domain.FightReader
import org.fasttrackit.curs22funexercisekotlin.domain.FightResult
import org.fasttrackit.curs22funexercisekotlin.domain.Hero
import org.fasttrackit.curs22funexercisekotlin.exceptions.ResourceNotFoundException
import org.springframework.stereotype.Service
import java.text.DecimalFormat
import java.util.*

@Service
class FightService(val heroService: HeroService) {

    val fights = mutableListOf<Fight>()
    private final val reader = FightReader(fileLocation = "fights.txt")

    init {
        reader.read().forEach { fight -> addFight(fight) }
    }

    fun getAllFights() = fights.toList()

    fun addFight(fight: Fight): Fight {
        val newFight = Fight(fetchLatestId(), fight.firstFighter, fight.secondFighter)
        val fightWithResult = fightWithResult(newFight)
        fights.add(fightWithResult)
        return fightWithResult
    }

    fun getFightById(id: Int) = getOrThrow(id)

    fun fightsInvolvingHero(id: Int) = fights.filter { isInFight(it, id) }

    private fun fetchLatestId(): Int {
        val existingIds = fights.map { it.id }.toSet()
        val allInts = (1..Int.MAX_VALUE).asSequence()
        return allInts.first { !existingIds.contains(it) }
    }

    private fun getOrThrow(id: Int) = fights.find { it.id == id }
            ?: throw ResourceNotFoundException("Could not find fight with id $id")

    private fun isInFight(fight: Fight, heroId: Int) = fight.firstFighter == heroId || fight.secondFighter == heroId

    private fun generateDamage(): Double {
        val df = DecimalFormat("#.####")
        val random = Random()
        return df.format(random.nextDouble()).toDouble()
    }

    private fun damagedStamina(hero: Hero): Int {
        val stamina = hero.stamina - generateDamage() * hero.skill
        return if (stamina > 0) stamina.toInt() else 0
    }

    private fun fightWithResult(fight: Fight): Fight {
        val hero1 = heroService.getHeroById(fight.firstFighter)
        val hero2 = heroService.getHeroById(fight.secondFighter)
        val damagedHero1 = Hero(hero1.name, hero1.skill, damagedStamina(hero1))
        val damagedHero2 = Hero(hero2.name, hero2.skill, damagedStamina(hero2))

        val result: FightResult

        if (damagedHero1.stamina > damagedHero2.stamina) {
            result = FightResult("${damagedHero1.name}, remaining stamina = ${damagedHero1.stamina}",
                    "${damagedHero2.name}, remaining stamina = ${damagedHero2.stamina}")
            hero1.addWin()
        } else if (damagedHero1.stamina < damagedHero2.stamina) {
            result = FightResult("${damagedHero2.name}, remaining stamina = ${damagedHero2.stamina}",
                    "${damagedHero1.name}, remaining stamina = ${damagedHero1.stamina}")
            hero2.addWin()
        } else {
            result = FightResult("Tie, remaining stamina = ${damagedHero1.stamina}",
                    "Tie, remaining stamina = ${damagedHero2.stamina}")
        }
        return Fight(fetchLatestId(), fight.firstFighter, fight.secondFighter, result)
    }
}