package com.task.productcatalog.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.task.productcatalog.ui.adapters.CategoriesAdapter
import com.task.productcatalog.ui.adapters.OnItemClickListener
import com.task.productcatalog.databinding.ActivityCategoryListBinding
import com.task.productcatalog.viewModels.CategoryListViewModel
import com.task.productcatalog.util.ErrorManager.showError
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*

@AndroidEntryPoint
class CategoryListActivity : AppCompatActivity(), OnItemClickListener {

    private val viewModel: CategoryListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityCategoryListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = CategoriesAdapter(this)
        binding.rvCategories.layoutManager = LinearLayoutManager(this)
        binding.rvCategories.adapter = adapter

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch { viewModel.categoryListState.collect { adapter.updateList(it) } }
                launch { viewModel.errorChannel.collect { showError(it) } }
            }
        }
    }

    override fun onItemClick(url: String, name: String) {
        val intent = ProductListActivity.newIntent(this, url).apply {
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }
        startActivity(intent)

    }
}