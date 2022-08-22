package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.adapter.MainRecyclerAdapter
import com.udacity.asteroidradar.databinding.FragmentMainBinding

class MainFragment : Fragment() {


    private val viewModel: MainViewModel by lazy {
        val activity = requireNotNull(this.activity){}
        ViewModelProvider(this, MainViewModelFactory(activity.application)).get(MainViewModel::class.java)
    }
    private lateinit var binding: FragmentMainBinding

    private lateinit var adapter: MainRecyclerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        setHasOptionsMenu(true)

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Create instance of MainRecyclerAdapter and on CLick  navigate to destail screen
         adapter = MainRecyclerAdapter(MainRecyclerAdapter.OnClickListener{
            findNavController().navigate(MainFragmentDirections.actionShowDetail(it))}
        )

        //bind MainRecycler adapter
        binding.asteroidRecycler.adapter = adapter

        viewModel.asteroids.observe(viewLifecycleOwner,{adapter.submitList(it)})

        //change visibility of  Progressbar based on Network API status
        viewModel.status.observe(viewLifecycleOwner,{status ->
            when (status) {
                MainViewModel.NasaApiStatus.LOADING -> {
                    binding.apply { statusLoadingWheel.visibility = View.VISIBLE }
                }
                MainViewModel.NasaApiStatus.DONE -> {
                    binding.statusLoadingWheel.visibility = View.GONE
                }
                else -> {
                    binding.statusLoadingWheel.visibility = View.GONE
                }
            }

        })




    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.show_week_asteroids -> {
                viewModel.getWeekAsteroids().observe(viewLifecycleOwner, {
                    adapter.submitList(it)
                })
            }
            R.id.show_today_asteroids -> {
                viewModel.getTodayAsteroids().observe(viewLifecycleOwner, {
                    adapter.submitList(it)
                })
            }
            R.id.show_all_asteroids -> {
                viewModel.asteroids.observe(viewLifecycleOwner, {
                    adapter.submitList(it)
                })
            }
        }
        return true
    }
}
