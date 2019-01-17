package com.mobileootpinfo.mobileootpinfo.util;

import android.os.AsyncTask;

import com.mobsandgeeks.saripaar.AnnotationRule;

import java.net.HttpURLConnection;
import java.net.URL;

public class ValidateCSVPathRule extends AnnotationRule<ValidateCSVPath, String> {

    /*protected ValidateCSVPathRule(Url url) {
        super(url);
    }*/

    private int httpResponse;

    protected ValidateCSVPathRule(final ValidateCSVPath csvPath) {
        super(csvPath);
    }

    @Override
    public boolean isValid(final String url) {

        try {
            return new verifyCSV().execute(url).get();
        } catch (Exception e) {
            return false;
        }

    }

    private static class verifyCSV extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Boolean doInBackground(String... params) {

            try {
                HttpURLConnection.setFollowRedirects(false);
                if (params[0].isEmpty() || params[0].length() < 1) {
                    return false;
                } else {
                    if (!params[0].endsWith("/")) {
                        params[0] += "/";
                    }
                    HttpURLConnection con = (HttpURLConnection) new URL(params[0] + "leagues.csv").openConnection();
                    con.setRequestMethod("HEAD");
                    con.setConnectTimeout(2000);
                    return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
                //valid = false;
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            //httpResponse = result;
        }
    }
}
