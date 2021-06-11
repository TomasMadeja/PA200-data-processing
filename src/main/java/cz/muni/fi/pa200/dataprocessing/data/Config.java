package cz.muni.fi.pa200.dataprocessing.data;

import java.net.URI;
import java.net.URISyntaxException;

public class Config {

    /**
     * Gets URI which specifies the table, the URI must include the SAS token
     * @return URI of the table, null if the creation does not succeed
     *
     * https://docs.microsoft.com/en-us/java/api/com.microsoft.azure.storage.table.cloudtable?view=azure-java-legacy
     * https://docs.microsoft.com/en-us/azure/storage/common/storage-sas-overview
     */
    public static URI getTableURI() {
        URI primaryURL = null;
        try {
            primaryURL = new URI(""); // TODO enter the table URI
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return primaryURL;
    }
}
