package com.example.sampleproject

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.sampleproject.api.ApiInterface
import com.example.sampleproject.model.Data
import com.google.gson.JsonSyntaxException
import retrofit2.HttpException
import java.net.UnknownHostException

class DataPagingSource(
    private val service: ApiInterface
) : PagingSource<Int, Data>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Data> {
        val position = params.key ?: 0
        return try {

            val response = service.getUsers( key = position+1)

            if (response.body()?.data?.isNotEmpty()==true) {
                val repos = response.body()?.data
                val totalPages = response.body()?.total_pages

                LoadResult.Page(
                    data = repos ?: listOf(),
                    prevKey = if (position == 0) null else position - 1,
                    nextKey = if (repos?.isNullOrEmpty() == true) null else position + 1
                )

            } else LoadResult.Page(
                data = listOf(),
                prevKey = if (position == 0) null else position - 1,
                nextKey = null
            )


        } catch (exception: JsonSyntaxException) {
            LoadResult.Page(
                listOf(),
                prevKey = if (position == 0) null else position - 1,
                nextKey = null
            )

        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        } catch (exception: UnknownHostException) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Data>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}