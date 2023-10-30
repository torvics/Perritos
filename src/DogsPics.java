import com.google.gson.Gson;
import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class DogsPics {
    public static final String BASE_URL = "https://dog.ceo/api/breed/";
    public static final String URLRandom = "https://dog.ceo/api/breeds/image/random/";

    public static void main(String[] args) {
        int n = 3;
        String breed = JOptionPane.showInputDialog("¿Qué raza quiere ver?");

        DogsApiResponse response = query(n, breed);

        ArrayList<String> urls = response.getMessage();
        for (String u : urls) {
            System.out.println(u);
            EventQueue.invokeLater(() -> {
                JFrame ex = new ImageView(u);
                ex.setVisible(true);

                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            });
        }
    }

    public static DogsApiResponse query(int n, String breed) {
        DogsApiResponse response = null;
        String urlString = null;

        if (breed == null || breed.trim().isEmpty()) {
            urlString = URLRandom + n;
        } else {
            urlString = BASE_URL + breed + "/images/random/" + n;
        }

        try {
            URL u = new URL(urlString);

            BufferedReader in = new BufferedReader(new InputStreamReader(u.openStream()));
            String buffer;
            StringBuilder jsonText = new StringBuilder();
            while ((buffer = in.readLine()) != null) {
                jsonText.append(buffer);
            }
            in.close();

            if (jsonText.toString().startsWith("{")) {
                Gson gson = new Gson();
                response = gson.fromJson(jsonText.toString(), DogsApiResponse.class);
            } else {
                response = new DogsApiResponse();
                ArrayList<String> urls = new ArrayList<>();
                urls.add(jsonText.toString());
                response.setMessage(urls);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return response;
    }
}
