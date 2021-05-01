package khusainov.farrukh.communityapp.ui.activities

import android.content.Intent
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.view.WindowManager
import khusainov.farrukh.communityapp.R

class SplashActivity : AppCompatActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setTheme(R.style.Theme_CommunityApp)
		setContentView(R.layout.activity_splash)

		window.setFlags(
			WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
			WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
		)

		if (savedInstanceState == null) {
			Handler(Looper.getMainLooper()).postDelayed({
				startActivity(Intent(this, HomeActivity::class.java))
				finish()
			}, 4000)
		}
	}
}