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
        val wordsToAnswerSize: Int = 2
        fun newInstance() = PaperKeyStepOneFragment()
    }

    private lateinit var pagerAdapter: PaperKeyPagerAdapter
    private lateinit var viewModel: PaperKeyStepOneViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.paper_key_step_one_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, CustomViewModelFactory(wordsToAnswerSize)).get(PaperKeyStepOneViewModel::class.java)
        setupPager()
        addListeners()
    }

    fun addListeners() {
        btnPrevious.setOnClickListener {
            viewPager.currentItem = viewPager.currentItem - 1
        }
        btnNext.setOnClickListener {
            if (viewPager.currentItem == viewModel.list.size - 1) {
                (activity as LoginActivity).finishPaperKey(viewModel.getRandomWords())
            } else
                viewPager.currentItem = viewPager.currentItem + 1
        }
    }

    private val listener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) {

        }

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            when (position) {
                0 -> {
                    val colorStateList = ContextCompat.getColorStateList((activity as LoginActivity), R.color.color_grey)
                    btnPrevious.backgroundTintList = colorStateList
                    btnPrevious.isClickable = false
                }
                1 -> {
                    val colorStateList = ContextCompat.getColorStateList(
                            (activity as LoginActivity), R.color.color_primary_dark)
                    btnPrevious.backgroundTintList = colorStateList
                    btnPrevious.isClickable = true
                }
                viewModel.list.size - 1 -> {
                    val colorStateList = ContextCompat.getColorStateList(
                            (activity as LoginActivity), R.color.color_primary_accent)
                    btnNext.backgroundTintList = colorStateList
                    btnNext.text = (activity as LoginActivity).resources.getText(R.string.finish)

                }
                viewModel.list.size - 2 -> {
                    val colorStateList = ContextCompat.getColorStateList(
                            (activity as LoginActivity), R.color.color_primary_dark)
                    btnNext.backgroundTintList = colorStateList
                    btnNext.text = (activity as LoginActivity).resources.getText(R.string.next)
                }

            }
        }

        override fun onPageSelected(position: Int) {

        }
    }

    fun setupPager() {
        pagerAdapter = PaperKeyPagerAdapter(viewModel.list, this!!.context!!)
        viewPager.adapter = pagerAdapter

        viewPager.addOnPageChangeListener(listener)
    }

}
