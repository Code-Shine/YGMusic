package com.example.musicplayer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends BaseActivity {

    private EditText Text1,Text2;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private CheckBox rememberPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        Text1 = (EditText)findViewById(R.id.editText1);//find获得用户名的实例对象
        Text2 = (EditText)findViewById(R.id.editText2);//find获得密码的实例对象
        rememberPass = (CheckBox) findViewById(R.id.remember_pass);

        //设置复选框的勾选为false
        boolean isRemember = pref.getBoolean("remember_password",false);

        if(isRemember){
            //将账号和密码都放到到文本框中
            String account = pref.getString("account","");
            String password = pref.getString("password","");
            Text1.setText(account);
            Text2.setText(password);
            rememberPass.setChecked(true);
        }
        //登陆按钮
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                String username = Text1.getText().toString();
                String password = Text2.getText().toString();
                if(username.equals( "admin") && password.equals( "111")){
                    editor = pref.edit();
                    if(rememberPass.isChecked()){//检查复选框是否被选中
                        editor.putBoolean("remember_password",true);
                        editor.putString("account",username);
                        editor.putString("password",password);

                    }else {
                        editor.clear();
                    }
                    editor.apply();

                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);

//                    //存储用户名与密码
//                    Bundle bundle = new Bundle();
//                    bundle.putCharSequence("u_name",username);
//                    bundle.putCharSequence("p_word",password);
//                    intent.putExtras(bundle);

                    startActivity(intent);
                    //登陆后就撤销当前活动
                    finish();

                }
                else {
                    Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}