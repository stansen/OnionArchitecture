package com.onlion.basearchitecture

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.onlion.onionshell.log.logd
import com.onlion.onionshell.log.logdN
import com.onlion.onionshell.log.setLogTag
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        logd { "On create" }
//        logdN("a")
//        logdN("oncreate")
        button.setOnClickListener {
//            logd { "On click" }
            setLogTag("OnClick")
            logd("onclick")
            logdN("dfasdfsf")
        }

    }

    override fun onResume() {
        super.onResume()

    }
}
