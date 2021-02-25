package com.androidfood.mvvm.helper

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.widget.TextView
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import com.androidfood.mvvm.R
import com.androidfood.mvvm.callback.ActionCallback
import com.androidfood.mvvm.constants.AppConstants_Java
import com.androidfood.mvvm.data.model.Restaurant.AddRestaurantResponse
import com.androidfood.mvvm.data.model.auth.Register.UserRegistrationModel
import id.zelory.compressor.Compressor
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.lang.Exception
import java.util.*
import kotlin.jvm.Throws


class GeneralHelper {

    companion object {

        fun getCurrentTime(): Date {
            return Calendar.getInstance().time

        }


        fun showDialog(context: Context, msg: String?, actionCallback: ActionCallback) {
            val dialog = Dialog(context)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.alert_dialog)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            val text: TextView = dialog.findViewById(R.id.text) as TextView
            val dialogButton: TextView = dialog.findViewById(R.id.txtOk) as TextView

            text.setText(msg)
            dialogButton.setOnClickListener {
                actionCallback.onBindingClick(0)
                dialog.dismiss()
            }
            dialog.show()
        }


        fun parseFailureJson(jsonObject: JSONObject): String {
            val message = jsonObject.getString("message")
            val errors = jsonObject.getString("success")
            return message.trim()
        }

        fun updateUserInfo(data: UserRegistrationModel?) {
            val gson = Gson()
            val json = gson.toJson(data)
            PrefsHelper.putString(AppConstants_Java.USERSDATA, json)
        }

        fun updateRestaurantInfo(data: AddRestaurantResponse?) {
            val gson = Gson()
            val json = gson.toJson(data)
            PrefsHelper.putString(AppConstants_Java.USERSRESDATA, json)
        }

        fun getUsersData(): UserRegistrationModel {
            val gson = Gson()
            val usersData = PrefsHelper.getString(AppConstants_Java.USERSDATA)
            val ud: UserRegistrationModel =
                gson.fromJson(usersData, UserRegistrationModel::class.java)
            return ud
        }

        fun getUsersResData(): AddRestaurantResponse? {
            try {
                val gson = Gson()
                val usersData = PrefsHelper.getString(AppConstants_Java.USERSRESDATA)
                val ud: AddRestaurantResponse =
                    gson.fromJson(usersData, AddRestaurantResponse::class.java)
                return ud
            } catch (e: Exception) {
                return null
            }
        }


        fun checkInput(textInputLayout: Array<TextInputLayout>): Boolean {
            for (textInput in textInputLayout) {
                if (textInput.editText?.text.toString().isEmpty()) {
                    return false
                }
            }

            return true
        }

        @Throws(IOException::class)
        fun CompressPic(
            file: File?,
            context: Context?
        ): File {
            return Compressor(context).compressToFile(file)
        }


    }
}