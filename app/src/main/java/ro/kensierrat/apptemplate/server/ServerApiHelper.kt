package ro.kensierrat.apptemplate.server

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import ro.kensierrat.apptemplate.domain.GenericModel
import java.util.UUID

interface ServerApiHelper {
    @GET("/generics")
    suspend fun getGenerics(): List<GenericModel>
    @GET("/allGenerics")
    suspend fun getAllGenerics(): List<GenericModel>
    @GET("/generic/{id}")
    suspend fun getGeneric(@Path("id") id: Int): GenericModel
    @POST("/generic")
    suspend fun postGeneric(@Body generic: GenericModel): Response<GenericModel>
    @DELETE("/generic/{id}")
    suspend fun deleteGeneric(@Path("id") id: Int): Response<Unit>
    @PUT("/generic/{id}")
    suspend fun putGeneric(@Path("id") id: Int, @Body generic: GenericModel): Response<Unit>
}