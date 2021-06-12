package cz.muni.fi.pa200.dataprocessing;

import cz.muni.fi.pa200.dataprocessing.api.OpenSkyApi;
import cz.muni.fi.pa200.dataprocessing.exceptions.RetrievalError;
import cz.muni.fi.pa200.dataprocessing.model.OpenSkyStates;
import cz.muni.fi.pa200.dataprocessing.model.StateVector;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

public class DataRetriever {

    private final OpenSkyApi api;

    public DataRetriever() {
        api = buildApiInstance();
    }

    /**
     * Fetch current flight vector
     * @return OpenSkyStates
     * @throws RetrievalError if error occured during request
     */
    public OpenSkyStates getFlightVectors() {
        try {
            return api.getStates(0, null);
        } catch (IOException e) {
            throw new RetrievalError("Failed to get state vectors", e);
        }
    }

    private static OpenSkyApi buildApiInstance() {
        return new OpenSkyApi(null, null, 10, 70, 60);
    }
}
