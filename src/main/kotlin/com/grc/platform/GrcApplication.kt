package com.grc.platform

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class GrcApplication

fun main(args: Array<String>) {
    runApplication<GrcApplication>(*args)
}
