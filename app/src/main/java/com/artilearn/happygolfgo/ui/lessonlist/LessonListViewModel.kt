package com.artilearn.happygolfgo.ui.lessonlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.artilearn.happygolfgo.base.BaseViewModel

class LessonListViewModel : BaseViewModel() {

    private val _emptyLessonList: MutableLiveData<Boolean> = MutableLiveData()

    val emptyLessonList: LiveData<Boolean> get() = _emptyLessonList
}