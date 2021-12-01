package com.pixieium.austtravels.home.stopwatch

import android.os.Handler
import android.os.Message
import android.widget.TextView
import com.pixieium.austtravels.R
import com.pixieium.austtravels.home.HomeRepository


class StopwatchHandler(private val timerTextView: TextView) : Handler() {
    private var timer: Stopwatch = Stopwatch()
    private val mDatabase: HomeRepository = HomeRepository()
    private var totalTimeElapsed: Long = 0

    override fun handleMessage(msg: Message) {
        when (msg.what) {
            MSG_START_TIMER -> {
                totalTimeElapsed = 0
                timer.start() //start timer
                this.sendEmptyMessage(MSG_UPDATE_TIMER)
            }
            MSG_UPDATE_TIMER -> {
                totalTimeElapsed = timer.elapsedTimeMilli
                timerTextView.text =
                    timerTextView.context.getString(
                        R.string.currently_sharing_location,
                        timer.elapsedTimeMin.toString(), timer.elapsedTimeSecs.toString()
                    )
                this.sendEmptyMessageDelayed(
                    MSG_UPDATE_TIMER,
                    REFRESH_RATE.toLong()
                ) //text view is updated every second,
            }
            MSG_STOP_TIMER -> {
                this.removeMessages(MSG_UPDATE_TIMER) // no more updates.
                timer.stop() //stop timer
                mDatabase.updateContribution(totalTimeElapsed)
            }
            else -> {
                // invalid message
            }
        }
    }

    companion object {
        private const val MSG_START_TIMER = 0
        private const val MSG_STOP_TIMER = 1
        private const val MSG_UPDATE_TIMER = 2
        private const val REFRESH_RATE = 100
    }

}

