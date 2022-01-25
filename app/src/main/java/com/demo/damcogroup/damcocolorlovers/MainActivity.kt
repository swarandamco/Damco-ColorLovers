package com.demo.damcogroup.damcocolorlovers

import DataModel
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
import com.demo.damcogroup.damcocolorlovers.utils.isConnectionAvailable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity() {

    var disposable: Disposable? = null
    val appApiServe by lazy {
        RestServiceHandler.create()
    }
    var list: List<DataModel>? = null
//    var rv_images: RecyclerView? = null
//    var et_search: EditText? = null
//    var progress_Load_Colors: ProgressBar? = null

    lateinit var binding: ActivityMainBinding
    val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        setContentView(R.layout.activity_main)
        initUi()
    }

    fun initUi() {
//        et_search = findViewById<EditText>(R.id.etsearch)
//        val btn_search = findViewById<Button>(R.id.btnsearch)
//        rv_images = findViewById<RecyclerView>(R.id.rv_images)
//        progress_Load_Colors = findViewById<ProgressBar>(R.id.progressBar)

        callApiToGetColors(KEY_WORD);
        binding.etsearch?.setText("")
//        binding.progressBar?.visibility = View.GONE

        binding.btnsearch.setOnClickListener {
            //
            hideKeyboard(it)
            var data = binding.etsearch?.text.toString()

            if (data.isNullOrEmpty()){
                data = ""
            }

            if (data.length > 2) {
                callApiToGetColors(data)
            } else {
                showToast("Input must be greater than 2 characters")
            }
        }

    }


    private fun bindAdapter(list: List<DataModel>?) {
        val myAdapter = RecyclerViewAdapter(this, list!!)
        binding.rvImages?.layoutManager =
            GridLayoutManager(this, 2)
        binding.rvImages?.adapter = myAdapter
        binding.progressBar?.visibility = View.GONE
    }

    private fun callApiToGetColors(keyword: String) {
        if (isConnectionAvailable(applicationContext)) {
            binding.progressBar?.visibility = View.VISIBLE
            disposable =
                appApiServe.getListOfColors(keyword, "json", "20")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ result ->
                        Log.d(TAG, "Response is: " + result)
                        list = result
                        if (list != null) {
                            bindAdapter(list)
                        } else {
                            Log.e(TAG, "List is null or empty")
                        }
//                        val myAdapter = RecyclerViewAdapter(this, list!!)
//                        binding.rvImages?.layoutManager =
//                            GridLayoutManager(this, 2)
//                        binding.rvImages?.adapter = myAdapter
//                        binding.progressBar?.visibility = View.GONE
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

    fun hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun showToast(message: String?) {
        if (message != null && message.length > 0) {
            Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
        }
    }

}