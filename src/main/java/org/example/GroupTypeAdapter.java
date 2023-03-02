package org.example;


import com.google.gson.*;
import ru.maipomogator.hibernate.entities.Group;
import ru.maipomogator.hibernate.enums.GroupType;

import java.lang.reflect.Type;

public class GroupTypeAdapter implements JsonSerializer<Group>, JsonDeserializer<Group> {

    @Override
    public JsonElement serialize(Group group, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", group.getId());
        jsonObject.addProperty("name", group.getName());
        jsonObject.addProperty("course", group.getCourse());
        jsonObject.addProperty("faculty", group.getFaculty());
        jsonObject.addProperty("type", group.getType().toString());
        // Add any other properties you want to serialize to the JsonObject
        return jsonObject;
    }

    @Override
    public Group deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String id = jsonObject.get("id").getAsString();
        String name = jsonObject.get("name").getAsString();
        int course =Integer.parseInt(String.valueOf(jsonObject.get("course")));
        int faculty = Integer.parseInt(String.valueOf(jsonObject.get("faculty")));
        String typeString = String.valueOf(jsonObject.get("type")).replaceAll("\"","");
        GroupType groupType = GroupType.valueOf(typeString);

        // Get any other properties you want to deserialize from the JsonObject
        return new Group(name, course, faculty,groupType);
    }
}

/*import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import ru.maipomogator.hibernate.entities.Group;
import ru.maipomogator.hibernate.enums.GroupType;

import java.io.IOException;


public class GroupTypeAdapter extends TypeAdapter<Group> {

    @Override
    public void write(JsonWriter jsonWriter, Group group) throws IOException {
        jsonWriter.beginObject();
        jsonWriter.name("id").value(group.getId());
        jsonWriter.name("name").value(group.getName());
        jsonWriter.name("course").value(group.getCourse());
        jsonWriter.name("faculty").value(group.getFaculty());
        jsonWriter.name("type").value(group.getType().toString());


    }

    @Override
    public Group read(JsonReader in) throws IOException {
        Group group = new Group();
        in.beginObject();
        while (in.hasNext()) {
            String name = in.nextName();
            switch (name) {
                case "id" -> group.setId(in.nextInt());
                case "name" -> group.setName(in.nextString());
                case "course" ->
                    // String typeStr = in.nextString();
                        group.setCourse(in.nextInt());
                case "faculty" ->
                    // String dayStr = in.nextString();
                        group.setFaculty(in.nextInt());
                case "type" -> {
                    String typeStr = in.nextString();
                    group.setType(GroupType.valueOf(typeStr));
                }
                default -> in.skipValue();
            }
        }
        in.endObject();
        return group;
    }
}*/
