package riderz.team10.ecse321.com.riderzpassengers;

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
import riderz.team10.ecse321.com.riderzpassengers.constants.HTTP;
import riderz.team10.ecse321.com.riderzpassengers.constants.URL;
import riderz.team10.ecse321.com.riderzpassengers.http.HttpRequestClient;

public class SignUpPassengers extends AppCompatActivity implements HttpRequestClient {

    protected boolean success;
    protected String msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_passengers);
        mapButtons();
    }

    @Override
    public void mapButtons(){
        Button signUpButton = findViewById(R.id.signUpButton);
        signUpButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                syncHttpRequest();
                if(success){
                    msg="";
                    /*ToDO*/
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
    public void syncHttpRequest (){
        final String userSignUpUrl = URL.baseUrl + URL.userUrl + "addUser/";



        final String firstName = getPlainText(R.id.firstName);
        final String lastName = getPlainText(R.id.lastName);
        final String username = getPlainText(R.id.username);
        final String email = getPlainText(R.id.email);
        final String password = getPlainText(R.id.password);
        final String phone = getPlainText(R.id.phoneNumber);
        final RequestParams userParams = new RequestParams();


        userParams.add("username", username );
        userParams.add("email", email);
        userParams.add("firstName",firstName);
        userParams.add("lastName",lastName);
        userParams.add("password",password);
        userParams.add("phone", phone);




        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                SyncHttpClient client = new SyncHttpClient();
                client.setTimeout(HTTP.maxTimeout);

                client.post(userSignUpUrl, userParams, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        Log.d("Debug","Successful creation of user in User Sign Up");
                        Log.e("Debug",new String (responseBody));
                        /*TODO*/
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Log.e("Debug","Sign Up User process to create user failed");
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
