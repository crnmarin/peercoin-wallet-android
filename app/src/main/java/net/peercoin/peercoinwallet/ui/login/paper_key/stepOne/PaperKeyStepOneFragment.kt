package net.peercoin.peercoinwallet.ui.login.paper_key.stepOne

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.paper_key_step_one_fragment.*
import net.peercoin.peercoinwallet.R
import net.peercoin.peercoinwallet.helper.CustomViewModelFactory
import net.peercoin.peercoinwallet.ui.login.LoginActivity

class PaperKeyStepOneFragment : Fragment() {

    companion object {
        val TAG: String = PaperKeyStepOneFragment::class.java.simpleName
        fun newInstance() = PaperKeyStepOneFragment()
    }

    private var WORDS_SIZE: Int = 2
    private lateinit var pagerAdapter: PaperKeyPagerAdapter
    private lateinit var viewModel: PaperKeyStepOneViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.paper_key_step_one_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, CustomViewModelFactory(WORDS_SIZE)).get(PaperKeyStepOneViewModel::class.java)
        setupPager()
        addListeners()
        changePagerDesign(viewPager!!.currentItem)
    }

    fun addListeners() {
        btnPrevious.setOnClickListener {
            viewPager.currentItem = viewPager.currentItem - 1
        }
        btnNext.setOnClickListener {
            if (viewPager.currentItem == viewModel.list.size - 1) {
                (activity as LoginActivity).finishPaperKey(viewModel.getRandomWords())
            } else {
                viewPager.currentItem = viewPager.currentItem + 1
            }
        }
    }

    private val listener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) {

        }

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            changePagerDesign(position)
        }

        override fun onPageSelected(position: Int) {

        }
    }

    fun changePagerDesign(position: Int) {
        when (position) {
            0 -> {
                val colorStateList = ContextCompat.getColorStateList((activity as LoginActivity), R.color.color_grey)
                btnPrevious.backgroundTintList = colorStateList
                btnPrevious.isClickable = false
            }
            1 -> {
                setButtonDefaultColors()
            }
            viewModel.list.size - 2 -> {
                setButtonDefaultColors()
            }
            viewModel.list.size - 1 -> {
                setButtonDefaultColors()
                val colorStateList = ContextCompat.getColorStateList(
                        (activity as LoginActivity), R.color.color_primary_accent)
                btnNext.backgroundTintList = colorStateList
                btnNext.text = (activity as LoginActivity).resources.getText(R.string.finish)

            }

        }
    }

    fun setButtonDefaultColors() {
        val colorStateList = ContextCompat.getColorStateList(
                (activity as LoginActivity), R.color.color_primary_dark)
        btnPrevious.backgroundTintList = colorStateList
        btnPrevious.isClickable = true
        btnNext.backgroundTintList = colorStateList
        btnNext.text = (activity as LoginActivity).resources.getText(R.string.next)
    }

    fun setupPager() {
        pagerAdapter = PaperKeyPagerAdapter(viewModel.list, this.context!!)
        viewPager.adapter = pagerAdapter

        viewPager.addOnPageChangeListener(listener)
    }

}
