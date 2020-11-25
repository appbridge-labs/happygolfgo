package com.artilearn.happygolfgo.ui.gamerank.mygroup

import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseFragment
import com.artilearn.happygolfgo.databinding.FragmentMygroupBinding
import com.artilearn.happygolfgo.ui.gamerank.GameRankAdapter
import com.artilearn.happygolfgo.ui.gamerank.GameRankTestModel
import com.artilearn.happygolfgo.ui.gamerank.GameRankViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MyGroupFragment : BaseFragment<FragmentMygroupBinding, GameRankViewModel>(R.layout.fragment_mygroup) {

    override val viewModel: GameRankViewModel by sharedViewModel()
    private lateinit var adapter: GameRankAdapter
    private var list = arrayListOf<GameRankTestModel>()

    override fun init() {

        initAdapter()
        setRandomData()
    }

    private fun initAdapter() {
        adapter = GameRankAdapter(list)

        binding.rvMyGroup.adapter = adapter
    }

    private fun setRandomData() {
        list = arrayListOf()
        val data1 = GameRankTestModel(1, "닉네임~~11", 7.7f)
        val data2 = GameRankTestModel(2, "닉네임~~22", 3.7f)
        val data3 = GameRankTestModel(3, "닉네임~~33", 6.7f)
        val data4 = GameRankTestModel(4, "닉네임~~44", 9.7f)
        val data5 = GameRankTestModel(5, "닉네임~55~", 10f)
        val data6 = GameRankTestModel(6, "닉네임~66~", 1f)
        val data7 = GameRankTestModel(7, "닉네임77~~", 7.2f)
        val data8 = GameRankTestModel(8, "닉네임~88~", 7.0f)
        val data9 = GameRankTestModel(9, "닉네임~~99", 5.7f)
        val data10 = GameRankTestModel(10, "닉네임~1010~", 4.2f)
        list.add(data1)
        list.add(data2)
        list.add(data3)
        list.add(data4)
        list.add(data5)
        list.add(data6)
        list.add(data7)
        list.add(data8)
        list.add(data9)
        list.add(data10)

        adapter.setItems(list)
    }
}