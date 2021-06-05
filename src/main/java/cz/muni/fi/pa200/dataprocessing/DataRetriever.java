package cz.muni.fi.pa200.dataprocessing;

import cz.muni.fi.pa200.dataprocessing.exceptions.RetrievalError;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class DataRetriever {

    String getFlightData() {
        var builder = new OkHttpClient.Builder();
        builder.connectTimeout(50, TimeUnit.SECONDS);
        builder.callTimeout(50, TimeUnit.SECONDS);
        builder.readTimeout(50, TimeUnit.SECONDS);
        OkHttpClient client = builder.build();
        client.writeTimeoutMillis();
        String url = "https://opensky-network.org/api/states/all";

        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (IOException e) {
            throw new RetrievalError(url, e);
        }
    }
}
