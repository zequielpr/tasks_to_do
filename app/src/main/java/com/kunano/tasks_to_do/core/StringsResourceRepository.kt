package com.kunano.tasks_to_do.core

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StringsResourceRepository @Inject constructor(@ApplicationContext private val context: Context) {
    fun getStringResource(id: Int): String{
       return context.resources.getString(id)
    }
}