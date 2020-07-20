package com.artilearn.happygolfgo.deprecated

//import android.util.Log
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import com.artilearn.happygolfgo.base.BaseViewModel
//import com.artilearn.happygolfgo.data.Ticket
//import com.artilearn.happygolfgo.data.home.source.HomeRepository
//import io.reactivex.android.schedulers.AndroidSchedulers
//import io.reactivex.schedulers.Schedulers
//
//class HomeFragmentViewModel(
//    private val homeRepository: HomeRepository
//) : BaseViewModel() {
//    private val TAG = javaClass.simpleName
//
//    private val _emptyTicket: MutableLiveData<Boolean> = MutableLiveData()
//    private val _tickets: MutableLiveData<ArrayList<Ticket>> = MutableLiveData()
//
//    val emptyTicket: LiveData<Boolean> get() = _emptyTicket
//    val tickets: LiveData<ArrayList<Ticket>> get() = _tickets
//
//    init {
//        requestTicket()
//    }
//
//    fun requestTicket() {
//        compositeDisposable.add(
//            homeRepository.requestHomeTicket()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .doOnSubscribe { showLoading() }
//                .doAfterTerminate { hideLoading() }
//                .subscribe({
//                    val body = it.body()
//                    if (it.isSuccessful) {
//                        if (body != null && body.data.tickets.isNotEmpty()) {
//                            hideEmptyTicket()
//                            _tickets.value = body.data.tickets as ArrayList<Ticket>
//                        } else {
//                            showEmptyTicket()
//                        }
//                    } else {
//                        _serverError.value = it.message()
//                    }
//                }, {
//                    Log.d(TAG, "fail: $it\n${it.message}")
//                    when (it.message) {
//                        "Network Error" -> {
//                            _networkFail.value = Unit
//                        }
//                        else -> {
//                            _serverError.value = it.message
//                        }
//                    }
//                })
//        )
//    }
//
//    private fun showEmptyTicket() {
//        _emptyTicket.value = true
//    }
//
//    private fun hideEmptyTicket() {
//        _emptyTicket.value = false
//    }
//}