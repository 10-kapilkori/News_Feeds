package com.task.newsapp.ui.saved

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.task.newsapp.R
import com.task.newsapp.UserApplication
import com.task.newsapp.adapter_viewholder.SavedNewsAdapter
import com.task.newsapp.databinding.SavedNewsFragmentBinding
import com.task.newsapp.entity.NewsEntity

class SavedNewsFragment : Fragment() {
    var list: List<NewsEntity>? = null

    lateinit var savedNewsAdapter: SavedNewsAdapter

    //    var dbViewModel: NewsDbViewModel? = null
//    var newsDbViewModel: NewsDbViewModel? = null
    private val viewModel: NewsViewModel by activityViewModels {
        NewsViewModelFactory((activity?.application as UserApplication).db.dao)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = SavedNewsFragmentBinding.bind(
            inflater.inflate(
                R.layout.saved_news_fragment,
                container,
                false
            )
        )
        list = ArrayList()

        val email: String? =
            activity?.getSharedPreferences("News", Context.MODE_PRIVATE)?.getString("email", "")
        savedNewsAdapter = SavedNewsAdapter(container?.context, list)

        viewModel.fetchNewsWithUserSpecific(email = email ?: "")
            .observe(viewLifecycleOwner) { newsEntities: List<NewsEntity> ->
                if (newsEntities.isEmpty()) {
                    view.savedNoneTv.visibility = View.VISIBLE
                    view.savedErrorIv.visibility = View.VISIBLE
                } else {
                    list = newsEntities.reversed()
                    savedNewsAdapter.savedNews(list)
                }
                Log.i(TAG, "onCreateView: $list")
            }

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                viewModel.deleteNews(savedNewsAdapter.getPosition(viewHolder.adapterPosition))
                Toast.makeText(context, "Successfully Deleted", Toast.LENGTH_SHORT).show()
            }
        }).attachToRecyclerView(view.savedRecyclerView)

        view.savedRecyclerView.layoutManager = LinearLayoutManager(context)
        view.savedRecyclerView.adapter = savedNewsAdapter
        return view.root
    }

    companion object {
        private const val TAG = "SavedNewsFragment"
    }
}