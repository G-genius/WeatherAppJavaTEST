package com.ggenius.whattowear;

import org.json.JSONException;
import org.json.JSONObject;

public class weatherData {
    private String mTemperature,mHumidity, mFeels_like,mWind, mPressure, micon,mcity,mWeatherType,weatherIcon;
    private int mCondition;

    public static weatherData fromJson(JSONObject jsonObject){
        try {
            weatherData weatherD=new weatherData();
            weatherD.mcity=jsonObject.getString("name");
            weatherD.mCondition=jsonObject.getJSONArray("weather").getJSONObject(0).getInt("id");
            weatherD.mWeatherType=jsonObject.getJSONArray("weather").getJSONObject(0).getString("main");
            weatherD.mHumidity=jsonObject.getJSONObject("main").getString("humidity") + "%";
            weatherD.mWind=jsonObject.getJSONObject("wind").getInt("speed") + "м/с";
            weatherD.mPressure=jsonObject.getJSONObject("main").getInt("pressure") * 0.75 + "мм рт. ст.";
//            weatherD.mFeels_like="Ощущается как "+jsonObject.getJSONObject("main").getInt("feels_like")+ "°C";

            weatherD.micon=updateWeatherIcon(weatherD.mCondition);
            double tempResult=jsonObject.getJSONObject("main").getDouble("temp")-273.15;
            int roundedValue=(int)Math.rint(tempResult);
            weatherD.mTemperature=Integer.toString(roundedValue);
            double feels_like_result=jsonObject.getJSONObject("main").getDouble("feels_like")-273.15;
            int roundValue2=(int)Math.rint(feels_like_result);
            weatherD.mFeels_like="Ощущается как "+Integer.toString(roundValue2)+"°C";
            return weatherD;
        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String updateWeatherIcon(int condition){
        if(condition>=0 && condition<=300){
            return "ic_lighning";
        }
        else if(condition>=300 && condition<=500){
            return "ic_rain";
        }
        else if(condition>=500 && condition<=600){
            return "ic_rain";//Иконка душ
        }
        else if(condition>=600 && condition<=700){
            return "ic_snow";
        }
        else if(condition>=772 && condition<=800){
            return "ic_cloudy";//overcast пасмурно
        }
        else if(condition>=701 && condition<=771){
            return "ic_smog";
        }
        else if(condition==800){
            return "ic_sun_cloud";
        }
        else if(condition>=801 && condition<=804){
            return "ic_cloudy";
        }
        else if(condition>=900 && condition<=902){
            return "ic_lighning";//ic_lighning 2
        }
        else if(condition==903){
            return "ic_snow";
        }
        else if(condition==904){
            return "ic_sun_cloud";
        }
        else if(condition>=905 && condition<=1000){
            return "ic_lighning";//ic_lighning 2
        }

        return "dunno";
    }

    public String getmTemperature() {
        return mTemperature+"°C";
    }

    public String getMicon() {
        return micon;
    }

    public String getMcity() {
        return mcity;
    }

    public String getmWeatherType() {
        return mWeatherType;
    }

    public String getmHumidity() {
        return mHumidity;
    }

    public String getmFeels_like() {
        return mFeels_like;
    }

    public String getmWind() {
        return mWind;
    }

    public String getmPressure() {
        return mPressure;
    }
}
