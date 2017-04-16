package com.stay4it.im.net;

import android.os.AsyncTask;

import com.stay4it.im.untils.Trace;

import java.net.HttpURLConnection;

/**
 * @author Stay
 * @version create time：Sep 15, 2014 12:35:20 PM
 */
public class RequestTask extends AsyncTask<Void, Integer, Object> {
    private Request request;
    private static final int ACTION_UPLOAD = 0;
    private static final int ACTION_DOWNLOAD = 1;

    public RequestTask(Request request) {
        this.request = request;
        Trace.d("request url:" + request.url);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Object doInBackground(Void... params) {
        int retryCount = 0;
        int retry = 0;
        if (request.callback != null) {
            retryCount = request.callback.retryCount();
        }
        return request(retry, retryCount);
    }


    private Object request(int retry, int retryCount) {
        try {
            Object object = null;
            if (request.callback != null) {
                object = request.callback.preRequest();
                if (object != null) {
                    return object;
                }
            }
            if (request.uploadListener != null) {
                request.tmpUploadLister = new OnUploadProgressChangedListener() {

                    @Override
                    public void onProgressUpdate(int curPos, int contentLength) {
                        publishProgress(ACTION_UPLOAD, curPos, contentLength);
                    }
                };
            }
            HttpURLConnection connection = HttpUrlConnectionUtil.execute(request);
            if (request.callback != null) {
                if (request.listener != null) {
                    object = request.callback.handle(connection, new IRequestListener() {

                        @Override
                        public void onProgressUpdate(int curPos, int contentLength) {
                            publishProgress(ACTION_DOWNLOAD, curPos, contentLength);
                        }
                    });
                } else {
                    object = request.callback.handle(connection);
                }
                return request.callback.postRequest(object);
            } else {
                return null;
            }
        } catch (AppException e) {
            if (e.getStatus() == AppException.ExceptionStatus.TimeoutException) {
                if (retry < retryCount) {
                    return request(retry++, retryCount);
                }
            }
            return e;
        }
    }

    @Override
    protected void onPostExecute(Object result) {
        super.onPostExecute(result);
        if (request.callback.isForceCancelled()) {
            return;
        }
        if (request.callback != null) {
            if (result != null && result instanceof AppException) {
                AppException exception = (AppException) result;
                if (exception.getStatus() == AppException.ExceptionStatus.ServerException) {
                    if (exception.getErrorCode() == 403) {
//						TODO show dialog()
//						request.callback.onTokenInvalid();
                    }
                }
                request.callback.onFailure((AppException) result);
            } else {
                request.callback.onSuccess(result);
            }
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        if (values[0] == ACTION_DOWNLOAD) {
            request.listener.onProgressUpdate(values[1], values[2]);
        } else {
            request.uploadListener.onProgressUpdate(values[1], values[2]);
        }
    }


}
