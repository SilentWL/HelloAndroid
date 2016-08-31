package json;

import android.util.JsonReader;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by Administrator on 2016/4/26 0026.
 */
public class Jsonutils {
    public void ParseJson(String jsonData){
        try {
            JsonReader reader = new JsonReader(new StringReader(jsonData));

            reader.beginArray();

            while (reader.hasNext()){
                reader.beginObject();
                while (reader.hasNext()) {
                    String tagName = reader.nextName();
                    if (tagName.equals("name")) {
                        Log.w("Jsonutils", "name=" + reader.nextString());
                    } else if (tagName.equals("age")) {
                        Log.w("Jsonutils", "age=" + reader.nextInt());
                    }
                }
                reader.endObject();
            }
            reader.endArray();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void ParseUserJson(String jsonData){
        Gson gson = new Gson();

        Type listType = new TypeToken<LinkedList<User>>(){}.getType();
        LinkedList<User> users = gson.fromJson(jsonData, listType);

        for (Iterator iterator = users.iterator();iterator.hasNext();) {
            User user = (User)iterator.next();
            Log.w("Jsonutils", "name=" + user.getName());
            Log.w("Jsonutils", "age=" + user.getAge());
        }
    }
}
