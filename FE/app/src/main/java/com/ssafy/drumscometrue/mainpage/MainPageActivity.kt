package com.ssafy.drumscometrue.mainpage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ssafy.drumscometrue.R
import com.ssafy.drumscometrue.freePlay.FreePlayActivity
import com.ssafy.drumscometrue.kpop.KpopListActivity
import com.ssafy.drumscometrue.practice.PracticeMainActivity
import com.ssafy.drumscometrue.tutorial.TutorialActivity
import com.ssafy.drumscometrue.user.login.LoginActivity

class MainPageActivity : AppCompatActivity() {
    private lateinit var viewPager: ViewPager2
    private lateinit var logoutBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)

        viewPager = findViewById(R.id.view_pager)
        logoutBtn = findViewById(R.id.logout_btn)

        logoutBtn.setOnClickListener {
            // SharedPreferences 초기화
            val sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE)
            sharedPreferences.edit().clear().apply()

            // 로그인 페이지로 이동
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)

            // MainPageActivity 종료 (선택적)
            finish()
        }

        initViews()
    }

    private fun initViews() {
        val pageDataList = listOf(
            PageData(
                R.drawable.drum_img,
                "자유연주",
//                "자유연주로 자유롭게 연주하세요",
                1
            ),
            PageData(
                R.drawable.pad_img,
                "연습하기",
//                "연습하기로 연습하세요",
                2
            ),
            PageData(
                R.drawable.tutorial_img,
                "튜토리얼",
//                "튜토리얼을 통해 튜토리얼을 하세요",
                3
            )
        )

        val adapter = PageAdapter(pageDataList)
        viewPager.adapter = adapter

        // 여러 가지 애니메이션 효과를 적용하기 위해 CompositePageTransformer 사용
        val compositePageTransformer = CompositePageTransformer()
        compositePageTransformer.addTransformer(ScaleInTransformer())
        compositePageTransformer.addTransformer(MarginPageTransformer(40))

        viewPager.orientation = ViewPager2.ORIENTATION_VERTICAL
        viewPager.setPageTransformer(compositePageTransformer)
        // 카드 클릭 이벤트 처리
        adapter.setOnItemClickListener { position ->
            // 각 카드의 position에 따라서 Activity를 시작합니다.
            when (position) {
                0 -> {
                    val intent = Intent(this, FreePlayActivity::class.java)
                    startActivity(intent)
                }

                1 -> {
                    val intent = Intent(this, PracticeMainActivity::class.java)
                    startActivity(intent)
                }

                2 -> {
                    val intent = Intent(this, TutorialActivity::class.java)
                    startActivity(intent)
                }

                // 추가적인카드 항목이 있다면 여기에 계속 추가할 수 있습니다.
            }
        }
    }
}