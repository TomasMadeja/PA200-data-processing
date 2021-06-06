package cz.muni.fi.pa200.dataprocessing;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DataRetrieverTest {

    private DataRetriever api = new DataRetriever();

    @Test
    void getAllFlights() {
        var states = api.getFlightVectors();
        assertThat(states).isNotNull();
        assertThat(states.getStates()).isNotEmpty();
    }

    @Test
    void wat(){

    }
}