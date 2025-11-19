package BQLOTWjson;

import java.io.FileReader;

public class JSONReadBQLOTW {
    public static void main(String[] args) {
        try(FileReader reader = new FileReader("BQLOTW_orarend.json")) {
            JSONPareser jsonPareser = new JSONParser();
            JSONObject jsonObject = (JSONObject)jsonParser.parse(reader);

            JSONObject root = (JSONObject) jsonObject.get("BQLOTW_orarend");
            JSONArray lessons = (JSONArray) root.get("ora");

            System.out.println("BQLOTW Órarend 2025 ősz:\n");

            for(int i=0; i<lessons.size(); i++)
            {
                JSONObject lesson = (JSONObject) lessons.get(i);
                JSONObject time = (JSONObject) lesson.get("idopont");
                System.out.println("Tárgy: " + lesson.get("targy"));
                System.out.println("Időpont: " + time.get("nap") + " " + time.get("tol") + "-" + time.get("ig"));
                System.out.println("Helyszín: " + lesson.get("helyszin"));
                System.out.println("Oktató: ") + lesson.get("oktato"));
                System.out.println();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
