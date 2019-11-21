package net.purpleocean.themovieapp.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import net.purpleocean.themovieapp.ui.adapter.AdapterType
import net.purpleocean.themovieapp.ui.adapter.ViewType

/**
 * 각각의 영화 아이템을 위한 데이터 클래스 정의
 * @property vote_count 투표수
 * @property vote_average 투표 평균 점수
 * @property title 영화명
 * @property release_date 출시일
 * @property poster_path 포스터의 위치
 * @property overview 영화 설명
 */
@Parcelize
data class MovieItem(
    val vote_count: Int,
    val vote_average: Float,
    val title: String,
    val release_date: String,
    val poster_path: String,
    val overview: String?
) : ViewType, Parcelable {
    override fun getViewType() = AdapterType.MOVIE
}

/**
 * 영화 목록을 위한 데이터 클래스 정의
 * @constructor 페이지와 결과 영화 목록 List 설정
 * @property page 페이지를 나타내며 한페이지당 20개의 영화목록
 * @property results 한 페이지의 영화목록20개를 List에 구성
 */
@Parcelize
data class MovieList(
    var page: Int?,
    val results: List<MovieItem>) : Parcelable {
}