package com.example.delicious.Fragment.ChildFragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.delicious.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class DishComment extends Fragment {
    View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_dish_comment, container, false);
        initView();
        return rootView;
    }
    public void initView() {
        TextView test =  rootView.findViewById(R.id.tv_push);
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             /*   Notification notification = new NotificationCompat.Builder(getActivity(), "push")
                        .setContentTitle("Hello")
                        .setContentText("lOLO CC")
                        .setSmallIcon(R.drawable.ic_f_icreamt)
                        .build();
                NotificationManagerCompat  managerCompat = NotificationManagerCompat.from(getActivity());
                managerCompat.notify(123335, notification);*/
               getAccessToken();

            }
        });
    }
    public void getAccessToken() {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        String url = "https://oauth-login.cloud.huawei.com/oauth2/v3/token";

            JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        sendnotification(response.getString("access_token"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getActivity(), "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                public byte[] getBody() {
                    Map<String, String> jsonObject = new HashMap<>();
                        jsonObject.put("grant_type", "client_credentials");
                        jsonObject.put("client_id", "104544311");
                        jsonObject.put("client_secret","089cac0ea3cdcbc703c2fb23cddbf19c2e6350a88d30db54af1fa987d56853ae");
                    if (jsonObject != null && jsonObject.size() > 0) {
                        return encodeParameters(jsonObject, getParamsEncoding());
                    }
                    return null;
                }

                @Override
                public String getBodyContentType() {
                    return "application/x-www-form-urlencoded; charset=UTF-8";
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/x-www-form-urlencoded");
                    headers.put("POST", "/oauth2/v3/token HTTP/1.1");
                    headers.put("Host","oauth-login.cloud.huawei.com");
                    return headers;
                }
            };
            requestQueue.add(objectRequest);
    }
    private byte[] encodeParameters(Map<String, String> params, String paramsEncoding) {
        StringBuilder encodedParams = new StringBuilder();
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                encodedParams.append(URLEncoder.encode(entry.getKey(), paramsEncoding));
                encodedParams.append('=');
                encodedParams.append(URLEncoder.encode(entry.getValue(), paramsEncoding));
                encodedParams.append('&');
            }
            return encodedParams.toString().getBytes(paramsEncoding);
        } catch (UnsupportedEncodingException uee) {
            throw new RuntimeException("Encoding not supported: " + paramsEncoding, uee);
        }
    }
    public void sendnotification(String accessToken){
        RequestQueue r = Volley.newRequestQueue(getActivity());
        String url = "https://push-api.cloud.huawei.com/v1/104544311/messages:send";
        JSONArray jsonArray = new JSONArray();
        jsonArray.put("AECA6iwiG459BTpYvcw81KEeJgcJnIZnWlNzbjX5anRAeC0fz6NeJLgl6fmGU5LUya8XsH-KtAmvuAPtV9vwl6GbzBvXow7c31Z_K7_u4eUeQgQpahK_UT0-BmtED6yPlQ");
        JSONObject mainObj = new JSONObject();
        try {
            JSONObject message = new JSONObject();
            //
            JSONObject clickaction = new JSONObject();
            clickaction.put("type",3);
            clickaction.put("url","https://example.com");
            JSONObject notificationChild = new JSONObject();
            notificationChild.put("icon", "/raw/bg_hoaqua");
            notificationChild.put("sound","/raw/shake");
            notificationChild.put("title","hello");
            notificationChild.put("body","xin chao");
            notificationChild.put("channel_id","push");
            notificationChild.put("click_action",clickaction);
            String when = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(Calendar.getInstance().getTime());
            notificationChild.put("when", when);
            //
            JSONObject android = new JSONObject();
            android.put("notification", notificationChild);
            //
            JSONObject notification = new JSONObject();
            notification.put("title","hello");
            notification.put("body","xin chao");

            //
            message.put("notification",notification);
            message.put("android", android);
            message.put("token",jsonArray);
            mainObj.put("validate_only", false);
            mainObj.put("message", message);

            JsonObjectRequest js = new JsonObjectRequest(Request.Method.POST, url, mainObj, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        Log.e("err",response.getString("msg"));
                        Log.e("err2",response.getString("code"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getActivity(), ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> header = new HashMap<>();
                    header.put("Content-Type","application/json");
                    header.put("Authorization","Bearer "+ accessToken);
                    return header;
                }

            };
            r.add(js);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}