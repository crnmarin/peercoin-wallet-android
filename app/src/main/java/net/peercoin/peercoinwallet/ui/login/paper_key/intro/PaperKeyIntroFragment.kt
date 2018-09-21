package net.peercoin.peercoinwallet.ui.login.paper_key.intro

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_paper_key_intro.*

import net.peercoin.peercoinwallet.R
import net.peercoin.peercoinwallet.ui.login.LoginActivity

class PaperKeyIntroFragment : Fragment() {

    companion object {
        val TAG: String = PaperKeyIntroFragment::class.java.simpleName
        fun newInstance() = PaperKeyIntroFragment()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_paper_key_intro, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        btnWriteDown.setOnClickListener {
            (activity as LoginActivity).writePaperKey()
        }

    }

}
