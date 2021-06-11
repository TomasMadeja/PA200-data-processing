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
import cz.muni.fi.pa200.dataprocessing.model.OpenSkyStates;

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

        URI primaryUri = getTableURI();
        if (primaryUri == null) {
            context.getLogger().info("Could not create URI.");
            return;
        }

        try {
            uploadFlightVectorsToTable(primaryUri, flightVectors);
        } catch (StorageException e) {
            e.printStackTrace();
        }
    }

    private OpenSkyStates retrieveFlightVectors() {
        DataRetriever dataRetriever = new DataRetriever();
        return dataRetriever.getFlightVectors();
    }

    /**
     * Creates URI which specifies the table, the URI must include the SAS token
     * @return URI of the table, null if the creation does not succeed
     *
     * https://docs.microsoft.com/en-us/java/api/com.microsoft.azure.storage.table.cloudtable?view=azure-java-legacy
     * https://docs.microsoft.com/en-us/azure/storage/common/storage-sas-overview
     */
    private URI getTableURI() {
        URI primaryUri = null;
        try {
            primaryUri = new URI(""); // TODO enter the table URI
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return primaryUri;
    }

    /**
     * Uploads flight vectors to table specified by URI
     * @param primaryUri URI of table
     * @param flightVectors OpenSkyStates
     * @throws StorageException if error occurred during upload
     */
    private void uploadFlightVectorsToTable(URI primaryUri, OpenSkyStates flightVectors) throws StorageException {
        CloudTable table = new CloudTable(new StorageUri(primaryUri));
        StateVectorUploader stateVectorUploader = new StateVectorUploader(new Random(), table, 32);
        stateVectorUploader.uploadToTable(flightVectors.getStates(), flightVectors.getTime());
    }
}
