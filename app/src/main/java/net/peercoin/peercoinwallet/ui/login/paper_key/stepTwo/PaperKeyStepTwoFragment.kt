package net.peercoin.peercoinwallet.ui.login.paper_key.stepTwo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import net.peercoin.peercoinwallet.R

class PaperKeyStepTwoFragment : Fragment() {

    companion object {
        val TAG: String? = PaperKeyStepTwoFragment::class.java.simpleName

        fun newInstance(randomWords: List<String>) = PaperKeyStepTwoFragment()
    }

    private lateinit var viewModel: PaperKeyStepTwoViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.paper_key_step_two_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(PaperKeyStepTwoViewModel::class.java)

    }

}
