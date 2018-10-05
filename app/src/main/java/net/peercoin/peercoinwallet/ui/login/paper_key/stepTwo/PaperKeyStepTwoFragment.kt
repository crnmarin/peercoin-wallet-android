package net.peercoin.peercoinwallet.ui.login.paper_key.stepTwo

import android.app.AlertDialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.FocusFinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_finger.*
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

        setupRecyclerView()
        btnSubmit.setOnClickListener {
            if (adapter.isCorrectFulfilled()) {
                AlertDialog.Builder(activity).setTitle(getString(R.string.success)).setMessage("Great succeeded!").create().show()
            } else {
                AlertDialog.Builder(activity).setTitle(getString(R.string.incorrect_title)).setMessage(getString(R.string.incorrect_words_message)).create().show()
            }
        }


    }

    fun setupRecyclerView() {
        val args: Bundle = this.arguments!!
        val map: HashMap<Int, String> = args.getSerializable(WORDS_KEY) as HashMap<Int, String>
        adapter = WordsAdapter(map, object : WordsAdapter.TouchListener {
            override fun removeFocusFromRv() {
                //TODO focus remain on edit text after input is over, it should be removed
//                val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//                imm.hideSoftInputFromWindow(btnSubmit.windowToken, 0)
//                btnSubmit.isFocusableInTouchMode = true
//                btnSubmit.requestFocus()
//                Handler().postDelayed({
//                    btnSubmit.isFocusableInTouchMode = false
//                    btnSubmit.clearFocus()
//                    rvWords.clearFocus()
//                }, 50)

            }
        })
        rvWords.layoutManager = LinearLayoutManager(context)
        rvWords.adapter = adapter
    }

}
