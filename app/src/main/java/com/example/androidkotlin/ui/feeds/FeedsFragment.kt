package com.example.androidkotlin.ui.feeds

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.androidkotlin.MainActivity
import com.example.androidkotlin.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class FeedsFragment : Fragment() {

    private lateinit var feedsViewModel: FeedsViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        feedsViewModel =
                ViewModelProvider(this).get(FeedsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_feeds, container, false)
//        val textView: TextView = root.findViewById(R.id.text_home)
//        feedsViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })
        val list:ArrayList<String> = ArrayList()
        list.add("Shashank")
        list.add("Shefali")
        list.add("Deepa")
        list.add("Neha")


        val recyclerView:RecyclerView=root.findViewById<RecyclerView>(R.id.feeds_recycler_view)
        recyclerView.adapter=FeedsAdapter(list);
        recyclerView.setPadding(0,0,0,100)
        return root
    }
}