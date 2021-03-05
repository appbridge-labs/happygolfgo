package com.artilearn.happygolfgo.data.login.source.remote

import com.artilearn.happygolfgo.api.ApiInterface
import com.artilearn.happygolfgo.data.login.LoginDataModel
import com.artilearn.happygolfgo.data.login.LoginResponse
import com.artilearn.happygolfgo.modules.apiModule
import com.google.firebase.iid.FirebaseInstanceId
import io.reactivex.Single
import retrofit2.Retrofit

class LoginRemoteDataSourceImpl(
    private val apiInterface: ApiInterface,
    private val firebase: FirebaseInstanceId
) : LoginRemoteDataSource {

    override fun remoteLogin(
        data: HashMap<String, LoginDataModel>
    ): Single<LoginResponse> {
        return apiInterface.login(data)
    }

    override fun getFcmToken(): Single<String?> {
        return Single.create { emitter ->
            firebase.instanceId
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        task.result?.let {
                            emitter.onSuccess(it.token)
                        } ?: emitter.onSuccess("")
                    } else {
                        task.exception?.let { t ->
                            emitter.onError(t)
                        } ?: emitter.onError(Throwable(IllegalStateException("UnKnown Error")))
                    }
                }
        }
    }

}