package com.example.voteapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_vote_history.*

class VoteHistoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vote_history)
        var totalVoteup :String="1"
        var totalVotedown: String ="1"
        var voteuptext= findViewById(R.id.textView26) as TextView
        var votedowntext= findViewById(R.id.textView27) as TextView
        val queue= Volley.newRequestQueue( baseContext)
        val queuedown= Volley.newRequestQueue( baseContext)
        val  stringRequest = StringRequest(
            Request.Method.GET,
            "https://web-production-eac9.up.railway.app/totalvoteup",
            { response ->
                try {
                    totalVoteup = response as String
                    Toast.makeText(
                        baseContext,
                        "Block Hash " + response,
                        Toast.LENGTH_LONG
                    ).show()
                    val stringRequestDown = StringRequest(
                        Request.Method.GET,
                        "https://web-production-eac9.up.railway.app/totalvotedown",
                        { response ->
                            try {
                                totalVotedown = response as String
                                Toast.makeText(
                                    baseContext,
                                    "Block Hash " + response,
                                    Toast.LENGTH_LONG
                                ).show()

                                votedowntext.text = totalVotedown
                                voteuptext.text = totalVoteup
                                var countvotedown = totalVotedown.toFloat()
                                var countvoteup = totalVoteup.toFloat()
                                var pervotedown = (countvotedown/(countvotedown+countvoteup))*100
                                var pervoteup = (countvoteup/(countvotedown+countvoteup))*100
                                var voteuptextperc= findViewById(R.id.textView28) as TextView
                                var votedowntextperc= findViewById(R.id.textView30) as TextView
                                voteuptextperc.text = pervoteup.toString()
                                votedowntextperc.text = pervotedown.toString()

                            } catch (exception: Exception) {
                                exception.printStackTrace()
                            }
                        },
                        { error ->
                            Toast.makeText(
                                baseContext,
                                error.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    )
                    queuedown.add(stringRequestDown)

                } catch (exception: Exception) {
                    exception.printStackTrace()
                }
            },
            { error ->
                Toast.makeText(
                    baseContext,
                    error.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        )
        queue.add(stringRequest)





        tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab:TabLayout.Tab) {
                when (tab.position){
                    0 -> {iv_graph.setImageResource(R.drawable.result1)}
                    1 -> {iv_graph.setImageResource(R.drawable.chart1)}
                }
            }
        })

}
}
