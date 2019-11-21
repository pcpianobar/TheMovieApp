package net.purpleocean.themovieapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_main.*
import net.purpleocean.themovieapp.ui.MovieFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        if (savedInstanceState == null) {
            changeFragment(MovieFragment()) // (1)
        }
    }

    private fun changeFragment(f: Fragment, cleanStack: Boolean = false) {
        val ft = supportFragmentManager.beginTransaction() // (2) 프레그먼트 관리자를 통한 제어
        ft.replace(R.id.base_content, f) // (3) 프레그먼트의 변경
        ft.addToBackStack(null) // (4) 백스택에 넣기
        ft.commit() // (5) 최종 프레그먼트의 적용
    }
}
