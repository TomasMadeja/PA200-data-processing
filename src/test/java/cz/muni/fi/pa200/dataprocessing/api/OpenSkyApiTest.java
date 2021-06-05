package cz.muni.fi.pa200.dataprocessing.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.muni.fi.pa200.dataprocessing.model.StateVector;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

class OpenSkyApiTest {

    private OpenSkyApi api = new OpenSkyApi(null, null);

    @Test
    void sampleTest() throws IOException {
        var response = api.getStates(0, null);
        assertThat(response).isNotNull();
        assertThat(response.getStates()).isNotEmpty();

        ObjectMapper mapper = new ObjectMapper();
        List<String> rows = response.getStates().stream().map(val -> {
            try {
                return mapper.writeValueAsString(val);
            } catch (Exception e) {
                return null;
            }
        }).collect(toList());
        assertThat(mapper.readValue(rows.get(0), StateVector.class)).isNull();
        assertThat(rows).isEmpty();
    }
}