package com.dewii.mpeapp.networks;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.dewii.mpeapp.utils.LogUtils;
import com.dewii.mpeapp.utils.NetworkUtils;
import com.dewii.mpeapp.utils.PreferenceUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class VolleyProvider {
    private static final String TAG = VolleyProvider.class.getCanonicalName();


    public interface OnResponseListener<T> {
        void onSuccess(T response);

        void onFailure(VolleyError volleyError);
    }


    private static VolleyProvider volleyProvider;
    private RequestQueue requestQueue;
    private Context context;


    private VolleyProvider(Context context) {
        this.context = context;
        requestQueue = new RequestQueue(new DiskBasedCache(context.getCacheDir(),
                NetworkUtils.CACHE_SIZE_MAX), new BasicNetwork(new HurlStack()));
        requestQueue.start();
    }


    public static synchronized VolleyProvider getProvider(Context context) {
        if (volleyProvider == null) {
            volleyProvider = new VolleyProvider(context.getApplicationContext());
        }
        return volleyProvider;
    }


    public void addToRequestQueue(MultipartRequest multipartRequest) {
        requestQueue.add(multipartRequest);
    }


    public void executePostRequest(String url, JSONArray params, OnResponseListener<JSONArray> onResponseListener, boolean cache, boolean hasAuth) {
        LogUtils.logInfo(TAG, "executePostRequest: ");

        if (NetworkUtils.isNetworkAvailable()) {
            try {
                requestQueue.getCache().remove(url);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        LogUtils.logRequest(TAG, url + ":" + params.toString());
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.POST, url, params,
                response -> {
                    LogUtils.logResponse(TAG, response.toString());
                    if (onResponseListener != null)
                        onResponseListener.onSuccess(response);
                },
                error -> {
                    LogUtils.logError(TAG, NetworkUtils.getErrorString(error));
                    if (onResponseListener != null)
                        onResponseListener.onFailure(error);
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + PreferenceUtils.getString(context, PreferenceUtils.KEY_TOKEN));
                return headers;
            }

            @Override
            protected Response<JSONArray> parseNetworkResponse(NetworkResponse networkResponse) {
                return NetworkUtils.parseArray(networkResponse);
            }
        };

        request.setShouldCache(cache);
        request.setRetryPolicy(NetworkUtils.RETRY_POLICY);
        request.setTag(TAG);
        requestQueue.add(request);
    }


    public void executePostRequest(String url, JSONObject jsonParams, OnResponseListener<JSONObject> onResponseListener, boolean cache, boolean hasAuth) {
        LogUtils.logInfo(TAG, "executePostJsonRequest: ");

        if (NetworkUtils.isNetworkAvailable()) {
            try {
                requestQueue.getCache().remove(url);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        LogUtils.logRequest(TAG, url + ":" + jsonParams.toString());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonParams,
                response -> {
                    LogUtils.logResponse(TAG, response.toString());
                    if (onResponseListener != null)
                        onResponseListener.onSuccess(response);
                },
                error -> {
                    LogUtils.logError(TAG, NetworkUtils.getErrorString(error));
                    if (onResponseListener != null)
                        onResponseListener.onFailure(error);
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + PreferenceUtils.getString(context, PreferenceUtils.KEY_TOKEN));
                return headers;
            }
        };

        request.setShouldCache(cache);
        request.setRetryPolicy(NetworkUtils.RETRY_POLICY);
        request.setTag(TAG);
        requestQueue.add(request);
    }


    public void executePostRequest(String url, Map<String, String> params, OnResponseListener<String> onResponseListener, boolean cache, boolean hasAuth) {
        LogUtils.logInfo(TAG, "executePostRequest: ");
        if (NetworkUtils.isNetworkAvailable()) {
            try {
                requestQueue.getCache().remove(url);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        LogUtils.logRequest(TAG, url + ":" + params.toString());
        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    LogUtils.logResponse(TAG, response);
                    if (onResponseListener != null)
                        onResponseListener.onSuccess(response);
                },
                error -> {
                    LogUtils.logError(TAG, NetworkUtils.getErrorString(error));
                    if (onResponseListener != null)
                        onResponseListener.onFailure(error);
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + PreferenceUtils.getString(context, PreferenceUtils.KEY_TOKEN));
                return headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };

        request.setShouldCache(cache);
        request.setRetryPolicy(NetworkUtils.RETRY_POLICY);
        request.setTag(TAG);
        requestQueue.add(request);
    }


    public void executeGetRequest(String url, OnResponseListener<String> onResponseListener, boolean cache, boolean hasAuth) {
        LogUtils.logInfo(TAG, "executeGetRequest: ");

        if (NetworkUtils.isNetworkAvailable()) {
            try {
                requestQueue.getCache().remove(url);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        LogUtils.logRequest(TAG, url);
        StringRequest request = new StringRequest(Request.Method.GET, url,
                response -> {
                    LogUtils.logResponse(TAG, response);
                    if (onResponseListener != null)
                        onResponseListener.onSuccess(response);
                },
                error -> {
                    LogUtils.logError(TAG, NetworkUtils.getErrorString(error));
                    if (onResponseListener != null)
                        onResponseListener.onFailure(error);
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + PreferenceUtils.getString(context, PreferenceUtils.KEY_TOKEN));
                return headers;
            }
        };

        request.setShouldCache(cache);
        request.setRetryPolicy(NetworkUtils.RETRY_POLICY);
        request.setTag(TAG);
        requestQueue.add(request);
    }


    public void executeUrlEncodedRequest(String url, Map<String, String> params, OnResponseListener<String> onResponseListener, boolean cache, boolean hasAuth) {
        LogUtils.logInfo(TAG, "executePostRequest: ");
        if (NetworkUtils.isNetworkAvailable()) {
            try {
                requestQueue.getCache().remove(url);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        LogUtils.logRequest(TAG, url + ":" + params.toString());
        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    LogUtils.logResponse(TAG, response);
                    if (onResponseListener != null)
                        onResponseListener.onSuccess(response);
                },
                error -> {
                    LogUtils.logError(TAG, NetworkUtils.getErrorString(error));
                    if (onResponseListener != null)
                        onResponseListener.onFailure(error);
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorzation", PreferenceUtils.getString(context, PreferenceUtils.KEY_TOKEN));
                return headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };

        request.setShouldCache(cache);
        request.setRetryPolicy(NetworkUtils.RETRY_POLICY);
        request.setTag(TAG);
        requestQueue.add(request);
    }


    public void executeMultipartRequest(String url, Map<String, String> params, OnResponseListener<String> onResponseListener, boolean cache, boolean hasAuth) {
        executeMultipartRequest(url, params, null, onResponseListener, cache, hasAuth);
    }

    public void executeMultipartRequest(String url, Map<String, String> params, Map<String, File> files, OnResponseListener<String> onResponseListener, boolean cache, boolean hasAuth) {
        LogUtils.logInfo(TAG, "executeMultipartRequest: ");
        if (NetworkUtils.isNetworkAvailable()) {
            try {
                requestQueue.getCache().remove(url);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        LogUtils.logRequest(TAG, url + ":" + params.toString());
        MultipartRequest request = new MultipartRequest(url, params, files,
                response -> {
                    LogUtils.logResponse(TAG, response);
                    if (onResponseListener != null)
                        onResponseListener.onSuccess(response);
                },
                error -> {
                    LogUtils.logError(TAG, NetworkUtils.getErrorString(error));
                    if (onResponseListener != null)
                        onResponseListener.onFailure(error);
                })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", PreferenceUtils.getString(context, PreferenceUtils.KEY_TOKEN));
                return headers;
            }
        };

        request.setShouldCache(cache);
        request.setRetryPolicy(NetworkUtils.RETRY_POLICY);
        request.setTag(TAG);
        requestQueue.add(request);
    }

    public void executeMultipartGetRequest(String url, OnResponseListener<String> onResponseListener, boolean cache, boolean hasAuth) {
        executeMultipartGetRequest(url, null, onResponseListener, cache, hasAuth);
    }

    public void executeMultipartGetRequest(String url, Map<String, File> files, OnResponseListener<String> onResponseListener, boolean cache, boolean hasAuth) {
        LogUtils.logInfo(TAG, "executeMultipartRequest: ");
        if (NetworkUtils.isNetworkAvailable()) {
            try {
                requestQueue.getCache().remove(url);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        LogUtils.logRequest(TAG, url);
        MultipartRequest request = new MultipartRequest(url, files,
                response -> {
                    LogUtils.logResponse(TAG, response);
                    if (onResponseListener != null)
                        onResponseListener.onSuccess(response);
                },
                error -> {
                    LogUtils.logError(TAG, NetworkUtils.getErrorString(error));
                    if (onResponseListener != null)
                        onResponseListener.onFailure(error);
                })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", PreferenceUtils.getString(context, PreferenceUtils.KEY_TOKEN));
                return headers;
            }
        };

        request.setShouldCache(cache);
        request.setRetryPolicy(NetworkUtils.RETRY_POLICY);
        request.setTag(TAG);
        requestQueue.add(request);
    }


    public void cancelRequest(String tag) {
        requestQueue.cancelAll(tag);
    }
}
