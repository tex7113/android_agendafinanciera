package com.texdevs.agendafinanciera.presentation.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.texdevs.agendafinanciera.domain.CanAccessToApp
import com.texdevs.agendafinanciera.presentation.model.Artist
import com.texdevs.agendafinanciera.presentation.model.Player
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class HomeViewmodel : ViewModel() {

    private var canAccessToApp: CanAccessToApp = CanAccessToApp()
    private var db: FirebaseFirestore = Firebase.firestore
    private val database = Firebase.database

    private val _artist = MutableStateFlow<List<Artist>>(emptyList())
    val artist: StateFlow<List<Artist>> = _artist

    private val _player = MutableStateFlow<Player?>(null)
    val player:StateFlow<Player?> = _player

    private val _blockVersion : MutableStateFlow<Boolean> = MutableStateFlow(false)
    val blockVersion:StateFlow<Boolean> = _blockVersion

    init {
        checkUsVersion()
        getArtists()
        getPlayer()
    //        repeat(20){
    //            loadData()
    //        }
    }

    private fun checkUsVersion() {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO){
                canAccessToApp()
            }
            _blockVersion.value = !result
        }
    }

    private fun getPlayer() {
        viewModelScope.launch {
            collectPlayer().collect{
                val player = it.getValue(Player::class.java)
                _player.value = player
            }
        }
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

    private fun collectPlayer(): Flow<DataSnapshot> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                trySend(snapshot).isSuccess
            }

            override fun onCancelled(error: DatabaseError) {
                Log.i("tex log", "Error: ${error.message}")
                close(error.toException())
            }
        }
        val ref = database.reference.child("player")
        ref.addValueEventListener(listener)

        awaitClose{
            ref.removeEventListener(listener)
        }
    }

    fun onPlaySelected() {
        if (player.value != null) {
            val currentPlayer = _player.value?.copy(play = !player.value?.play!!)
            val ref = database.reference.child("player")
            ref.setValue(currentPlayer)
        }
    }

    fun onCancelSelected() {
        val ref = database.reference.child("player")
        ref.setValue(null)
    }

    fun addPlayer(artist: Artist) {
        val ref = database.reference.child("player")
        val player = Player(artist = artist, play = true)
        ref.setValue(player)
    }
}