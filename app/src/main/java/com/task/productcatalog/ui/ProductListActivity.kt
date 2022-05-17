package com.task.productcatalog.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.task.productcatalog.R
import com.task.productcatalog.databinding.ActivityProductListBinding
import com.task.productcatalog.util.ErrorManager.showError
import com.task.productcatalog.ui.adapters.OffsetItemDecoration
import com.task.productcatalog.ui.adapters.OnItemClickListener
import com.task.productcatalog.ui.adapters.ProductsAdapter
import com.task.productcatalog.viewModels.ProductListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductListActivity : AppCompatActivity(), OnItemClickListener {

    private val viewModel: ProductListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityProductListBinding.inflate(layoutInflater)
        setContentView(binding.root)


        supportActionBar?.title = intent.getStringExtra(CATEGORY_NAME_EXTRA)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val adapter = ProductsAdapter(this)
        binding.rvProducts.layoutManager = LinearLayoutManager(this)
        binding.rvProducts.adapter = adapter
        binding.rvProducts.addItemDecoration(OffsetItemDecoration(R.dimen.item_product_offset))

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch { viewModel.productListState.collect { adapter.updateList(it) } }
                launch { viewModel.errorChannel.collect { showError(it) } }
            }
        }
    }

    override fun onItemClick(url: String, name: String) {
        val intent = ProductDetailsActivity.newIntent(this, url).apply {
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }
        startActivity(intent)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val CATEGORY_URL_EXTRA = "CATEGORY_URL_EXTRA"
        const val CATEGORY_NAME_EXTRA = "CATEGORY_URL_EXTRA"
        fun newIntent(context: Context, categoryUrl: String): Intent {
            return Intent(context, ProductListActivity::class.java).apply {
                putExtra(CATEGORY_URL_EXTRA, categoryUrl)
            }
        }
    }
}