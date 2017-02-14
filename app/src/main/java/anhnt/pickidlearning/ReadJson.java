package anhnt.pickidlearning;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import anhnt.pickidlearning.models.Category;
import anhnt.pickidlearning.models.Item;

/**
 * Created by AnhNT on 1/25/2017.
 */

public class ReadJson {
    public static List<Category> readCategory(Context context) throws IOException, JSONException {
        String textJsonCategory = readText(context, R.raw.categories);
        List<Category> categories = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(textJsonCategory);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            int id = jsonObject.getInt("id");
            String name = jsonObject.getString("name");
            String path = jsonObject.getString("image");
            Category category = new Category(id, path, name);
            categories.add(category);
        }
        return categories;
    }

    public static List<Item> readItem(Context context) throws IOException, JSONException {
        List<Item> items = new ArrayList<>();
        String text = readText(context, R.raw.items);
        JSONArray jsonArray = new JSONArray(text);

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            int id = jsonObject.getInt("id");
            String image = jsonObject.getString("image");
            String name = jsonObject.getString("name");
            String sound = jsonObject.getString("sound");
            int categoryId = jsonObject.getInt("categoryId");
            Item item = new Item(id, image, name, sound, categoryId);
            items.add(item);
        }
        return items;
    }

    public static List<Category> getType(Context context) throws IOException, JSONException {
        List<Category> types = new ArrayList<>();
        String text = readText(context, R.raw.type);
        JSONArray jsonArray = new JSONArray(text);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            int id = jsonObject.getInt("id");
            String name = jsonObject.getString("name");
            String image = jsonObject.getString("image");
            types.add(new Category(id, image, name));
        }
        return types;
    }

    public static String readText(Context context, int resId) throws IOException {
        InputStream is = context.getResources().openRawResource(resId);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String s = null;
        while ((s = br.readLine()) != null) {
            sb.append(s);
            sb.append("\n");
        }
        return sb.toString();
    }
}
