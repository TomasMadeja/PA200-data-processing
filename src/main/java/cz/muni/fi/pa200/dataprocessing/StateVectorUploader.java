package cz.muni.fi.pa200.dataprocessing;

import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.table.CloudTable;
import com.microsoft.azure.storage.table.TableBatchOperation;
import cz.muni.fi.pa200.dataprocessing.exceptions.AzureTableException;
import cz.muni.fi.pa200.dataprocessing.model.StateVector;

import java.util.Collection;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class StateVectorUploader {

    private static final int UPPER_BOUND = 99_999;

    private final Random rand;

    private final CloudTable table;
    private final int batchSize;

    public StateVectorUploader(Random rand, CloudTable table, int batchSize) {
        this.rand = rand;
        this.table = table;
        this.batchSize = batchSize;
    }

    /**
     * Uploads vector to the specified table
     * @param vectors vector of states
     * @param time time of the state vector
     */
    public void uploadToTable(Collection<StateVector> vectors, int time) {
        TableBatchOperation batchOp = new TableBatchOperation();
        String partition_prefix =  buildPrefix(time);
        AtomicInteger partitionCounter = new AtomicInteger(0);
        AtomicInteger rowCounter = new AtomicInteger(0);
        // There are no guarantees for this to be thread safe, otherwise, would use parallelStream
        vectors.forEach(vector -> {
            updateKeys(
                    partition_prefix,
                    vector,
                    partitionCounter,
                    rowCounter
            );
            batchOp.insert(vector);
        });
        try {
            table.execute(batchOp);
        } catch (StorageException e) {
            throw new AzureTableException(e);
        }
    }

    private synchronized void updateKeys(
            String partition_prefix,
            StateVector vector,
            AtomicInteger partitionCounter,
            AtomicInteger rowCounter
    ) {
        vector.setPartitionKey(String.format("%s_%s", partition_prefix, partitionCounter));
        vector.setRowKey(String.format("%s_%s_%s", partition_prefix, partitionCounter, rowCounter));
        if (rowCounter.incrementAndGet() >= batchSize) {
            rowCounter.set(0);
            partitionCounter.getAndIncrement();
        }
    }

    private String buildPrefix(int time) {
        return String.format("%d_%d", time, rand.nextInt(UPPER_BOUND));
    }
}
