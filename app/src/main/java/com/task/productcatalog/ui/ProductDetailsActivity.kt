package com.task.productcatalog.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.task.productcatalog.R
import com.task.productcatalog.databinding.ActivityProductDetailsBinding
import com.task.productcatalog.util.ErrorManager.showError
import com.task.productcatalog.util.formatHtmlWithoutLinks
import com.task.productcatalog.util.getLargeImageUrl
import com.task.productcatalog.util.loadImage
import com.task.productcatalog.viewModels.ProductDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ProductDetailsActivity : AppCompatActivity() {

    private val viewModel: ProductDetailsViewModel by viewModels()

    private lateinit var binding: ActivityProductDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch { viewModel.productState.collect { updateState(it) } }
                launch { viewModel.errorChannel.collect { showError(it) } }
            }
        }
    }

    private fun updateState(state: ProductState) {
        if (state is ProductState.ProductDetailsUi) {
            val imageUrl = getLargeImageUrl(state.image)
            loadImage(this, imageUrl, binding.ivProductLargeImage)

            binding.tvProductPrice.text = state.price.toString()
            binding.tvProductName.text = state.name
            binding.tvProductDescription.formatHtmlWithoutLinks(state.description)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            R.id.homeEvent -> {
                navigateHome()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun navigateHome() {
        startActivity(Intent(this, CategoryListActivity::class.java))
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.address_list, menu);
        return true;
    }

    companion object {
        const val PRODUCT_URL_EXTRA = "PRODUCT_URL_EXTRA"
        fun newIntent(context: Context, productUrl: String): Intent {
            return Intent(context, ProductDetailsActivity::class.java).apply {
                putExtra(PRODUCT_URL_EXTRA, productUrl)
            }
        }
    }
}