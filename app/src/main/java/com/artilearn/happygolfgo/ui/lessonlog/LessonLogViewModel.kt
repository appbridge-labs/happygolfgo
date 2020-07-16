package com.artilearn.happygolfgo.ui.lessonlog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.artilearn.happygolfgo.base.BaseViewModel

class LessonLogViewModel : BaseViewModel() {

    private val _emptyLessonLog: MutableLiveData<Boolean> = MutableLiveData()

    val emptyLessonLog: LiveData<Boolean> get() = _emptyLessonLog
}