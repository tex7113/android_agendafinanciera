package com.texdevs.agendafinanciera.presentation.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.texdevs.agendafinanciera.presentation.model.Artist
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class HomeViewmodel : ViewModel() {

    private var db: FirebaseFirestore = Firebase.firestore

    private val _artist = MutableStateFlow<List<Artist>>(emptyList())
    val artist: StateFlow<List<Artist>> = _artist

    init {
//        repeat(20){
//            loadData()
//        }

        getArtists()
    }

//    private fun loadData() {
//    val random = (1..10000).random()
//    val artist = Artist(name = "Random $random", description = "Description numero $random", image = "https://imgs.search.brave.com/SS4rL5ZInWo1T4eU6ifM--AghbYyQ00oe_uBJ4yxYuA/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly9pLnBp/bmltZy5jb20vb3Jp/Z2luYWxzL2RmL2U5/L2UxL2RmZTllMTk1/ODhlNGU1OTI0YTJk/Mzk5MWI3MzM0MzE0/LnBuZw")
//    db.collection("artists").add(artist)
//    }

    private fun getArtists() {
        viewModelScope.launch {
            val result: List<Artist> = withContext(Dispatchers.IO) {
                getAllArtists()
            }
            _artist.value = result
        }
    }

    suspend fun getAllArtists(): List<Artist> {
        return try {
            db.collection("artists")
                .get()
                .await()
                .documents
                .mapNotNull { snapshot ->
                    snapshot.toObject(Artist::class.java)
                }
        } catch (e: Exception) {
            Log.i("tex",e.toString())
            emptyList()
        }
    }
}