package ro.pub.cs.systems.eim.practicaltest02;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.net.InetAddress;
import java.util.concurrent.RunnableScheduledFuture;

public class PracticalTest02MainActivity extends AppCompatActivity {

    //server
    private EditText serverPortEditText = null;
    private Button connectButton = null;

    //client
    private EditText clientAdressEditText = null;
    private EditText clientPortEditText = null;
    private EditText cityEditText = null;
    private Spinner informationTypeSpinner = null;
    private Button getWeatherForecastButton = null;
    private TextView weatherForecastTextView = null;

    private ServerThread serverThread = null;
    private ClientThread clientThread = null ;
    ConnectButtonOnClickListener connectButtonOnClickListener = new ConnectButtonOnClickListener();

    private class ConnectButtonOnClickListener implements Button.OnClickListener{

        @Override
        public void onClick(View v) {
            String serverPort = serverPortEditText.getText().toString();
            if(serverPort == null || serverPort.isEmpty()){
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Server port should be filled!", Toast.LENGTH_SHORT).show();
                return;
            }
            serverThread = new ServerThread(Integer.parseInt(serverPort));
            if(serverThread.getServerSocket() == null){
                Log.e(Constants.TAG, "[MAIN ACTIVITY] Could not create server thread!");
                return;
            }
            serverThread.start();
        }
    }
    GetWeatherForecastButtonClickListener getWeatherForecastButtonClickListener = new GetWeatherForecastButtonClickListener();
    public class GetWeatherForecastButtonClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            String clientAddress = clientAdressEditText.getText().toString();
            String clientPort = clientPortEditText.getFontFeatureSettings().toString();
            if(clientAddress ==  null || clientAddress.isEmpty() ||
                    clientPort == null || clientPort.isEmpty())
            {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Client connection parameters should be filled!", Toast.LENGTH_SHORT).show();
                return;
            }

            if(serverThread == null || ! serverThread.isAlive()){
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] There is no server to connect to!", Toast.LENGTH_SHORT).show();
                return;
            }
            String city = cityEditText.getText().toString();
            String informationType = informationTypeSpinner.getSelectedItem().toString();

            if(city == null || city.isEmpty() ||
                informationType == null || informationType.isEmpty())
            {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Parameters from client (city / information type) should be filled", Toast.LENGTH_SHORT).show();
                return;
            }
            weatherForecastTextView.setText(Constants.EMPTY_STRING);
            clientThread = new ClientThread(clientAddress, Integer.parseInt(clientPort), city, informationType, weatherForecastTextView);
            clientThread.start();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        serverPortEditText = (EditText)findViewById(R.id.server_port_edit_text);
        connectButton = (Button) findViewById(R.id.connect_button);
        connectButton.setOnClickListener(connectButtonOnClickListener);

        clientAdressEditText = (EditText)findViewById(R.id.client_address_edit_text);
        clientPortEditText = (EditText)findViewById(R.id.client_port_edit_text);
        informationTypeSpinner = (Spinner)findViewById(R.id.information_type_spinner);

        getWeatherForecastButton = (Button)findViewById(R.id.get_weather_forecast_button);
        getWeatherForecastButton.setOnClickListener(getWeatherForecastButtonClickListener);

        weatherForecastTextView = (TextView)findViewById(R.id.weather_forecast_text_view);
    }

    protected void onDestroy(){
        Log.i(Constants.TAG, "[MAIN ACTIVITY] onDestroy() callback method has been invoked");
        if(serverThread != null)
        {
            serverThread.stopThread();
        }
        super.onDestroy();
    }
}