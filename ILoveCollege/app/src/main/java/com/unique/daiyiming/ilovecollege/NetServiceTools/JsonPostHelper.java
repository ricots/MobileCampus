package com.unique.daiyiming.ilovecollege.NetServiceTools;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by daiyiming on 2015/7/11.
 */
public class JsonPostHelper {

    private final static String GET_PARAMS_RESULT = "result"; //结果
    private final static String GET_PARAMS_CAUSE = "cause"; //失败原因

    private RequestQueue requestQueue = null;

    /**
     * 只要有回应即调用
     */
    public interface OnJsonPostHelperResonsedListener {
        void OnJsonPostHelperResponsed();
    }

    /**
     * 回应为成功时候的调用
     */
    public interface OnJsonPostHelperResonseSuccessedListener {
        void OnJsonPostHelperResonseSuccessed(JSONObject response);
    }

    /**
     * 回应为失败时候的调用
     */
    public interface OnJsonPostHelperResonseFiledListener {
        void OnJsonPostHelperResonseFiled(String cause);
    }

    public JsonPostHelper(Context context) {
        requestQueue = Volley.newRequestQueue(context);
    }

    /**
     * 从网络获取Json返回值
     * @param url 网络地址
     * @param jsonObject jsonObjec对象
     * @param responsed 只要有返回，无论成功失败都调用
     * @param successed 返回成功时候的调用
     * @param filed 返回失败时的调用
     */
    public void getJsonResponse(String url, JSONObject jsonObject, final OnJsonPostHelperResonsedListener responsed, final OnJsonPostHelperResonseSuccessedListener successed, final OnJsonPostHelperResonseFiledListener filed) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (responsed != null) {
                    responsed.OnJsonPostHelperResponsed();
                }
                int result = 0;
                try {
                    result = Integer.parseInt(response.getString(GET_PARAMS_RESULT));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (result == 1) {
                    if (successed != null) {
                        successed.OnJsonPostHelperResonseSuccessed(response);
                    }
                } else {
                    String cause = "";
                    try {
                        cause = response.getString(GET_PARAMS_CAUSE);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (filed != null) {
                        filed.OnJsonPostHelperResonseFiled(cause);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (responsed != null) {
                    responsed.OnJsonPostHelperResponsed();
                }
                if (filed != null) {
                    filed.OnJsonPostHelperResonseFiled("");
                }
            }
        });
        requestQueue.add(request);
    }
}
