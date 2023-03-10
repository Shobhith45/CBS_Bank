package com.cbsindia.cbs.di

import com.cbsindia.cbs.data.remote.MoneyTransferApi
import com.cbsindia.cbs.data.remote.LoginApi
import com.cbsindia.cbs.data.remote.BalanceApi
import com.cbsindia.cbs.data.remote.DepositApi
import com.cbsindia.cbs.data.remote.TransactionApi
import com.cbsindia.cbs.data.remote.WithdrawApi
import com.cbsindia.cbs.data.repository.base.PrefRepository
import com.cbsindia.cbs.data.remote.CreateAccountApi
import com.cbsindia.cbs.util.Constant
import com.cbsindia.cbs.util.Constant.KEY_AUTHORIZATION_HEADER
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {
    factory { provideGson() }
    single { provideOkkHttp(get()) }
    single { provideRetrofit(get(),get()) }
    factory { provideLoginApi(get()) }
    factory { provideCreateAccountApi(get()) }
    factory { provideBalanceApi(get()) }
    factory { provideTransactionApi(get()) }
    factory { provideMoneyTransferApi(get()) }
    factory { provideDepositApi(get()) }
    factory { provideWithdrawApi(get()) }
}

private fun provideTransactionApi(retrofit:Retrofit):TransactionApi =
    retrofit.create(TransactionApi::class.java)

private fun provideBalanceApi(retrofit: Retrofit): BalanceApi =
    retrofit.create(BalanceApi::class.java)

private fun provideGson(): GsonConverterFactory =
    GsonConverterFactory.create()

private fun provideOkkHttp(prefRepository: PrefRepository): OkHttpClient =
    OkHttpClient
        .Builder()
        .addInterceptor { chain ->
            chain.proceed(
                chain.request().newBuilder().also {
                    it.addHeader(
                        KEY_AUTHORIZATION_HEADER,
                        prefRepository.getUserToken().toString()
                    )
                }.build()
            )
        }
        .build()

private fun provideRetrofit(
    gsonConverterFactory: GsonConverterFactory,
    client: OkHttpClient
): Retrofit =
    Retrofit
        .Builder()
        .addConverterFactory(gsonConverterFactory)
        .client(client)
        .baseUrl(Constant.BASE_URL).build()

private fun provideDepositApi(retrofit: Retrofit): DepositApi =
    retrofit.create(DepositApi::class.java)

private fun provideMoneyTransferApi(retrofit: Retrofit): MoneyTransferApi =
        retrofit.create(MoneyTransferApi::class.java)

private fun provideLoginApi(retrofit: Retrofit): LoginApi =
    retrofit.create(LoginApi::class.java)

private fun provideCreateAccountApi(retrofit: Retrofit): CreateAccountApi =
    retrofit.create(CreateAccountApi::class.java)

private fun provideWithdrawApi(retrofit: Retrofit): WithdrawApi =
    retrofit.create(WithdrawApi::class.java)