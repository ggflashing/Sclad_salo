package com.example.sclad_salo.repository

import android.net.Uri
import android.util.Log
import com.example.sclad_salo.models.UnitModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class UnitRepository @Inject constructor() {

    private val databaseReferenceUnits: DatabaseReference =
        FirebaseDatabase.getInstance().reference.child("units")
    //Получение списка единиц товаров:

    fun getUnitsList(): Flow<List<UnitModel>> = callbackFlow {
        //Создаем слушателя для отслеживания изменений в базе данных товаров:
        val valueEventListener = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val tempList = snapshot.children.mapNotNull { dataSnapshot ->

                    dataSnapshot.getValue(UnitModel::class.java)
                }

                //Предоставляем последний список товаров -потоку,
                //trySend - это неблокируемый путь сохранения списка в свободном потоке

                trySend(tempList).isSuccess
            }





            override fun onCancelled(error: DatabaseError) {

                Log.e("UnitRepository","Failed to get unit list: ${error.message}")
                //Закрываем потом с исключением которое может быть перехвачено сборщиком
                close(error.toException())


            }


        }

        //Приклепляем слушателя для отслеживания изменений в базе данных товаров:
        databaseReferenceUnits.addValueEventListener(valueEventListener)

            //await будет вызван при отмене потока
        //Это идеальное место для отмены регистрации слушателя.

        awaitClose {
            databaseReferenceUnits.removeEventListener(valueEventListener)
            Log.e("Unitrepository","Stopped listening for unit updates.")
        }


    }

    //Добавить новый товар:

    suspend fun addNewUnit_rep(unitModel: UnitModel) {

        try {
            // .push() создает уникальный индетификатор .await() ожидает заверщения операции.
            databaseReferenceUnits.push().setValue(unitModel).await()
        } catch (e: Exception) {
            Log.e("UnitRepository", "Failed to add new unot", e)

            throw e // выдаем исключение чтобы сообщить ViewModel об ошибке добавления товара
            print(e.message.toString())

        }
    }

        //Функция для загрузки изображения товара и получения его URL


        suspend fun  uploadUnitImage(imageUri : Uri, unitName:String):String {
            val storageRaf = FirebaseStorage.getInstance().reference
                .child("units_photo/${System.currentTimeMillis()}/$unitName.png")


            //Загружаем изображения в хранилище Firebase Storage и дожидаемся заверщения загрузки
            val uploadTask = storageRaf.putFile(imageUri).await()


            //Получаем URL- адрес для загрузки изображения и дожидаемся его
            return uploadTask.storage.downloadUrl.await().toString()

        }


}