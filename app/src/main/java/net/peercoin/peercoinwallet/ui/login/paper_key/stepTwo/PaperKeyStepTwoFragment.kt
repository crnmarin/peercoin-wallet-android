package net.peercoin.peercoinwallet.ui.login.paper_key.stepTwo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.paper_key_step_two_fragment.*
import net.peercoin.peercoinwallet.R

class PaperKeyStepTwoFragment : Fragment() {

    companion object {
        val TAG: String? = PaperKeyStepTwoFragment::class.java.simpleName
        private const val WORDS_KEY = "key_for_map"

        fun newInstance(randomWords: HashMap<Int, String>): PaperKeyStepTwoFragment {
            val paperKeyStepTwoFragment = PaperKeyStepTwoFragment()
            val args = Bundle()
            args.putSerializable(WORDS_KEY, randomWords)
            paperKeyStepTwoFragment.arguments = args
            return paperKeyStepTwoFragment
        }
    }

    private lateinit var adapter: WordsAdapter
    private lateinit var viewModel: PaperKeyStepTwoViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.paper_key_step_two_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(PaperKeyStepTwoViewModel::class.java)

        val args: Bundle = this.arguments!!
        val map: HashMap<Int, String> = args.getSerializable(WORDS_KEY) as HashMap<Int, String>
        adapter = WordsAdapter(map)
        rvWords.layoutManager = LinearLayoutManager(context)
        rvWords.adapter = adapter


    }

}
