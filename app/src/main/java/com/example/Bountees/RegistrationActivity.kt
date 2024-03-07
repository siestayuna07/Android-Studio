package com.example.Bountees

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Patterns
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.Bountees.databinding.ActivityPageRegistrationBinding
import com.example.loginactivity.DBHelper
import com.jakewharton.rxbinding2.widget.RxTextView

class RegistrationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPageRegistrationBinding
    private lateinit var db: DBHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPageRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = DBHelper(this)


        val emailStream = RxTextView.textChanges(binding.registerEmail)
            .skipInitialValue()
            .map { email ->
                !isValidEmail(email.toString())
                email.length <15
            }
        emailStream.subscribe(){
            showEmailValidAlert(it)
        }

        val signIn = findViewById<TextView>(R.id.tv_button_SignIn)
        val spannable = SpannableString("Already have an account? Sign In")

        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                val intents = Intent(this@RegistrationActivity, LoginActivity::class.java)
                intents.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intents)
            }
        }

        spannable.setSpan(clickableSpan, 25, 32, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        signIn.text = spannable
        signIn.movementMethod = LinkMovementMethod.getInstance()



//  username validation
        val  usernameStream = RxTextView.textChanges(binding.registerUsername)
            .skipInitialValue()
            .map { username ->
                username.length <6
            }
        usernameStream.subscribe{
            showTextMinimalAlert(it, "Username")
        }
//  password validation
        val passwordStream = RxTextView.textChanges(binding.registerPassword)
            .skipInitialValue()
            .map { password ->
                password.length < 6
            }
        passwordStream.subscribe{
            showTextMinimalAlert(it, "Password")
        }
// confirm password validation

        val passwordConfirmStream = io.reactivex.Observable.merge(
            RxTextView.textChanges(binding.registerPassword)
                .skipInitialValue()
                .map { password ->
                    password.toString() !=binding.registerConfirmPassword.text.toString()
                },
            RxTextView.textChanges(binding.registerConfirmPassword)
                .skipInitialValue()
                .map { confirmPassword ->
                    confirmPassword.toString() !=binding.registerPassword.text.toString()

                })
        passwordConfirmStream.subscribe(){
            showPasswordConfirmAlert(it)
        }
// BUTTON ENABILE TRUE OR FALSE
        val invalidFieldsStream = io.reactivex.Observable.combineLatest(
            emailStream,usernameStream,passwordStream,
            passwordConfirmStream
        ) { emailInvalid: Boolean, usernameInvalid: Boolean,
            passwordInvalid: Boolean, passwordConfirmInvalid: Boolean ->
            !emailInvalid && !usernameInvalid && !passwordInvalid && !passwordConfirmInvalid
        }
        invalidFieldsStream.subscribe{isValid ->
            if(isValid){
                binding.btRegistration.isEnabled = true
                binding.btRegistration.backgroundTintList = ContextCompat.getColorStateList(this,R.color.black)

            }else{
                binding.btRegistration.isEnabled = false
                binding.btRegistration.backgroundTintList = ContextCompat.getColorStateList(this,android.R.color.darker_gray)
            }
        }

        binding.btRegistration.setOnClickListener {
            val regUsername = binding.registerUsername.text.toString()
            val regPassword = binding.registerPassword.text.toString()
            if (regUsername.isNotEmpty()&& regPassword.isNotEmpty()){
            registerDatabase(regUsername,regPassword)
            }
        }
    }
    //private fun showNameExistAlert(isNotValid: Boolean){
       //binding.registerFullname.error = if (isNotValid)"Name is Valid!" else null
    //}
    private fun showTextMinimalAlert(isNotValid: Boolean, text: String){
        if(text=="Username")
            binding.registerUsername.error = if (isNotValid) "$text must consist of 6 characters or more." else null
        else if (text == "Password")
            binding.registerPassword.error = if(isNotValid) "$text must consist of 6 characters or more." else null
    }
    private fun showEmailValidAlert(isNotValid: Boolean){
        binding.registerEmail.error = if (isNotValid)"Email is invalid" else null
    }
    private fun showPasswordConfirmAlert(isNotValid: Boolean){
        binding.registerConfirmPassword.error = if(isNotValid) "Password must match" else null
    }
    private fun registerDatabase(username: String, password: String){
        val insertedRowId = db.insertUser(username,password)
        if (insertedRowId != -1L){
            Toast.makeText(this, "Successful Register", Toast.LENGTH_SHORT).show()
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
            finish()
        }else{
            Toast.makeText(this, "This user already exists", Toast.LENGTH_SHORT).show()
        }

    }
    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches() && email.contains("@") && email.contains(".") && email.contains("gmail")
    }
}