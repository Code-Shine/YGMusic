package com.example.musicplayer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private EditText Text1,Text2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                Text1 = (EditText)findViewById(R.id.editText1);//find获得用户名的实例对象
                Text2 = (EditText)findViewById(R.id.editText2);//find获得密码的实例对象
                String username = Text1.getText().toString();
                String password = Text2.getText().toString();
                if(username.equals( "admin") && password.equals( "111")){

                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);

                    Bundle bundle = new Bundle();
                    bundle.putCharSequence("u_name",username);
                    bundle.putCharSequence("p_word",password);
                    intent.putExtras(bundle);

                    startActivity(intent);
                }
                else {
                    Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}