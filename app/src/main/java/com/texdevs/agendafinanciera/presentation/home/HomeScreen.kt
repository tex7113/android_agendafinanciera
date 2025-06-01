package com.texdevs.agendafinanciera.presentation.home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.google.firebase.firestore.FirebaseFirestore
import com.texdevs.agendafinanciera.presentation.model.Artist

@Composable
fun HomeScreen(viewmodel:HomeViewmodel = HomeViewmodel()) {

    val artists: State<List<Artist>> = viewmodel.artist.collectAsState()
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Popular artist",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )


        LazyRow {
            items(artists.value) {
                ArtistItem(it)
            }
        }
    }
}

@Composable
fun ArtistItem(artist: Artist) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        AsyncImage(
            modifier = Modifier.size(200.dp),
            model = artist.image,
            contentDescription = "Artists image"
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = artist.name.orEmpty(),
            color = Color.White,
            fontSize = 30.sp
        )
    }
}

@Preview
@Composable
fun PreviewArtistItem() {
    val artist = Artist(
        name = "Pepe",
        description = "El mejor",
        image = "",
        //songs = emptyList()
    )
    ArtistItem(artist = artist)
}


//fun createArtist(db: FirebaseFirestore) {
//    val random = (1..10000).random()
//    val artist = Artists(name = "Random $random", random)
//    db.collection("artists").add(artist)
//        .addOnSuccessListener {
//            Log.i("Tex", "Success")
//        }
//        .addOnFailureListener {
//            Log.i("Tex", "Failed")
//        }
//        .addOnCompleteListener {
//            Log.i("Tex", "Completed")
//        }
//}