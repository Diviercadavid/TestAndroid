package com.example.myapplication.helper;

import android.content.Context;
import com.example.myapplication.model.RequestResponse;

import java.io.*;

public class InternalStorage {
    private InternalStorage() {}

    public static void writeObject(Context context, String key, RequestResponse object) throws IOException {
        FileOutputStream fos = context.openFileOutput(key, Context.MODE_PRIVATE);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(object);
        oos.close();
        fos.close();
    }

    public static RequestResponse readObject(Context context, String key) throws IOException,
            ClassNotFoundException {
        FileInputStream fis = context.openFileInput(key);
        ObjectInputStream ois = new ObjectInputStream(fis);
        RequestResponse object = (RequestResponse) ois.readObject();
        return object;
    }
}
