package cz.muni.fi.pa200.dataprocessing.functions;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.*;
import java.util.Random;

import com.microsoft.azure.functions.annotation.*;
import com.microsoft.azure.functions.*;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.StorageUri;
import com.microsoft.azure.storage.table.CloudTable;
import cz.muni.fi.pa200.dataprocessing.DataRetriever;
import cz.muni.fi.pa200.dataprocessing.StateVectorUploader;
import cz.muni.fi.pa200.dataprocessing.exceptions.UploadError;
import cz.muni.fi.pa200.dataprocessing.model.OpenSkyStates;

import static cz.muni.fi.pa200.dataprocessing.data.Config.getTableURI;

/**
 * Azure Functions with Timer trigger.
 */
public class TimerTriggerFunction {
    /**
     * This function will be invoked periodically according to the specified schedule.
     */
    @FunctionName("TimerTrigger")
    public void run(
        @TimerTrigger(name = "timerInfo", schedule = "1 * * * * *") String timerInfo,
        final ExecutionContext context
    ) {
        context.getLogger().info("Java Timer trigger function executed at: " + LocalDateTime.now());

        OpenSkyStates flightVectors = retrieveFlightVectors();

        URI primaryURI = getTableURI();
        if (primaryURI == null) {
            context.getLogger().info("Could not create URI.");
            return;
        }

        try {
            uploadFlightVectorsToTable(primaryURI, flightVectors);
        } catch (UploadError e) {
            context.getLogger().info("Could not upload flight vectors.");
        }
    }

    private OpenSkyStates retrieveFlightVectors() {
        DataRetriever dataRetriever = new DataRetriever();
        return dataRetriever.getFlightVectors();
    }

    /**
     * Uploads flight vectors to table specified by URI
     * @param primaryUri URI of table
     * @param flightVectors OpenSkyStates
     * @throws UploadError if error occurred during upload
     */
    private void uploadFlightVectorsToTable(URI primaryUri, OpenSkyStates flightVectors) {
        try {
            CloudTable table = new CloudTable(new StorageUri(primaryUri));
            StateVectorUploader stateVectorUploader = new StateVectorUploader(new Random(), table, 32);
            stateVectorUploader.uploadToTable(flightVectors.getStates(), flightVectors.getTime());
        } catch (StorageException e) {
            throw new UploadError("Upload error", e.getCause());
        }
    }
}
