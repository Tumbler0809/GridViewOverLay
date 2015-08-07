package com.lzc.overlay;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
/**
 * Created by Administrator on 2015/4/15.
 */
public class myHttpRequestVolley extends Request<JSONObject>{
    private final  String TAG="myHttpRequestVolley";
    private Map<String,String> map;
    private com.android.volley.Response.Listener<JSONObject> mListener;
    public myHttpRequestVolley(int method, String url, Map<String,String> m,Response.Listener<JSONObject> listener, Response.ErrorListener errorListener)
    {
        super(method, url, errorListener);
        this.mListener = listener;
        this.map=m;
    }

    /**
     * Init The Params
     * @param mParams
     */
    public void setParams(Map<String,String> mParams){
        this.map = mParams;
    }

    @Override
    protected  Map<String,String>  getParams()throws AuthFailureError {
        Log.e(TAG,map.toString());
        return map;
    }
    protected void deliverResponse(JSONObject response)
    {
        this.mListener.onResponse(response);
    }
    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse paramNetworkResponse){
        JSONObject result=null;
        String parsed;
        try {
             parsed = new String(paramNetworkResponse.data, HttpHeaderParser.parseCharset(paramNetworkResponse.headers));
        } catch (UnsupportedEncodingException e) {
            parsed = new String(paramNetworkResponse.data);

        }
        try {
            result=new JSONObject(parsed);
        }catch (JSONException e){
            e.printStackTrace();
        }
        return Response.success(result, HttpHeaderParser.parseCacheHeaders(paramNetworkResponse));
    }

}
