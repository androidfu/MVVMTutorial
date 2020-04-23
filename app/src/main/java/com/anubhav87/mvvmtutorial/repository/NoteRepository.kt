package com.anubhav87.mvvmtutorial.repository

import android.arch.lifecycle.LiveData
import android.os.AsyncTask
import com.anubhav87.mvvmtutorial.db.dao.NoteDao
import com.anubhav87.mvvmtutorial.db.entity.Note

class NoteRepository(private val noteDao: NoteDao) {

    private val allNotes: LiveData<List<Note>> = noteDao.getAllNotes()

    fun insert(note: Note) {
        DbAsyncTask(object : ResponseListener {
            override fun onFinished(result: Any?) {
                // You can process the result from onPostExecute in your listener
                val noteId = result as Long
            }
        }) {
            run {
                noteDao.insert(note)
            }
        }.execute()
    }

    fun deleteAllNotes() {
        DbAsyncTask {
            run {
                noteDao.deleteAllTickles()
            }
        }.execute()
    }

    fun getAllNotes(): LiveData<List<Note>> {
        return allNotes
    }
    
    private class DbAsyncTask(val listener: ResponseListener? = null, val block: () -> Any?) :
        AsyncTask<Any, Any, Any>() {

        override fun doInBackground(vararg params: Any?): Any? {
            return block()
        }

        override fun onPostExecute(result: Any?) {
            listener?.onFinished(result)
        }
    }

    interface ResponseListener {
        fun onFinished(result: Any?)
    }

}
