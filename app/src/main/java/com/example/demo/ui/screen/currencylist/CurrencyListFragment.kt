package com.example.demo.ui.screen.currencylist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.demo.CurrencyApplication
import com.example.demo.domain.model.CurrencyType
import javax.inject.Inject

class CurrencyListFragment : Fragment() {
    
    @Inject
    lateinit var currencyListViewModelFactory: CurrencyListViewModelFactory
    
    private val currencyType: CurrencyType by lazy {
        arguments?.getSerializable(ARG_CURRENCY_TYPE) as? CurrencyType ?: CurrencyType.ALL
    }
    
    private val currencyListViewModel: CurrencyListViewModel by viewModels { 
        currencyListViewModelFactory.create(currencyType)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (requireActivity().application as CurrencyApplication).appComponent.inject(this)
        
        return ComposeView(requireContext()).apply {
            // Dispose of the Composition when the view's LifecycleOwner is destroyed
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            
            setContent {
                MaterialTheme {
                    val uiState by currencyListViewModel.uiState.collectAsStateWithLifecycle()
                    
                    CurrencyListScreen(
                        uiState = uiState,
                        onAction = currencyListViewModel::onAction,
                        onNavigateAction = { parentFragmentManager.popBackStack() }
                    )
                }
            }
        }
    }

    companion object {
        private const val ARG_CURRENCY_TYPE = "currency_type"
        
        fun newInstance(currencyType: CurrencyType = CurrencyType.ALL): CurrencyListFragment {
            return CurrencyListFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_CURRENCY_TYPE, currencyType)
                }
            }
        }
    }
}
