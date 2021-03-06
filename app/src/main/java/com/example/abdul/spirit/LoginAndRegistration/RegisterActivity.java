package com.example.abdul.spirit.LoginAndRegistration;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.abdul.spirit.R;
import com.example.abdul.spirit.Utils.Constants;
import com.example.abdul.spirit.Utils.Requesthandler;
import com.example.abdul.spirit.Utils.SharedPrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    // activity vars
    private AppCompatEditText reg_username,reg_password,reg_conf_pass,reg_name,reg_age,reg_role;
    private AppCompatButton reg_btn_reg;
    private TextView reg_button_log;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //activity vars
        reg_username = (AppCompatEditText)findViewById(R.id.reg_email_edit);
        reg_password = (AppCompatEditText)findViewById(R.id.reg_pass_edit);
        reg_conf_pass = (AppCompatEditText)findViewById(R.id.reg_confirm_pass_edit);
        reg_btn_reg = (AppCompatButton)findViewById(R.id.reg_button_register);
        reg_button_log = (TextView)findViewById(R.id.reg_button_login);
        reg_name = (AppCompatEditText)findViewById(R.id.reg_name_edit);
        reg_age = (AppCompatEditText)findViewById(R.id.reg_age_edit);
        reg_role = (AppCompatEditText)findViewById(R.id.reg_role_edit);
        progressDialog = new ProgressDialog(this);

        reg_button_log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoLogPage = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(gotoLogPage);
                finish();
            }
        });

        if (SharedPrefManager.getInstance(this).isLoggedIn()){
            finish();
//            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
            return;

        }

        reg_btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }


    private void registerUser() {
        final String confirmPass = reg_conf_pass.getText().toString().trim();
        final String username = reg_username.getText().toString().trim();
        final String password = reg_password.getText().toString().trim();
        final String name = reg_name.getText().toString().trim();
        final String age = reg_age.getText().toString().trim();
        final String role = reg_role.getText().toString().trim();



        if (!confirmPass.isEmpty() && !username.isEmpty() && !password.isEmpty() && !name.isEmpty() && !age.isEmpty() && !role.isEmpty()){

            if(password.equals(confirmPass)){
                progressDialog.setMessage("Registering user...");
                progressDialog.show();

                StringRequest stringRequest = new StringRequest(Request.Method.POST,
                        Constants.REGISTER_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                progressDialog.dismiss();

                                try {
                                    JSONObject jsonObject = new JSONObject(response);

                                    Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progressDialog.hide();
                                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("username", username);
                        params.put("password", password);
                        params.put("name",name);
                        params.put("age",age);
                        params.put("role",role);
                        return params;
                    }
                };


                Requesthandler.getInstance(this).addToRequestQueue(stringRequest);


            }else {
                Toast.makeText(getApplicationContext(), "Passwords do not match.", Toast.LENGTH_LONG).show();

            }

        }else {
            Toast.makeText(
                    getApplicationContext(),
                    "Enter credentials",
                    Toast.LENGTH_LONG
            ).show();

        }





    }

}
