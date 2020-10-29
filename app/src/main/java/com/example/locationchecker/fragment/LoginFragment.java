package com.example.locationchecker.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.locationchecker.R;
import com.example.locationchecker.activity.HomeParentsActivity;
import com.example.locationchecker.activity.TypeLoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.ref.Reference;
import java.util.ArrayList;
import java.util.concurrent.Executor;

import static android.content.Context.MODE_PRIVATE;


public class LoginFragment extends Fragment {

    public Executor executors;
    public BiometricPrompt biometricPrompt;
    public BiometricPrompt.PromptInfo promptInfo;
    private  Button btn;

    DatabaseReference myRef;
    private FirebaseAuth mAuth;
    private EditText inputName, inputEmail, inputPassword;
    private Button btnLogin, btnSignUp, btnResetPassword;
    public LoginFragment() {
        // Required empty public constructor
    }
    public void loginAccount(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = new Intent(getActivity(), HomeParentsActivity.class);
                            startActivity(intent);

//                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithEmail:failure", task.getException());
                            Toast.makeText(getContext(), "Authentication failed.",
                                    Toast.LENGTH_LONG).show();
//                            updateUI(null);
                            // ...
                        }

                        // ...
                    }
                });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myRef = FirebaseDatabase.getInstance().getReference();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        btnLogin = (Button) view.findViewById(R.id.btn_login_user);
//        inputName = (EditText) view.findViewById(R.id.edt_name);
        inputEmail = (EditText) view.findViewById(R.id.edt_email);
        inputPassword = (EditText) view.findViewById(R.id.edt_pass);

        //
//        Bundle bundle = this.getArguments();
//        ArrayList<String> data = bundle.getStringArrayList("key");
//        inputEmail.setText(data.get(0));
//        inputPassword.setText(data.get(1));

//        Button btnLogin = view.findViewById(R.id.btn_login_user);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Read from the database
                loginAccount(inputEmail.getText().toString(), inputPassword.getText().toString());

            }
        });

        btn = (Button)view.findViewById(R.id.btnLogin);


        //Biometric


        executors = ContextCompat.getMainExecutor(getContext());

        biometricPrompt = new BiometricPrompt(getActivity(), (Executor) executors, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
//                txt.setText("Loi "+ errString);
                Toast.makeText(getActivity(),errString,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(getActivity(),"Dăng nhập thành công!",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getActivity(), HomeParentsActivity.class);
                startActivity(intent);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(getActivity(),"Đăng nhập thất bại!",Toast.LENGTH_LONG).show();
            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Đăng nhập!")
                .setSubtitle("Sử dụng vân tay để đăng nhập!")
                // Can't call setNegativeButtonText() and
                // setAllowedAuthenticators(...|DEVICE_CREDENTIAL) at the same time.
                .setNegativeButtonText("Sử dụng mật khẩu!")
//                .setAllowedAuthenticators(BIOMETRIC_STRONG | DEVICE_CREDENTIAL)
                .build();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                biometricPrompt.authenticate(promptInfo);
            }
        });



        return view;
    }


    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        //gọi hàm lưu trạng thái ở đây
        savingPreferences();
    }
    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        //gọi hàm đọc trạng thái ở đây
        restoringPreferences();
    }
    /**
     * hàm lưu trạng thái
     */
    public void savingPreferences()
    {
        //tạo đối tượng getSharedPreferences
        SharedPreferences pre=this.getActivity().getSharedPreferences("prefname", MODE_PRIVATE);
        //tạo đối tượng Editor để lưu thay đổi
        SharedPreferences.Editor editor=pre.edit();
        String user=inputEmail.getText().toString();
        String pwd=inputPassword.getText().toString();
        //boolean bchk = chksaveaccount.isChecked();
//        if(!bchk)
//        {
//            //xóa mọi lưu trữ trước đó
//            editor.clear();
//        }
//        else
//        {
            //lưu vào editor
            editor.putString("user", user);
            editor.putString("pwd", pwd);
            //editor.putBoolean("checked", bchk);
        //}
        //chấp nhận lưu xuống file
        editor.commit();
    }
    /**
     * hàm đọc trạng thái đã lưu trước đó
     */
    public void restoringPreferences()
    {
        SharedPreferences pre=this.getActivity().getSharedPreferences
                ("prefname",MODE_PRIVATE);
        //lấy giá trị checked ra, nếu không thấy thì giá trị mặc định là false
//        boolean bchk=pre.getBoolean("checked", false);
//        if(bchk)
//        {
//            //lấy user, pwd, nếu không thấy giá trị mặc định là rỗng
            String user=pre.getString("user", "");
            String pwd=pre.getString("pwd", "");
            inputEmail.setText(user);
            inputPassword.setText(pwd);
//        }
//        chksaveaccount.setChecked(bchk);
    }
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.activity_main, menu);
//        return true;
//    }
}