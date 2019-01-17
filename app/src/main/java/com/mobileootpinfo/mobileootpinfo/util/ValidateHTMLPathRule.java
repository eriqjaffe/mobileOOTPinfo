package com.mobileootpinfo.mobileootpinfo.util;

import android.os.AsyncTask;

import com.mobsandgeeks.saripaar.AnnotationRule;

import java.net.HttpURLConnection;
import java.net.URL;

public class ValidateHTMLPathRule extends AnnotationRule<ValidateHTMLPath, String> {

    /*protected ValidateHTMLPathRule(Url url) {
        super(url);
    }*/

    private int httpResponse;

    protected ValidateHTMLPathRule(final ValidateHTMLPath htmlPath) {
        super(htmlPath);
    }

    @Override
    public boolean isValid(final String url) {

        try {
            return new verifyHTML().execute(url).get();
        } catch (Exception e) {
            return false;
        }

    }

    private static class verifyHTML extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Boolean doInBackground(String... params) {

            try {
                HttpURLConnection.setFollowRedirects(false);
                if (params[0].isEmpty() || params[0].length() < 1) {
                    return true;
                } else {
                    if (!params[0].endsWith("/")) {
                        params[0] += "/";
                    }
                    HttpURLConnection con =  (HttpURLConnection) new URL(params[0]+"news/html/index.html").openConnection();
                    con.setRequestMethod("HEAD");
                    con.setConnectTimeout(2000);
                    return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
                }
            } catch (java.net.SocketTimeoutException e) {
                return false;
            } catch (java.io.IOException e) {
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            //httpResponse = result;
        }
    }
}