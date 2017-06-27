package fcm;

import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import apimodels.FcmResponse;
import io.fusionbit.vcarrycustomer.App;

/**
 * Created by rutvik on 1/26/2017 at 5:11 PM.
 */

public class FCM
{

    private static final String TAG = App.APP_TAG + FCM.class.getSimpleName();

    public static void sendPushNotification(final String type, final String title,
                                            final String message, final String deviceId,
                                            final JSONObject extra,
                                            final FCMCallbackListener listener) throws IllegalArgumentException
    {

        URL url;

        try
        {
            url = new URL("https://fcm.googleapis.com/fcm/send");
        } catch (MalformedURLException e)
        {
            throw new IllegalArgumentException("invalid url");
        }

        StringBuilder bodyBuilder = new StringBuilder();

        // constructs the POST body using the parameters

        JSONObject root = new JSONObject();
        try
        {
            root.put("to", deviceId);
            JSONObject data = new JSONObject();
            JSONObject innerData = new JSONObject();
            innerData.put("type", type);
            innerData.put("title", title);
            innerData.put("message", message);
            data.put("data", innerData);
            if (extra != null)
            {
                data.put("extra", extra);
            }
            root.put("data", data);
        } catch (JSONException e)
        {
            throw new IllegalArgumentException("CANNOT FORM JSON" + e.getMessage());
        }

        bodyBuilder.append(root.toString());

        String body = bodyBuilder.toString();

        Log.v(TAG, "Posting: " + body + "' to " + url);

        byte[] bytes = body.getBytes();

        HttpURLConnection conn = null;

        try
        {

            Log.i(TAG, "URL->" + url);

            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setFixedLengthStreamingMode(bytes.length);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", "key=AAAAh0-x11I:APA91bHdQc3YJjY68n9z4LBss9YG8zn_6HxC1iKc-b4G61E48agDOAiVXTUBQyw3MTCX8QxxtHAx0Phh8Q8VEsBB4bJjCyz06JMVrSbFn69pUeZGNRUir758HFpYzGaWMmH38SJKMCMWBimiZhMDWKRAxP3fBurUQg");
            conn.setRequestProperty("Host", "fcm.googleapis.com");

            // post the request
            OutputStream out = conn.getOutputStream();
            out.write(bytes);
            out.close();

            // handle the response
            int status = conn.getResponseCode();

            Log.i(TAG, "RESPONSE from server: " + status);

            listener.sentNotificationHttpStatus(status, readInputStreamToString(conn));

        } catch (IOException e)
        {
            throw new IllegalArgumentException("failed to send push notification");
        } finally
        {
            if (conn != null)
            {
                conn.disconnect();
            }

        }
    }

    private static FcmResponse readInputStreamToString(HttpURLConnection connection)
    {
        String result = null;
        StringBuffer sb = new StringBuffer();
        InputStream is = null;

        try
        {
            is = new BufferedInputStream(connection.getInputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String inputLine = "";
            while ((inputLine = br.readLine()) != null)
            {
                sb.append(inputLine);
            }
            result = sb.toString();
        } catch (Exception e)
        {
            Log.i(TAG, "Error reading InputStream");
            result = null;
        } finally
        {
            if (is != null)
            {
                try
                {
                    is.close();
                } catch (IOException e)
                {
                    Log.i(TAG, "Error closing InputStream");
                }
            }
        }

        Log.i(TAG, result);

        Gson gson = new Gson();
        final FcmResponse response =
                gson.fromJson(result, FcmResponse.class);

        return response;
    }

    public interface FCMCallbackListener
    {
        void sentNotificationHttpStatus(int statusCode, FcmResponse response);
    }

}
