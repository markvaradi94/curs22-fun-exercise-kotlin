package org.fasttrackit.curs22funexercisekotlin.controllers

import org.fasttrackit.curs22funexercisekotlin.domain.Fight
import org.fasttrackit.curs22funexercisekotlin.services.FightService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("fights")
class FightController(val fightService: FightService) {

    @GetMapping
    fun getAllFights(@RequestParam(required = false) heroId: Int?): List<Fight> {
        return if (heroId == null) {
            fightService.getAllFights()
        } else {
            fightService.fightsInvolvingHero(heroId)
        }
    }

    @GetMapping("{id}")
    fun getFightById(@PathVariable id: Int) = fightService.getFightById(id)

    @PostMapping
    fun createFight(@RequestBody fight: Fight) = fightService.addFight(fight)

    @PostMapping("/create")
    fun createFightByHeroIds(@RequestParam(required = false) firstFighter: Int,
                             @RequestParam(required = false) secondFighter: Int) =
            fightService.addFight(Fight(firstFighter, secondFighter))
}