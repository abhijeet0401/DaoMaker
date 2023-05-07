package com.example.voteapp.ui.main.startView

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.voteapp.PlanActivity
import com.example.voteapp.R
import com.example.voteapp.adapter.FeedAdapter
import com.example.voteapp.data.Feed
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList

class FeedFragment : Fragment() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_feed, container, false)
        initView(view)
        return view
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initView(view: View) {
        val recycler_feed = view.findViewById<RecyclerView>(R.id.recycler_feed)
        val feed1 = Feed("Nag", LocalDateTime.of(2023,1,1,18,12,31),"Oh DAO, thou art a creature of the new age,\n" +
                "Whose governance lies not with kings or sage,\n" +
                "But with code and contracts immutable." )
        val feed2 = Feed("Pista", LocalDateTime.of(2023,3,20,10,15,54),"Oh DAO, thou art a wondrous thing to see,\n" +
                "A symbol of democracy in its purest form,\n" +
                "That doth challenge the status quo with fervor.")
        val feed3 = Feed("Kereszti", LocalDateTime.of(2023,3,20,10,15,54), "May thy presence grow and spread far and wide,\n" +
                "And may thy principles forever abide,\n" +
                "As thou dost pave the way for a better tomorrow." )

        val feedList = ArrayList<Feed>()
        feedList.add(feed1)
        feedList.add(feed2)
        feedList.add(feed3)
        recycler_feed.adapter = FeedAdapter(context!!,feedList)
        recycler_feed.layoutManager = LinearLayoutManager(context)

    }

    private fun openVote() {
        val intent: Intent = Intent(context,PlanActivity::class.java)
        startActivity(intent)
    }
}
