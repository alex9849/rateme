package de.hskl.rateme.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Objects;

public class EscherPlzValidator {
    public static boolean validateCityAndPlz(String plz, String city) {
        try {
            final String finalCity = city.toLowerCase();
            URL url = new URL("http://escher.informatik.hs-kl.de/PlzService/ort?plz=" + plz);
            URLConnection connection = url.openConnection();
            connection.setRequestProperty("Accept", "application/json");
            Gson gson = new Gson();
            TypeToken<List<EscherResponse>> token = new TypeToken<List<EscherResponse>>(){};
            List<EscherResponse> er = gson.fromJson(new JsonReader(new InputStreamReader(connection.getInputStream())), token.getType());
            if(er == null)
                return false;
            return er.stream().anyMatch(x -> Objects.equals(x.getOrt().toLowerCase(), finalCity));
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            System.out.println("Could not contact Escher PLZ-service!");
            e.printStackTrace();
            return true;
        }
    }

    private static class EscherResponse {
        private String plz;
        private String ort;

        public String getPlz() {
            return plz;
        }

        public void setPlz(String plz) {
            this.plz = plz;
        }

        public String getOrt() {
            return ort;
        }

        public void setOrt(String ort) {
            this.ort = ort;
        }
    }
}
