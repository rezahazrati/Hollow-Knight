package com.reza.hollowknight.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.reza.hollowknight.model.SaveProfile;

public class JsonHelper {
    private static final Json json = new Json();

    static {
        json.setOutputType(JsonWriter.OutputType.json);
    }

    public static void save(String fileName, SaveProfile profile) {
        FileHandle file = Gdx.files.local("saves/" + fileName);
        file.writeString(json.prettyPrint(profile), false);
    }

    public static SaveProfile load(String fileName) {
        FileHandle file = Gdx.files.local("saves/" + fileName);
        if (!file.exists()) {
            return null;
        }
        return json.fromJson(SaveProfile.class, file.readString());
    }
}
