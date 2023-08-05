package com.example.mirestaurante.infraestructure.repository

import com.example.mirestaurante.infraestructure.database.AppDataBase
import com.example.mirestaurante.domain.model.User
import com.example.mirestaurante.domain.repository.UserRepository
import com.example.mirestaurante.infraestructure.remote.user.UserResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class UserRepositoryRoom(database: AppDataBase) : UserRepository {
    private val userDao = database.getUserDao()

    override suspend fun register(user: User): Response<UserResponse>? {
        withContext(Dispatchers.IO) {
            userDao.create(user)
        }

        return null
    }

    override suspend fun checkIfIsInDB(email: String): Int {
        return withContext(Dispatchers.IO) {
            userDao.checkIfUserIsInDB(email)
        }
    }

    override suspend fun authenticate(email: String, password: String): Int {
        return withContext(Dispatchers.IO) {
            userDao.authenticate(email, password)
        }
    }
}