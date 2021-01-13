package khusainov.farrukh.articlestats.model

import com.google.gson.annotations.SerializedName

data class Article(
    @SerializedName("title")
    val title: String,

    @SerializedName("counts")
    val stats: Stats
)