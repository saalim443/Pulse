package codeflies.com.pulse.Intro.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import codeflies.com.pulse.Intro.model.OnboardingDataModel
import codeflies.com.pulse.databinding.OnboardingScreenItemBinding

class OnboardingAdapter(var data: List<OnboardingDataModel> ) : PagerAdapter() {
    // Specify the number of pages
    override fun getCount(): Int {
        return 3
    }
    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view === obj
    }

    // Create the page for the given position
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val binding: OnboardingScreenItemBinding = OnboardingScreenItemBinding.inflate(LayoutInflater.from(container.context), container, false)

        binding.imageView.setImageResource(data.get(position).imageRes)
        container.addView(binding.root)
        return binding.root
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        container.removeView(obj as View)
    }
}
