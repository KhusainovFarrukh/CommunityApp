package khusainov.farrukh.articlestats.api

import khusainov.farrukh.articlestats.utils.Constants.Companion.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val communityApi: CommunityApi = retrofit.create(CommunityApi::class.java)
}