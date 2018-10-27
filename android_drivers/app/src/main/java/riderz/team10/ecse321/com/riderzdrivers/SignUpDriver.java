package riderz.team10.ecse321.com.riderzdrivers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;
import cz.msebera.android.httpclient.Header;
import riderz.team10.ecse321.com.riderzdrivers.constants.HTTP;
import riderz.team10.ecse321.com.riderzdrivers.constants.TAG;
import riderz.team10.ecse321.com.riderzdrivers.constants.URL;
import riderz.team10.ecse321.com.riderzdrivers.http.HttpRequestClient;



public class SignUpDriver extends AppCompatActivity implements HttpRequestClient {

    protected boolean success;
    protected String msg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mapButtons();
    }

    @Override
    public void mapButtons(){
        Button signUpButton = findViewById(R.id.signUpButton);

        signUpButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v){
                syncHttpRequest();
                if(success){
                    msg="";
                    /*TODO*/
                }
                else{
                    msg="";
                    /*TODO*/
                }
            }

        });

    }

    public String getPlainText(int idName){

        EditText text = findViewById(idName);
        return text.getText().toString();
    }

    @Override
    public void syncHttpRequest(){


        final String userSignUpUrl = URL.baseUrl + URL.userUrl + "addUser/";
        final String driverSignUpUrl = URL.baseUrl+URL.driverUrl + "new/";




        final String firstName = getPlainText(R.id.firstName);
        final String lastName = getPlainText(R.id.lastName);
        final String username = getPlainText(R.id.userName);
        final String email = getPlainText(R.id.email);
        final String password = getPlainText(R.id.password);
        final String phone = getPlainText(R.id.phoneNumber);
        final RequestParams userParams = new RequestParams();
        final RequestParams driverParams = new RequestParams();


        userParams.add("username", username );
        userParams.add("email", email);
        userParams.add("firstName",firstName);
        userParams.add("lastName",lastName);
        userParams.add("password",password);
        userParams.add("phone", phone);

        driverParams.add("operator", username);



        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                SyncHttpClient client = new SyncHttpClient();
                client.setTimeout(HTTP.maxTimeout);

                client.post(userSignUpUrl, userParams, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        Log.d("Debug","Successful creation of user in Driver Sign Up");
                        Log.e("Debug",new String (responseBody));
                        /*TODO*/
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Log.e("Debug","Sign Up driver process to create user failed");
                        /*TODO*/
                    }
                });


                client.put(driverSignUpUrl, driverParams, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        Log.d("Debug","Successful creation of driver in Driver Sign Up");
                        /*TODO*/
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Log.e("Debug","Sign Up driver process to create driver failed");
                        /*TODO*/
                    }
                });
            }
        });
        t.start();

        try {
            t.join();
        } catch (InterruptedException e) {
            Log.e("Debug", "Thread exception in login thread");
        }


    }



}
