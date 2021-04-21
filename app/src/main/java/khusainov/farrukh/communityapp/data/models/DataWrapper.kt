package khusainov.farrukh.communityapp.data.models

/**
 *Created by FarrukhKhusainov on 4/9/21 12:07 PM
 **/
sealed class DataWrapper<T: Any> {
    data class Success<T: Any>(val data: T) : DataWrapper<T>()
    data class Error<T: Any>(val message: String) : DataWrapper<T>()
}