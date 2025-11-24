package com.example.sclad_salo.repository

import android.util.Log
import com.example.sclad_salo.models.Sclad_operator


import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class OperatorsRepository @Inject constructor() {

    private val databaseReferenceOperators: DatabaseReference =
        FirebaseDatabase.getInstance().reference.child("user_persons")


    suspend fun createNewOperator(

        name: String,
        email: String,
        password: String,
        role: String,

        ): Sclad_operator {

        try {
            //Создаем пользователя в Firebase Authentication

            val authResult = Firebase.auth.createUserWithEmailAndPassword(
                email,
                password
            )
                .await()


            val user = authResult.user ?: throw Exception("Failed to create user, user is null")
            val userUid = user.uid

            //Создаем модель данных для нового оператора

            val operationCode = (1000..9999).random()
            // Генерируем random код 4-значный оператора

            val newOperator = Sclad_operator(

                uid = userUid,
                name_surname = name,
                email = email,
                password = password,
                operation_code = operationCode,
                added_product = 0,
                promoted_product = 0,
                role = role

            )

            //Сохраняем данные нового оператора в базе данных Firevase Realtime

            databaseReferenceOperators.child(userUid).setValue(newOperator).await()

            //Возвращаем полный обьект оператора в случае успеха

            return newOperator


        } catch (e: Exception) {

            Log.e("OperatorsRepository", "Failed to create new operator", e)
            //Выдаем исключение чтобы ViewModel могла обработать сообщение об ошибке
            throw e
        }

    }

    //Авторизация оператора

    suspend fun singOperator(email: String, password: String) {
        try {
            //.Await , чтобы сделать асинхронный вызов Firebase последдовательным

            Firebase.auth.signInWithEmailAndPassword(email, password).await()
        } catch (e: Exception) {

            Log.e("OperatorsRepository", "Failed to sign in operatos", e)
            //выдаем исключение чтобы Viewmodel обработаьть сообщение об ошибке
            throw e

        }


    }

    //Получение списка операторов из базы данных Firevase RealTime

    fun getOperatorsList(): Flow<List<Sclad_operator>> = callbackFlow {

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val operatorsList =
                    snapshot.children.mapNotNull { it.getValue(Sclad_operator::class.java) }
                trySend(operatorsList)

            }

            override fun onCancelled(error: DatabaseError) {

                Log.e(
                    "OperatorsRepository",
                    "Operator list listener cancelled",
                    error.toException()

                )

                close(error.toException())


            }

        }

        databaseReferenceOperators.addValueEventListener(listener)

        awaitClose {
            databaseReferenceOperators.removeEventListener(listener)
            Log.d("OperatorsRepository", "Stopped listening for operator list updates")
        }


    }


    //Удалить одного оператора из базы данных Firebase Realtime используя его UID


    suspend fun deleteOperator(Uid: String) {

        try {
            databaseReferenceOperators.child(Uid).removeValue().await()
            Log.d("OperatorsRepository", "Successfully deleted operator with UID: $Uid")
        } catch (e: Exception) {
            Log.e("OperatorsRepository", "Failed to delete operator with UID: $Uid", e)
            //Исключение в случаем возникновения ошибки позволяя ViewModel перехватить его


        }
    }


    //Взять данные одного оператора из базы данных Firebase Realtime

    fun getOperatorByUid(uid: String): Flow<Sclad_operator?> = callbackFlow {

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val operator = snapshot.getValue(Sclad_operator::class.java)
                trySend(operator)
            }


            override fun onCancelled(error: DatabaseError) {
                Log.e(
                    "OperatorsRepository", "Operator listener cancelled",
                    error.toException()
                )

            }

        }


    }

    //Взять текущего оператора из базы данных Firebase Realtime

    fun getCurrentOperator_uid(): String {
        val currentUser = Firebase.auth.currentUser
        return currentUser?.uid ?: ""
    }

    //Обновить количество добавленных продуктов оператором в базе данных Firebase Realtime

    suspend fun incrementOperatorAddedProductCount(uid: String) {
        val userRef = databaseReferenceOperators.child(uid)
        try {
            val snapshot = userRef.get().await()
            if (snapshot.exists()) {
                val currentCount =
                    snapshot.child("added_product").getValue(Int::class.java) ?: 0
                userRef.child("added_product").setValue(currentCount + 1).await()


            }

        } catch (e: Exception) {
            Log.e("OperatorsRepository", "Failed to update operator added product count", e)
            throw e
            print(e.message.toString())


        }

    }


}





