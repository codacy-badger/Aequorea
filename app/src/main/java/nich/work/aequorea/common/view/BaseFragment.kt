package nich.work.aequorea.common.view

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import nich.work.aequorea.Aequorea

abstract class BaseFragment : Fragment() {

    private lateinit var mLayoutView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mLayoutView = provideContentView()
        ButterKnife.bind(this, mLayoutView)
        return mLayoutView
    }

    protected fun inflate(id: Int): View {
        return layoutInflater.inflate(id, null)
    }

    protected abstract fun provideContentView(): View

    fun isInLightTheme(): Boolean {
        return Aequorea.isLightTheme()
    }

    fun withBundle(bundle: Bundle): BaseFragment {
        arguments = bundle
        return this
    }

}