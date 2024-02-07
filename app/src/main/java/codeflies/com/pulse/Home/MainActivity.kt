package codeflies.com.pulse.Home

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import codeflies.com.pulse.Home.Fragments.Attendence
import codeflies.com.pulse.Home.Fragments.Candidates.Candidates
import codeflies.com.pulse.Home.Fragments.Holiday.Holidays
import codeflies.com.pulse.Home.Fragments.Leaves.Home
import codeflies.com.pulse.Profiles.Profile
import codeflies.com.pulse.R
import codeflies.com.pulse.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.rootLayout)

        changeMenu(1)

        binding.lyHome.setOnClickListener(
            View.OnClickListener {
                binding.title.text="Leaves"
                changeMenu(1)
            }
        )
        binding.lyFav.setOnClickListener(
            View.OnClickListener {
                binding.title.text="Candidates"
                changeMenu(2)
            }
        )
        binding.lyCart.setOnClickListener(
            View.OnClickListener {
                binding.title.text="Holidays"
                changeMenu(3)
            }
        )
        binding.lyMenu.setOnClickListener(
            View.OnClickListener {
                binding.title.text="Attendance"
                changeMenu(4)
            }
        )

         binding.profile.setOnClickListener {
             startActivity(Intent(this@MainActivity, Profile::class.java))
         }


    }

    fun changeMenu(i: Int) {

        if (i == 1) {
            changeFragment(Home())
            binding.ivHome.setColorFilter(
                ContextCompat.getColor(this, R.color.pulse_color),
                PorterDuff.Mode.SRC_ATOP
            )
            binding.vHome.visibility = View.VISIBLE

            binding.ivFav.setColorFilter(
                ContextCompat.getColor(this, R.color.white),
                PorterDuff.Mode.SRC_ATOP
            )
            binding.vFav.visibility = View.GONE

            binding.ivCart.setColorFilter(
                ContextCompat.getColor(this, R.color.white),
                PorterDuff.Mode.SRC_ATOP
            )
            binding.vCart.visibility = View.GONE

            binding.ivMenu.setColorFilter(
                ContextCompat.getColor(this, R.color.white),
                PorterDuff.Mode.SRC_ATOP
            )
            binding.vMenu.visibility = View.GONE


        } else if (i == 2) {
            changeFragment(Candidates())

            binding.ivHome.setColorFilter(
                ContextCompat.getColor(this, R.color.white),
                PorterDuff.Mode.SRC_ATOP
            )
            binding.vHome.visibility = View.GONE

            binding.ivFav.setColorFilter(
                ContextCompat.getColor(this, R.color.pulse_color),
                PorterDuff.Mode.SRC_ATOP
            )
            binding.vFav.visibility = View.VISIBLE

            binding.ivCart.setColorFilter(
                ContextCompat.getColor(this, R.color.white),
                PorterDuff.Mode.SRC_ATOP
            )
            binding.vCart.visibility = View.GONE

            binding.ivMenu.setColorFilter(
                ContextCompat.getColor(this, R.color.white),
                PorterDuff.Mode.SRC_ATOP
            )
            binding.vMenu.visibility = View.GONE
        } else if (i == 3) {
            changeFragment(Holidays())


            binding.ivHome.setColorFilter(
                ContextCompat.getColor(this, R.color.white),
                PorterDuff.Mode.SRC_ATOP
            )
            binding.vHome.visibility = View.GONE

            binding.ivFav.setColorFilter(
                ContextCompat.getColor(this, R.color.white),
                PorterDuff.Mode.SRC_ATOP
            )
            binding.vFav.visibility = View.GONE

            binding.ivCart.setColorFilter(
                ContextCompat.getColor(this, R.color.pulse_color),
                PorterDuff.Mode.SRC_ATOP
            )
            binding.vCart.visibility = View.VISIBLE

            binding.ivMenu.setColorFilter(
                ContextCompat.getColor(this, R.color.white),
                PorterDuff.Mode.SRC_ATOP
            )
            binding.vMenu.visibility = View.GONE
        } else if (i == 4) {
            changeFragment(Attendence())


            binding.ivHome.setColorFilter(
                ContextCompat.getColor(this, R.color.white),
                PorterDuff.Mode.SRC_ATOP
            )
            binding.vHome.visibility = View.GONE

            binding.ivFav.setColorFilter(
                ContextCompat.getColor(this, R.color.white),
                PorterDuff.Mode.SRC_ATOP
            )
            binding.vFav.visibility = View.GONE

            binding.ivCart.setColorFilter(
                ContextCompat.getColor(this, R.color.white),
                PorterDuff.Mode.SRC_ATOP
            )
            binding.vCart.visibility = View.GONE

            binding.ivMenu.setColorFilter(
                ContextCompat.getColor(this, R.color.pulse_color),
                PorterDuff.Mode.SRC_ATOP
            )
            binding.vMenu.visibility = View.VISIBLE
        }
    }

    fun changeFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frameLayout, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun exitApp() {
        val dialog = Dialog(this)
        dialog?.setContentView(R.layout.logout_layout)
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog?.setCancelable(true)
        dialog?.window?.attributes?.windowAnimations = R.style.animation
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val yes = dialog?.findViewById<TextView>(R.id.yes)
        val no = dialog?.findViewById<TextView>(R.id.no)
        val title = dialog?.findViewById<TextView>(R.id.title)
        val txtDesc = dialog?.findViewById<TextView>(R.id.txtDesc)

        title?.text = "Exit App"
        txtDesc?.text = "Are you sure to want exit from the app?"


        yes?.setOnClickListener {
            finishAffinity()
            dialog.dismiss()
        }

        no?.setOnClickListener {
            dialog.dismiss()
        }

        dialog?.show()
    }

    override fun onBackPressed() {
//        super.onBackPressed()
        exitApp()
    }
}