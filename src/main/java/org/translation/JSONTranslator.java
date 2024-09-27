package org.translation;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * An implementation of the Translator interface which reads in the translation
 * data from a JSON file. The data is read in once each time an instance of this class is constructed.
 */
public class JSONTranslator implements Translator {

    private final Map<String, JSONObject> jmap = new HashMap<String, JSONObject>();
    private final Map<String, Map<String, String>> codeToTranslation =
            new HashMap<String, Map<String, String>>();
    /**
     * Constructs a JSONTranslator using data from the sample.json resources file.
     */

    public JSONTranslator() {
        this("sample.json");
    }

    /**
     * Constructs a JSONTranslator populated using data from the specified resources file.
     * @param filename the name of the file in resources to load the data from
     * @throws RuntimeException if the resource file can't be loaded properly
     */
    public JSONTranslator(String filename) {
        // read the file to get the data to populate things...
        try {

            String jsonString = Files.readString(Paths.get(getClass().getClassLoader().getResource(filename).toURI()));

            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject a = jsonArray.getJSONObject(i);
                String alpha3 = "alpha3";
                jmap.put(a.getString(alpha3), a);
                Map<String, String> codeLan = new HashMap<String, String>();
                for (String key: a.keySet()) {
                    if (!"id".equals(key) && !"alpha2".equals(key) && !key.equals(alpha3)) {
                        codeLan.put(key, a.getString(key));
                    }
                }
                codeToTranslation.put(a.getString(alpha3), codeLan);
            }

        }
        catch (IOException | URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<String> getCountryLanguages(String country) {
        Map<String, String> coun = codeToTranslation.get(country);
        return new ArrayList<String>(coun.keySet());
    }

    @Override
    public List<String> getCountries() {

        return new ArrayList<String>(jmap.keySet());
    }

    @Override
    public String translate(String country, String language) {
        return jmap.get(country).getString(language);
    }
}
