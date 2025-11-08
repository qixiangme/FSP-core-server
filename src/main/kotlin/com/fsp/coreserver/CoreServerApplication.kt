package com.fsp.coreserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CoreServerApplication

fun main(args: Array<String>) {
    runApplication<CoreServerApplication>(*args)
}
