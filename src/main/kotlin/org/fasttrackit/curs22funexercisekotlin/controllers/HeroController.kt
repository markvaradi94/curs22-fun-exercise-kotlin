package org.fasttrackit.curs22funexercisekotlin.controllers

import org.fasttrackit.curs22funexercisekotlin.domain.Hero
import org.fasttrackit.curs22funexercisekotlin.services.HeroService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("heroes")
class HeroController(val heroService: HeroService) {

    @GetMapping
    fun getAllHeroes() = heroService.getAllHeroes()

    @GetMapping("{id}")
    fun getHeroById(@PathVariable id: Int) = heroService.getHeroById(id)

    @PostMapping
    fun addHero(@RequestBody hero: Hero) = heroService.addHero(hero)

    @PutMapping("{id}")
    fun updateHero(@PathVariable id: Int, @RequestBody hero: Hero) = heroService.updateHero(id, hero)

    @DeleteMapping("{id}")
    fun deleteHero(@PathVariable id: Int) = heroService.deleteHero(id)
}