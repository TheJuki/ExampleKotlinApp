package com.thejuki.example.api

import com.thejuki.example.json.*
import io.reactivex.Observable
import retrofit2.http.*

/**
 * Rest Service
 *
 * Retrofit Rest interface methods.
 *
 * @author **TheJuki** ([GitHub](https://github.com/TheJuki))
 * @version 1.0
 */
interface RestService {
    @FormUrlEncoded
    @POST("api/v1/login")
    fun postLogin(
            @Field("userId") userId: String,
            @Field("password") password: String
    ): Observable<UserJson>

    @FormUrlEncoded
    @POST("api/v1/deviceRegistration")
    fun postDeviceRegistration(@Header("Authorization") authorization: String,
                               @Field("deviceId") deviceToken: String,
                               @Field("deviceType") deviceType: String
    ): Observable<SuccessStatusJson>

    //region Contact API
    @GET("api/v1/contact/list/json")
    fun getContacts(@Header("Authorization") authorization: String,
                    @Query("term") term: String
    ): Observable<List<TableCellJson>>

    @GET("api/v1/contact/find/json")
    fun getContact(@Header("Authorization") authorization: String,
                   @Query("id") id: String
    ): Observable<ContactJson>

    @POST("api/v1/contact/save")
    fun postContact(@Header("Authorization") authorization: String,
                    @Body json: ContactJson
    ): Observable<ContactJson>

    @GET("api/v1/contact/lookup/json")
    fun getAutoCompleteContacts(@Header("Authorization") authorization: String,
                                @Query("term") term: String
    ): Observable<ArrayList<ContactLookupJson>>
    //endregion

    //region Note API
    @GET("api/v1/note/list/json")
    fun getNotes(@Header("Authorization") authorization: String,
                 @Query("pId") pId: String,
                 @Query("pType") pType: String,
                 @Query("type") type: String
    ): Observable<List<TableCellJson>>

    @GET("api/v1/note/find/json")
    fun getNote(@Header("Authorization") authorization: String,
                @Query("id") id: String
    ): Observable<NoteJson>

    @POST("api/v1/note/save")
    fun postNote(@Header("Authorization") authorization: String,
                 @Body json: NoteJson
    ): Observable<NoteJson>
    //endregion

    //region Team API
    @GET("api/v1/team/list/json")
    fun getTeams(@Header("Authorization") authorization: String
    ): Observable<ArrayList<ListItemJson>>

    //endregion

    //region Counts API
    @GET("api/v1/count/json")
    fun getCounts(@Header("Authorization") authorization: String
    ): Observable<CountsJson>

    //endregion
}
