package com.example.mytube

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mytube.adapter.ImageLoadingompletedListener
import com.example.mytube.adapter.RVAdapter
import com.example.mytube.adapter.SpinnerItems
import com.example.mytube.repository.webservice.EndPoint
import com.example.mytube.repository.Video
import com.example.mytube.viewmodel.MyTubeViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject


class MainActivity : AppCompatActivity() {
    val linearLayoutManager by lazy { LinearLayoutManager(this, RecyclerView.VERTICAL, false) }

    //val viewModel by lazy{ ViewModelProvider(this).get(MyTubeViewModel::class.java)}
    @Inject
    lateinit var viewModel: MyTubeViewModel
    lateinit var rvAdapter: RVAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        (application as MyApplication).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        (toolbar as Toolbar).apply {
            applyTitleFont(toolbar_title)
            setSupportActionBar(this)
        }
        observeLiveData()
        if (viewModel.apiResponse.value == null && isOnline(this)) {
            viewModel.fetchData(viewModel.getNextPageEndPoint())
        } else {
            noInternetMsg()
        }
    }

    private fun observeLiveData() {
        viewModel.apply {
            apiResponse.observe(this@MainActivity, Observer {
                isLoading.postValue(false)
                if (it == null) {
                    when (viewModel.loadedPages) {
                        2 -> {
                            noMoreData();viewModel.loadedPages++;return@Observer
                        }
                        else -> {
                            loadedPages++;viewModel.fetchData(viewModel.getNextPageEndPoint());return@Observer
                        }
                    }
                }
                if (!(::rvAdapter.isInitialized) && it.posts.count() > 0) {
                    loadedPages++;initRecyclerView(
                        rv_home_data,
                        it.posts
                    );setupSpinner(spinner_sort)
                } else if (it.posts.count() > 0) {
                    loadedPages++;rvAdapter.addMoreData(it.posts)
                } else {
                    loadedPages++;viewModel.fetchData(viewModel.getNextPageEndPoint())
                }
            })
            isLoading.observe(this@MainActivity, Observer {
                progressBar.visibility = if (it) View.VISIBLE else View.GONE
            })
        }
    }

    private fun initRecyclerView(rvHomeData: RecyclerView, posts: ArrayList<Video>) {
        rvAdapter = RVAdapter(posts, object : ImageLoadingompletedListener {
            override fun onLoadComplete() {
                Handler().postDelayed({ progressBar.visibility = View.GONE }, 500)
            }
        })
        rvHomeData.apply {
            this.layoutManager = linearLayoutManager
            adapter = rvAdapter

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                val itemThreshold = 1
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    var lastVisibleItemPosition = 0
                    val totalItemCount: Int = linearLayoutManager.itemCount
                    lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition()

                    if (viewModel.isLoading.value != true && lastVisibleItemPosition + itemThreshold >= totalItemCount) {
                        if (!isOnline(this@MainActivity)) {
                            noInternetMsg();return
                        }
                        val endPoint = viewModel.getNextPageEndPoint()
                        if (endPoint == EndPoint.BLANK) {
                            noMoreData()
                            return
                        }
                        viewModel.fetchData(endPoint)
                    }
                }
            })
        }
    }

    private fun noMoreData() {
        if (!::rvAdapter.isInitialized)
            Toast.makeText(
                this@MainActivity,
                "No feed available for now, come back later.",
                Toast.LENGTH_SHORT
            ).show()
        else Toast.makeText(this@MainActivity, "That's all folks!", Toast.LENGTH_SHORT).show()
    }

    private fun noInternetMsg() {
        Toast.makeText(this, "Please check your Internet connection.", Toast.LENGTH_LONG).show()
    }

    private fun setupSpinner(sortSpinner: Spinner) {
        val arrayAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, getSpinnerItems())
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sortSpinner.apply {
            adapter = arrayAdapter
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    when (parent.getItemAtPosition(position).toString()) {
                        SpinnerItems.NEW.value -> {
                            rvAdapter.sortData(SpinnerItems.NEW)
                        }
                        SpinnerItems.OLD.value -> {
                            rvAdapter.sortData(SpinnerItems.OLD)
                        }
                        SpinnerItems.SHARED_DESC.value -> {
                            rvAdapter.sortData(SpinnerItems.SHARED_DESC)
                        }
                        SpinnerItems.SHARED_ASC.value -> {
                            rvAdapter.sortData(SpinnerItems.SHARED_ASC)
                        }
                        SpinnerItems.LIKE_DESC.value -> {
                            rvAdapter.sortData(SpinnerItems.LIKE_DESC)
                        }
                        SpinnerItems.LIKE_ASC.value -> {
                            rvAdapter.sortData(SpinnerItems.LIKE_ASC)
                        }
                        SpinnerItems.VIEWS_DESC.value -> {
                            rvAdapter.sortData(SpinnerItems.VIEWS_DESC)
                        }
                        SpinnerItems.VIEWS_ASC.value -> {
                            rvAdapter.sortData(SpinnerItems.VIEWS_ASC)
                        }
                    }
                }
            }
        }
    }

    private fun getSpinnerItems(): ArrayList<String> {
        return ArrayList<String>().apply {
            add(SpinnerItems.NEW.value);add(SpinnerItems.OLD.value);add(SpinnerItems.SHARED_DESC.value);add(
            SpinnerItems.SHARED_ASC.value
        );
            add(SpinnerItems.LIKE_DESC.value);add(SpinnerItems.LIKE_ASC.value);
            add(SpinnerItems.VIEWS_DESC.value);add(SpinnerItems.VIEWS_ASC.value);
        }
    }
}

private fun applyTitleFont(toolbarTitle: TextView) {
    toolbarTitle.apply {
        text = "Live Feed"
        typeface = ResourcesCompat.getFont(this.context, R.font.kristi)
        textSize = 35F
        setTextColor(resources.getColor(R.color.titleTextColor))
    }
}
