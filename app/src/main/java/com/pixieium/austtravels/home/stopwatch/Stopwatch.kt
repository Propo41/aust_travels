package com.pixieium.austtravels.home.stopwatch

class Stopwatch {
    private var startTime: Long = 0
    private var running = false
    private var currentTime: Long = 0
    fun start() {
        startTime = System.currentTimeMillis()
        running = true
    }

    fun stop() {
        running = false
    }

    fun pause() {
        running = false
        currentTime = System.currentTimeMillis() - startTime
    }

    fun resume() {
        running = true
        startTime = System.currentTimeMillis() - currentTime
    }

    val elapsedTimeMilli: Long
        get() {
            var elapsed: Long = 0
            if (running) {
                elapsed = (System.currentTimeMillis() - startTime)
            }
            return elapsed
        }

    val elapsedTimeSecs: Long
        get() {
            var elapsed: Long = 0
            if (running) {
                elapsed = (System.currentTimeMillis() - startTime) / 1000 % 60
            }
            return elapsed
        }

    val elapsedTimeMin: Long
        get() {
            var elapsed: Long = 0
            if (running) {
                elapsed = ((System.currentTimeMillis() - startTime) / 1000 / 60)
            }
            return elapsed
        }

    val elapsedTimeHour: Long
        get() {
            var elapsed: Long = 0
            if (running) {
                elapsed = (System.currentTimeMillis() - startTime) / 1000 / 60 / 60
            }
            return elapsed
        }

    override fun toString(): String {
        return (elapsedTimeHour.toString() + ":" + elapsedTimeMin + ":"
                + elapsedTimeSecs + "." + elapsedTimeMilli)
    }
}
