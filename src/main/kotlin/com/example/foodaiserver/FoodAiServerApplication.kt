package com.example.foodaiserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class FoodAiServerApplication

fun main(args: Array<String>) {
	runApplication<FoodAiServerApplication>(*args)
}
