package khusainov.farrukh.communityapp.data

/**
 *Created by FarrukhKhusainov on 4/9/21 12:07 PM
 **/
//Wrapper tool for exception handling and etc.
sealed class DataWrapper<T : Any> {
	data class Success<T : Any>(val data: T) : DataWrapper<T>()
	data class Error<T : Any>(val message: String) : DataWrapper<T>()
}