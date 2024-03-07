package com.example.Bountees

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.Bountees.AddToCart.DatabaseHelper
import com.example.Bountees.Dashboard.Dashboard
import com.example.Bountees.databinding.ActivityPageLoginBinding
import com.example.loginactivity.DBHelper
import com.jakewharton.rxbinding2.widget.RxTextView

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPageLoginBinding
    private lateinit var db: DBHelper
    override fun onCreate(savedInstanceState: Bundle?) {

        db = DBHelper(this)
        super.onCreate(savedInstanceState)
        binding = ActivityPageLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)



        //  username validation
        val  usernameStream = RxTextView.textChanges(binding.loginUsername)
            .skipInitialValue()
            .map { username ->
                username.isEmpty()
            }
        usernameStream.subscribe{
            showTextMinimalAlert(it, "Username")
        }
//  password validation
        val passwordStream = RxTextView.textChanges(binding.loginPassword)
            .skipInitialValue()
            .map { password ->
                password.isEmpty()
            }
        passwordStream.subscribe{
            showTextMinimalAlert(it, "Password")
        }

// BUTTON ENABILE TRUE OR FALSE
        val invalidFieldsStream = io.reactivex.Observable.combineLatest(
            usernameStream,
            passwordStream
        ) {
                usernameInvalid: Boolean,
                passwordInvalid: Boolean,
            ->
            !usernameInvalid && !passwordInvalid
        }
        invalidFieldsStream.subscribe{isValid ->
            if(isValid){
                binding.btConfirmLogin.isEnabled = true
                binding.btConfirmLogin.backgroundTintList = ContextCompat.getColorStateList(this, R.color.black)

            }else{
                binding.btConfirmLogin.isEnabled = false
                binding.btConfirmLogin.backgroundTintList = ContextCompat.getColorStateList(this,android.R.color.darker_gray)
            }
        }



        binding.btConfirmLogin.setOnClickListener {
            val userName = binding.loginUsername.text.toString()
            val passWord = binding.loginPassword.text.toString()
            if (userName.isNotEmpty()&& passWord.isNotEmpty()) {

                loginDatabase(userName,passWord)
                Log.i("Test", "HomeLogin")
            }


        }
        binding.createAccount.setOnClickListener {
            startActivity(Intent(this, RegistrationActivity::class.java))
            finish()
        }
    }
    private fun showTextMinimalAlert(isNotValid: Boolean, text: String){
        if(text=="Email/Username")
            binding.loginUsername.error = if (isNotValid) "$text Login" else null
        else if (text == "Password")
            binding.loginPassword.error = if(isNotValid) "$text Password" else null
    }



    private fun loginDatabase(username:String, password: String) {
        val userExist = db.readUser(username, password)
        if (userExist) {
            Toast.makeText(this, "Login Success", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, Dashboard::class.java)
            clearDatabase()
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show()
        }


    }

    private fun clearDatabase() {
        val dbHelper = DatabaseHelper(this)
        val dbTwo = dbHelper.writableDatabase
        dbTwo.execSQL("DELETE FROM cart")
        dbTwo.close()

    }

}