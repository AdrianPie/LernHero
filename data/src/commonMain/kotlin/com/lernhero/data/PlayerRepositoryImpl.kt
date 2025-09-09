package com.lernhero.data

import com.lernhero.data.domain.PlayerRepository
import com.lernhero.shared.domain.Player
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseUser
import dev.gitlive.firebase.firestore.firestore

class PlayerRepositoryImpl: PlayerRepository {
    override suspend fun createPlayer(
        user: FirebaseUser?,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        try {
            if (user != null) {
                val playerCollection = Firebase.firestore.collection(collectionPath = "player")
                val player = Player(
                    id = user.uid,
                    name = user.displayName?.split(" ")?.firstOrNull() ?: "Unknown",
                    email = user.email ?: "Unknown",
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