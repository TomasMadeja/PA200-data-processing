package cz.muni.fi.pa200.dataprocessing;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DataRetrieverTest {

    private DataRetriever api = new DataRetriever();

    @Test
    void getAllFlights() {
        String response = api.getFlightData();
        assertThat(response).isNotEmpty();
    }

    @Test
    void wat(){

    }
}