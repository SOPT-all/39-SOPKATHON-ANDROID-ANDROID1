package com.android.sopkathon_android1

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * 앱 전체의 Hilt 시작점. 서버 주소와 Retrofit 제공 방식은
 * [com.android.sopkathon_android1.core.network.NetworkModule]에 정의한다.
 */
@HiltAndroidApp
class SopkathonApplication : Application()
