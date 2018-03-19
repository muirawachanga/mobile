package com.bituls.phonecall

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button

class MainActivity : Activity() {

    internal val context: Context = this
    private var button: Button? = null

    public override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button = findViewById<View>(R.id.buttonCall) as Button

        // add PhoneStateListener
        val phoneListener = PhoneCallListener()
        val telephonyManager = this
                .getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        telephonyManager.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE)

        // add button listener
        button!!.setOnClickListener {
            val callIntent = Intent(Intent.ACTION_CALL)
            callIntent.data = Uri.parse("tel:0377778888")
            startActivity(callIntent)
        }

    }

    //monitor phone call activities
    private inner class PhoneCallListener : PhoneStateListener() {

        private var isPhoneCalling = false

        internal var LOG_TAG = "LOGGING 123"

        override fun onCallStateChanged(state: Int, incomingNumber: String) {

            if (TelephonyManager.CALL_STATE_RINGING == state) {
                // phone ringing
                Log.i(LOG_TAG, "RINGING, number: " + incomingNumber)
            }

            if (TelephonyManager.CALL_STATE_OFFHOOK == state) {
                // active
                Log.i(LOG_TAG, "OFFHOOK")

                isPhoneCalling = true
            }

            if (TelephonyManager.CALL_STATE_IDLE == state) {
                // run when class initial and phone call ended,
                // need detect flag from CALL_STATE_OFFHOOK
                Log.i(LOG_TAG, "IDLE")

                if (isPhoneCalling) {

                    Log.i(LOG_TAG, "restart app")

                    // restart app
                    val i = baseContext.packageManager
                            .getLaunchIntentForPackage(
                                    baseContext.packageName)
                    i!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(i)

                    isPhoneCalling = false
                }

            }
        }
    }

}