package com.plznoanr.domain.repository

import com.plznoanr.domain.model.Profile
import com.plznoanr.domain.model.Search
import com.plznoanr.domain.model.Spectator
import com.plznoanr.domain.model.Summoner
import kotlinx.coroutines.flow.Flow

interface AppRepository {
    var apiKey: String?

    var isInit: Boolean
    fun getSearchList(): Flow<Result<List<Search>>>
    fun insertSearch(search: Search): Flow<Result<Unit>>
    fun deleteSearch(sName: String): Flow<Result<Unit>>
    fun deleteSearchAll(): Flow<Result<Unit>>
    fun requestSummoner(name: String): Flow<Result<Summoner>>
    fun readSummonerList(): Flow<Result<List<Summoner>>>
    fun refreshSummonerList(): Flow<Result<List<Summoner>>>
    fun requestSpectator(name: String): Flow<Result<Spectator>>
    fun insertSummoner(summoner: Summoner): Flow<Result<Unit>>
    fun deleteSummoner(name: String): Flow<Result<Unit>>
    fun deleteSummonerAll(): Flow<Result<Unit>>
    fun getProfile(): Flow<Result<Profile?>>
    fun insertProfile(profile: Profile): Flow<Result<Unit>>
    fun deleteProfile(): Flow<Result<Unit>>
    fun initLocalJson(): Flow<Result<Boolean>>

}