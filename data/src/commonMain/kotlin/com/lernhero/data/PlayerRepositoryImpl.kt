package com.lernhero.data


import com.lernhero.domain.data.PlayerRepository
import com.lernhero.domain.preset.Character
import com.lernhero.domain.preset.Player
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseUser
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.firestore

class PlayerRepositoryImpl: PlayerRepository {
    override fun getUserId(): String? {
        return Firebase.auth.currentUser?.uid
    }

    override suspend fun createPlayer(
        user: FirebaseUser?,
        name: String,
        character: Character?,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        try {
            if (user != null) {
                val playerCollection = Firebase.firestore.collection(collectionPath = "player")
                val player = Player(
                    id = user.uid,
                    name = name,
                    email = user.email ?: "Unknown",
                    character = character,
                )

                val playerExists = playerCollection.document(user.uid).get().exists

                if (playerExists) {
                    onSuccess()
                } else {
                    playerCollection.document(user.uid).set(player)
                    playerCollection.document(user.uid)
                        .collection("privateData")
                        .document("role")
                        .set(mapOf("isAdmin" to false))
                    onSuccess()
                }
            } else {
                onFailure("User is not available.")
            }
        } catch (e: Exception) {
            onFailure("Error while creating a Customer: ${e.message}")
        }

    }
}