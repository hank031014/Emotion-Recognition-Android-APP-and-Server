package com.emotion.emotion;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by AS V5-573G on 2017/5/29.
 */

public class SVMPost extends AsyncTask<String, String, Integer> {
    private static final String TAG_SUCCESS = "success";
    private static final String url_SL = "http://140.116.154.88/emotion/SVM_manage.py";
    static String json = "";

    private JSONObject jsonObj;
    private String[][] data;
    private String resp;
    private Activity activity;
    private ProgressDialog pDialog;

    public SVMPost(Activity a){
        activity = a;
    }

    // loading...
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(activity);
        pDialog.setMessage("Loading...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();
    }

    // running...
    protected Integer doInBackground(String... args) {
        String postStr = args[0];

        // Building Parameters
        try{
            JSONObject postJson = new JSONObject();
            postJson.put("value", 1523);
            postJson.put("str", postStr);
            int value = 5123;
            String up = "value=" + value + "&str=" + URLEncoder.encode(postStr, "UTF-8");

            URL url = new URL(url_SL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", "" + Integer.toString(up.getBytes().length));
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setDoOutput(true);

            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.writeBytes(up);
            Log.d("JSON_WR", up);
            wr.flush();
            wr.close();

            // 讀取資料
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            json = sb.toString();
            Log.d("JSON", "J: " + json);
            reader.close();

            if (Thread.interrupted()) {
                throw new InterruptedException();
            }
            if (json.equals("")) {
                Thread.sleep(1000);
            }
        }
        catch(Exception e)
        {
            //e.printStackTrace();
            Log.e("JSON POST", "Error POST data " + e.toString());
            return -1;
        }

        try {
            jsonObj = new JSONObject(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
        try {
            return jsonObj.getInt(TAG_SUCCESS);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return 0;
    }

    // finish!
    protected void onPostExecute(Integer success) {
        switch (success){
            case 0:
                Toast.makeText(activity.getApplicationContext(), "操作失敗，請聯絡管理員", Toast.LENGTH_LONG).show();
                break;
            case 1:
                Log.d("EmotionApp","Load successfully");
                //JSONArray jsonArray;
                try {
                    //jsonArray = jsonObj.getJSONArray("data");
                    resp = jsonObj.getString("message");
                    Toast.makeText(activity.getApplicationContext(), resp, Toast.LENGTH_LONG).show();
                        /*data = new String[jsonArray.length()][3];

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            data[i][0] = jsonObject.getString("friend_name");
                            data[i][1] = jsonObject.getString("nickname");
                            data[i][2] = jsonObject.getString("friend_img");

                        }*/
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                Log.d("EmotionApp", "NULL");
                break;
        }
        pDialog.dismiss();
    }
}
