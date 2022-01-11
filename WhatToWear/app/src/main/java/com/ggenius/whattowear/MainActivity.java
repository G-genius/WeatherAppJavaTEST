package com.ggenius.whattowear;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ggenius.whattowear.adapter.CategoryAdapter;
import com.ggenius.whattowear.adapter.ClothesAdapter;
import com.ggenius.whattowear.model.Category;
import com.ggenius.whattowear.model.Clothes;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    RecyclerView categoryRecycler, clothesRecycler;
    CategoryAdapter categoryAdapter;
    static ClothesAdapter clothesAdapter;
    static List<Clothes> clothesList = new ArrayList<>();
    //    Для погоды 2 способ
    final String APP_ID = "02a131db06297a28f7490797aea8925e";
    final String WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather";
    final String LANG_RU = "lang=ru";

    final long MIN_TIME = 5000;
    final float MIN_DISTANCE = 1000;
    final int REQUEST_CODE = 101;

    String Location_Provider = LocationManager.GPS_PROVIDER;

    TextView NameofCity, weatherState, Temperature, Feels_like, Humidity, Wind, Pressure;
    ImageView mweatherIcon;

    RelativeLayout mCityFinder;

    LocationManager mLocationManager;
    LocationListener mLocationListener;
    //    Для полученя погоды
    private EditText user_field;
    private Button main_btn;
    private TextView result_info;
    private TextView infoTemperature;
    //private TextView Humidity;
    //private TextView feels_like;
    //private TextView Wind;
    //private TextView Pressure;
    private ImageView water;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        weatherState = findViewById(R.id.infoTemperature);//weatherCondition
        Temperature = findViewById(R.id.result_info);//temperature
        mweatherIcon = findViewById(R.id.weatherIcon);
        NameofCity = findViewById(R.id.cityName);
        Feels_like = findViewById(R.id.feels_like);
        Humidity = findViewById(R.id.Humidity);
        Wind=findViewById(R.id.Wind);
        Pressure=findViewById(R.id.Pressure);


        List<Category> categoryList = new ArrayList<>();
        categoryList.add(new Category(1, "Гардероб"));
        categoryList.add(new Category(2, "Стиль"));
        categoryList.add(new Category(3, "Подписка"));
        categoryList.add(new Category(4, "Прочее"));

        setCategoryRecycler(categoryList);


        clothesList.add(new Clothes(1, "Верхняя одежда", "Куртка"));
        clothesList.add(new Clothes(2, "Нижняя одежда", "Штаны"));
        clothesList.add(new Clothes(3, "Обувь", "Ботинки"));
        clothesList.add(new Clothes(4, "Аксессуары", "Шарф\nВарежки"));

        setClothesRecycler(clothesList);


////        Для погоды
////        user_field = findViewById(R.id.user_field);
////        main_btn = findViewById(R.id.main_btn);
//        result_info = findViewById(R.id.result_info);
//        infoTemperature = findViewById(R.id.infoTemperature);
//        Humidity = findViewById(R.id.Humidity);
//        feels_like = findViewById(R.id.feels_like);
//        Wind = findViewById(R.id.Wind);
//        Pressure = findViewById(R.id.Pressure);
//        water = findViewById(R.id.water);
//
//        main_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (user_field.getText().toString().trim().equals(""))
//                    Toast.makeText(MainActivity.this, R.string.no_user_input, Toast.LENGTH_LONG).show();
//                else {
//                    String city = user_field.getText().toString();
//                    String key = "02a131db06297a28f7490797aea8925e";
//                    String url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + key + "&units=metric&lang=ru";
//
//                    new GetUrlData().execute(url);
//                }
//            }
//        });
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        getWeatherForCurrentLocation();
//    }


    @Override
    protected void onResume() {
        super.onResume();
        Intent mIntent=getIntent();
        String city = mIntent.getStringExtra("City");
        if(city!=null){
            getWeatherForNewCity(city);
        }
        else {
            getWeatherForCurrentLocation();
        }

    }

    private void getWeatherForNewCity(String city){
        RequestParams params = new RequestParams();
        params.put("q",city);
        params.put("appid",APP_ID);
        params.put("ru",LANG_RU);
        LetsdoSomeNetworking(params);
    }

    private void getWeatherForCurrentLocation() {
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {

                String Latitude = String.valueOf(location.getLatitude());
                String Longitude = String.valueOf(location.getLatitude());

                RequestParams params = new RequestParams();
                params.put("lat", Latitude);
                params.put("lon",Longitude);
                params.put("appid",APP_ID);
                params.put("ru",LANG_RU);
                LetsdoSomeNetworking(params);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
            return;
        }
        mLocationManager.requestLocationUpdates(Location_Provider, MIN_TIME, MIN_DISTANCE, mLocationListener);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==REQUEST_CODE){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(MainActivity.this,"Геолокация успешно получена",Toast.LENGTH_LONG).show();
                getWeatherForCurrentLocation();
            }
        }else {
            //Пользователь не дал соглашение
        }
    }

    private void LetsdoSomeNetworking(RequestParams params){
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(WEATHER_URL,params,new JsonHttpResponseHandler()

        {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Toast.makeText(MainActivity.this, "Город успешно получен", Toast.LENGTH_LONG).show();

                weatherData weatherD=weatherData.fromJson(response);
                updateUI(weatherD);
                //super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                //super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    private void updateUI(weatherData weather){
        Temperature.setText(weather.getmTemperature());
        Feels_like.setText(weather.getmFeels_like());
        Wind.setText(weather.getmWind());
        Humidity.setText(weather.getmHumidity());
        Pressure.setText(weather.getmPressure());
        NameofCity.setText(weather.getMcity());
        weatherState.setText(weather.getmWeatherType());
        int resourceID=getResources().getIdentifier(weather.getMicon(),"drawable",getPackageName());
        mweatherIcon.setImageResource(resourceID);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mLocationManager!=null){
            mLocationManager.removeUpdates(mLocationListener);
        }
    }
    //    // Для погоды
//    private class GetUrlData extends AsyncTask<String, String, String>{
//
//        protected void onPreExecute(){
//            super.onPreExecute();
//        }
//
//        @Override
//        protected String doInBackground(String... strings) {
//            HttpURLConnection connection = null;
//            BufferedReader reader = null;
//
//            try {
//                URL url = new URL(strings[0]);
//                connection = (HttpURLConnection) url.openConnection();
//                connection.connect();
//
//                InputStream stream = connection.getInputStream();
//                reader = new BufferedReader(new InputStreamReader(stream));
//
//                StringBuffer buffer = new StringBuffer();
//                String line = "";
//
//                while ((line = reader.readLine()) != null)
//                    buffer.append(line).append("\n");
//                return buffer.toString();
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            } finally {
//                if(connection != null)
//                    connection.disconnect();
//                try {
//                    if (reader != null)
//                        reader.close();
//                } catch (IOException e){
//                    e.printStackTrace();
//                }
//            }
//
//            return null;
//        }
//
//        @SuppressLint("SetTextI18n")
//        @Override
//        protected void onPostExecute(String result){
//            super.onPostExecute(result);
//
//            try {
//                JSONObject jsonObject = new JSONObject(result);
//                result_info.setText(""+jsonObject.getJSONObject("main").getInt("temp") + "°C");
//                infoTemperature.setText(""+jsonObject.getJSONArray("weather").getJSONObject(0).getString("description"));
//                Humidity.setText(""+jsonObject.getJSONObject("main").getInt("humidity") + "%");
//                feels_like.setText("Ощущается как "+jsonObject.getJSONObject("main").getInt("feels_like") + "°C");
//                Wind.setText(""+jsonObject.getJSONObject("wind").getInt("speed") + "м/с");
//                Pressure.setText(""+jsonObject.getJSONObject("main").getInt("pressure") * 0.75 + "мм рт. ст.");
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//
//        }
//    }

    private void setCategoryRecycler(List<Category> categoryList) {

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);

        categoryRecycler = findViewById(R.id.categoryRecycler);
        categoryRecycler.setLayoutManager(layoutManager);

        categoryAdapter = new CategoryAdapter(this, categoryList);
        categoryRecycler.setAdapter(categoryAdapter);

    }

    private void setClothesRecycler(List<Clothes> clothesList) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);

        clothesRecycler = findViewById(R.id.clothesRecycler);
        clothesRecycler.setLayoutManager(layoutManager);

        clothesAdapter = new ClothesAdapter(this, clothesList);
        clothesRecycler.setAdapter(clothesAdapter);
    }

    public void openSettings(View view){
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }
    public void openForecast(View view){
        Intent intent = new Intent(this, Forecast.class);
        startActivity(intent);
    }
}