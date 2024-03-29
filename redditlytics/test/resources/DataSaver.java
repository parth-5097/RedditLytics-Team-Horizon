package businesslogic;

import javax.inject.Inject;
import java.util.*;

public class DataSaver {
    private KeyResults apiClient;

    @Inject
    public DataSaver(KeyResults apiClient) {
        this.apiClient = apiClient;
    }

    public String getAndSaveData() {
        String data = apiClient.getData("apple");
        return data;
    }
}