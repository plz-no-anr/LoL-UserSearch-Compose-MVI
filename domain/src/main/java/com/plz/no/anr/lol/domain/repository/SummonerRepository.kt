package com.plz.no.anr.lol.domain.repository

import com.plz.no.anr.lol.domain.model.Spectator
import com.plz.no.anr.lol.domain.model.Summoner
import kotlinx.coroutines.flow.Flow

interface SummonerRepository {

    fun requestSummoner(name: String): Flow<Result<Summoner>>

    fun getSummoner(name: String): Flow<Result<Summoner>>

    fun getSummonerList(): Flow<Result<List<Summoner>>>

    fun requestSpectator(summonerId: String): Flow<Result<Spectator>>

    fun insertSummoner(summoner: Summoner): Flow<Result<Unit>>

    fun updateSummoner(summoner: Summoner): Flow<Result<Unit>>

    fun deleteSummoner(name: String): Flow<Result<Unit>>

    fun deleteSummonerAll(): Flow<Result<Unit>>

}