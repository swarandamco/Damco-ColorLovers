package com.demo.damcogroup.damcocolorlovers

import ColorDataModel
import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.demo.damcogroup.damcocolorlovers.adapters.RecyclerViewAdapter
import com.demo.damcogroup.damcocolorlovers.apirequests.RestServiceHandler
import com.demo.damcogroup.damcocolorlovers.databinding.ActivityMainBinding
import com.demo.damcogroup.damcocolorlovers.utils.KEY_WORD
import com.demo.damcogroup.damcocolorlovers.utils.avoidDoubleClicks
import com.demo.damcogroup.damcocolorlovers.utils.isConnectionAvailable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity() {

    var disposable: Disposable? = null
    val appApiServe by lazy {
        RestServiceHandler.create()
    }
    var list: List<ColorDataModel>? = null
    lateinit var binding: ActivityMainBinding
    val TAG = "color"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUi()
    }


   private fun initUi() {
        callApiToGetColors(KEY_WORD);
//        binding.etsearch?.setText("")

        binding.btnsearch.setOnClickListener {
            hideKeyboard(it)
            avoidDoubleClicks(it)

            var data = binding.etsearch?.text.toString()
            if (data.isNullOrEmpty()){
                data = ""
            }

//          Checking length validation on Search button
            if (data.length > 2) {
                callApiToGetColors(data)
            } else {
                showToast("Input must be greater than 2 characters")
            }
        }

    }

// This method is using to bind the data on GridView
    private fun bindAdapter(list: List<ColorDataModel>?) {
        val myAdapter = RecyclerViewAdapter(this, list!!)
        binding.rvImages?.layoutManager =
            GridLayoutManager(this, 2)
        binding.rvImages?.adapter = myAdapter
        binding.progressBar?.visibility = View.GONE
    }

  // This method is using to call get colours api
    private fun callApiToGetColors(keyword: String) {
        if (isConnectionAvailable(applicationContext)) {
            binding.progressBar?.visibility = View.VISIBLE
            disposable =
                appApiServe.getColourList(keyword, "json", "20")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ result ->
                        Log.d(TAG, "Res is: " + result)
                        list = result
                        if (list != null) {
                            bindAdapter(list)
                        } else {
                            Log.e(TAG, "List is null or empty")
                        }
                    },
                        { error ->
                            binding.progressBar?.visibility = View.GONE
                            Toast.makeText(
                                applicationContext,
                                "Item Not Found Error!",
                                Toast.LENGTH_SHORT
                            ).show()
                            Log.e(TAG, "error response is: " + error.message.toString())
                        })

        } else {
            binding.progressBar?.visibility = View.GONE
            showToast("Please Check your Internet Connection!")
        }

    }

// This function is using to hide the Soft keyboard input
   private fun hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

//    This function is using for displaying Alert popup messages
   private fun showToast(message: String?) {
        if (message != null && message.length > 0) {
            Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
        }
    }

}