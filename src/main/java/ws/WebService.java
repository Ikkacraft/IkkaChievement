package ws;

import com.fasterxml.jackson.databind.ObjectMapper;
import core.PluginCore;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class WebService {
    private PluginCore core;
    private static String ikkachievement = "http://712900f6.ngrok.io/";

    public WebService(PluginCore core) {
        this.core = core;
    }


    public List<Badge> getBadges() throws IOException {
        URL url = new URL(ikkachievement + "badges");

        ObjectMapper mapper = new ObjectMapper();
        String output = getBody(url);
        List<Badge> navigation = mapper.readValue(
                output,
                mapper.getTypeFactory().constructCollectionType(
                        List.class, Badge.class));
        return navigation;
    }

    public String getUnlockUserBadges(String userUUID) throws IOException {
        String urlString = ikkachievement + "badges/user/" + userUUID + "/unlock";
        URL url = new URL(urlString);

        ObjectMapper mapper = new ObjectMapper();
        return getBody(url);
    }

    public String getUserBadges(String userUUID) throws IOException {
        String urlString = ikkachievement + "badges/user/" + userUUID;
        URL url = new URL(urlString);

        ObjectMapper mapper = new ObjectMapper();
        return getBody(url);
    }

    public void validAchievement(String userUUID, int badgeID) throws IOException {
        String body = "{\"user_id\" :\"" + userUUID + "\",\"badge_id\": " + badgeID +
                ",\"status\" : \"unlock\",\"remaining\" : 0}";
        put(body, new URL(ikkachievement + "badges/unlock"));
    }

    public void updateAchievement(String userUUID, int badgeID, int remaining) throws IOException {
        String body = "{\"user_id\" :\"" + userUUID + "\",\"badge_id\": " + badgeID +
                ",\"status\" : \"lock\",\"remaining\" : " + String.valueOf(remaining) + "}";
        put(body, new URL(ikkachievement + "badges/unlock"));
    }

    private void put(String body, URL url) throws IOException {
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        //String encoding = new sun.misc.BASE64Encoder().encode(userPassword.getBytes());
        String encoding = "MmNiMjEyYzctZTE5Mi00ZWMzLTg2YmMtMDVlOGVmM2EyODZmOjJkZGE5NjIzNDEzNDRiM2FhM2MwZWZiN2Y2ODNjZWZh";

        //add request header
        con.setRequestMethod("PUT");
        con.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        con.setRequestProperty( "Content-type", "application/json");
        con.setRequestProperty( "Accept", "*/*" );
        con.setRequestProperty("Authorization", "Basic " + encoding);

        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(body);
        wr.flush();
        wr.close();
        core.getLogger().info(String.valueOf(con.getResponseCode()));
    }

    private String getBody(URL url) throws IOException {
        BufferedReader in = new BufferedReader(
                new InputStreamReader(url.openStream()));

        String inputLine;
        String output = "";
        while ((inputLine = in.readLine()) != null)
            output += inputLine;
        in.close();
        return output;
    }
}
