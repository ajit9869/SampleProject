package com.example.sampleproject

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.paging.*
import androidx.recyclerview.widget.RecyclerView
import com.example.sampleproject.adapter.UserAdapter
import com.example.sampleproject.api.ApiInterface
import com.example.sampleproject.api.NetworkConstants.NETWORK_PAGE_SIZE
import com.example.sampleproject.model.Data
import com.example.sampleproject.util.onSnackbar
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    val pagingAdapter = UserAdapter(UserAdapter.UserComparator)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recylerview)
        recyclerView.adapter = pagingAdapter

        lifecycleScope.launch {
            callApi()
                .collectLatest {
                    pagingAdapter.submitData(it)
                }
        }

      /*  pagingAdapter.addLoadStateListener { loadState ->
            if (loadState.source.refresh is LoadState.NotLoading && loadState.append.endOfPaginationReached ) {

                recyclerView.onSnackbar(getString(R.string.no_data))
            }
        }*/
    }


    private fun callApi(): Flow<PagingData<Data>>
    {
        val apiInterface = ApiInterface.create()

        val flow = Pager(
            config = PagingConfig(pageSize = NETWORK_PAGE_SIZE),
            pagingSourceFactory = {
                DataPagingSource(
                    service = apiInterface
                )
            }
        ) .flow
            .cachedIn(lifecycleScope)

        return flow

    }
}