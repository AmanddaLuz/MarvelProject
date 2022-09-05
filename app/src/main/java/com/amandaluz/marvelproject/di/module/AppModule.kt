package com.amandaluz.marvelproject.di.module

import com.amandaluz.marvelproject.data.db.repository.DatabaseRepository
import com.amandaluz.marvelproject.data.db.repository.DatabaseRepositoryImpl
import com.amandaluz.marvelproject.data.repository.CharacterRepository
import com.amandaluz.marvelproject.data.repository.CharactersRepositoryImpl
import com.amandaluz.marvelproject.data.repository.categoryrepository.CategoryRepository
import com.amandaluz.marvelproject.data.repository.categoryrepository.CategoryRepositoryImpl
import com.amandaluz.marvelproject.data.repository.loginrepository.LoginRepository
import com.amandaluz.marvelproject.data.repository.loginrepository.LoginRepositoryImpl
import com.amandaluz.marvelproject.view.login.repository.RegisterRepository
import com.amandaluz.marvelproject.view.login.repository.RegisterRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Singleton
    @Binds
    abstract fun bindCategoryRepository(
        categoryRepository: CategoryRepositoryImpl
    ): CategoryRepository

    @Singleton
    @Binds
    abstract fun bindCharacterRepository(
        characterRepository: CharactersRepositoryImpl
    ): CharacterRepository

    @Singleton
    @Binds
    abstract fun bindDataBaseRepository(
        databaseRepository: DatabaseRepositoryImpl
    ): DatabaseRepository

    @Singleton
    @Binds
    abstract fun bindRegisterRepository(
        registerRepository: RegisterRepositoryImpl
    ): RegisterRepository

    @Singleton
    @Binds
    abstract fun bindLoginRepository(
        loginRepository: LoginRepositoryImpl
    ): LoginRepository
}