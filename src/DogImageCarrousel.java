import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import javax.swing.*;
import java.awt.*;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class DogImageCarrousel {
    public static final String BASE_URL = "https://dog.ceo/api/breeds/image/random/";
    public static final int IMAGE_DISPLAY_TIME = 15000; // 15 seconds
    public static final int MIN_IMAGES = 10;
    public static final int MAX_IMAGES = 20;

    public static void main(String[] args) {
        int numImages = getRandomNumber(MIN_IMAGES, MAX_IMAGES);
        String breed = JOptionPane.showInputDialog("¿Qué raza desea ver? (Deje en blanco para aleatorio)");

        ArrayList<String> imageUrls = getImageUrls(numImages, breed);

        if (imageUrls.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No se encontraron imágenes para la raza especificada.");
            System.exit(0);
        }

        JFrame frame = new JFrame("Dog Image Carousel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        JLabel imageLabel = new JLabel();
        frame.add(imageLabel, BorderLayout.CENTER);

        Timer timer = new Timer();
        int[] currentIndex = {0};

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> {
                    if (currentIndex[0] < numImages) {
                        String imageUrl = imageUrls.get(currentIndex[0]);
                        ImageIcon imageIcon = new ImageIcon(new ImageIcon(imageUrl).getImage());
                        imageLabel.setIcon(imageIcon);
                        currentIndex[0]++;
                    } else {
                        currentIndex[0] = 0;
                    }
                });
            }
        };

        timer.schedule(task, 0, IMAGE_DISPLAY_TIME);

        frame.setVisible(true);
    }

    public static int getRandomNumber(int min, int max) {
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }

    public static ArrayList<String> getImageUrls(int numImages, String breed) {
        ArrayList<String> imageUrls = new ArrayList<>();
        String apiUrl = BASE_URL + numImages;

        if (breed != null && !breed.isEmpty()) {
            apiUrl += "?breed=" + breed;
        }

        try {
            URL url = new URL(apiUrl);
            InputStreamReader reader = new InputStreamReader(url.openStream());
            JsonObject response = new Gson().fromJson(reader, JsonObject.class);

            if (response.has("message")) {
                JsonArray images = response.getAsJsonArray("message");

                for (JsonElement element : images) {
                    imageUrls.add(element.getAsString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return imageUrls;
    }
}

