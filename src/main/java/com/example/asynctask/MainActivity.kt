package com.example.asynctask

import android.app.ProgressDialog
import android.content.Context
import android.net.ConnectivityManager
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.OkHttpClient
import okhttp3.Request

class MainActivity : AppCompatActivity() {
    lateinit var context: Context
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        context = this
        getQuestions().execute()
    }

    internal inner class getQuestions : AsyncTask<Void, Void, String, >() {
        lateinit var progressDialog: ProgressDialog

        var hasInternet = false
        override fun doInBackground(vararg params: Void?): String {

            if (isNetworkAvailable()){
                hasInternet = true
                val client = OkHttpClient()
                val url = "https://community.atlassian.com/t5/Marketplace-Apps-Integrations/Introducing-Trellinator-Automate-Trello-with-Google-Apps-Script/ba-p/925271"
                val request = Request.Builder().url(url).build()
                val response = client.newCall(request).execute()
                return response.body?.string().toString()
            }else
                return ""
        }



        override fun onPreExecute() {
            super.onPreExecute()
            progressDialog = ProgressDialog(context)
            progressDialog.setMessage("Downloading question")
            progressDialog.setCancelable(false)
            progressDialog.show()

        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            progressDialog.dismiss()

            if (hasInternet){
                tv_task.text = result

            }else
                "No Internet".also { tv_task.text = it }
        }
    }


    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

}