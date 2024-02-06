package codeflies.com.pulse.Intro


import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import codeflies.com.pulse.Intro.adapters.OnboardingAdapter
import codeflies.com.pulse.Intro.model.OnboardingDataModel
import codeflies.com.pulse.Login.LoginPage
import codeflies.com.pulse.MainActivity
import codeflies.com.pulse.R
import codeflies.com.pulse.databinding.ActivityIntroScreenBinding

class IntroScreenActivity : AppCompatActivity() {


    lateinit var binding: ActivityIntroScreenBinding
    var currentPage: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)


        var data = getOnboardingScreens()


        with(binding.viewPager) {
            adapter = OnboardingAdapter(data)
            binding.dotsIndicator.attachTo(this)
        }
        binding.textViewTitle.text = data.get(0).title
        binding.textViewDescription.text = data.get(0).description

        binding.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                // This method will be invoked when the current page is scrolled
            }

            override fun onPageSelected(position: Int) {
                if (position == 2) {
                    binding.next.text = "Let's Go!"
                } else {
                    binding.next.text = "Next"
                }
                currentPage = position
                binding.textViewTitle.text = data.get(position).title
                binding.textViewDescription.text = data.get(position).description
            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })


        binding.next.setOnClickListener {
            if (binding.next.text.toString().equals("Let's Go!")) {
                startActivity(Intent(this, LoginPage::class.java))
            } else {
                binding.viewPager.currentItem = currentPage + 1
            }
        }
    }


    private fun getOnboardingScreens(): List<OnboardingDataModel> {
        return listOf(
            OnboardingDataModel(
                R.drawable.img,
                "Mark your leave's very easily",
                "To mark leave easily, you can leverage modern technology and tools that streamline the process. Many systems use digital methods for leave tracking, making it convenient and efficient."
            ),
            OnboardingDataModel(
                R.drawable.img1,
                "Mark your leave's very easily",
                "To mark leave easily, you can leverage modern technology and tools that streamline the process. Many systems use digital methods for leave tracking, making it convenient and efficient."
            ),
            OnboardingDataModel(
                R.drawable.img2,
                "Mark your leave's very easily",
                "To mark leave easily, you can leverage modern technology and tools that streamline the process. Many systems use digital methods for leave tracking, making it convenient and efficient."
            )
        )
    }

}
