package com.chamojaya.androidaccelerometersensor;

import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class TargetActivity {
    public List<String> readFile(String fileName) {
        List<String> lines =new ArrayList<>();
        try {
            FileInputStream fileInputStream = openFileInput("File.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);

           BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuffer stringBuffer = new StringBuffer();
            String line="";
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line + "\n");
           }
            bufferedReader.close();
           fileInputStream.close();
//
            TextView displayText = null;
            displayText.setText(stringBuffer.toString());
       } catch (FileNotFoundException e) {
           e.printStackTrace();
       } catch (IOException e) {
           e.printStackTrace();
        }
        return lines;
    }
//
   private FileInputStream openFileInput(String s) {
       return null;
   }

    public void delete() {
        File dir = getFilesDir();
        File file = new File(dir, "file.txt");
        boolean deleted = file.delete();
    }

    private File getFilesDir() {
        return null;
    }
//
//    private File getFilesDir() {
//    }
//        File[] files = getFilesDir().listFiles();
//
//        for (File file : files) {
//            file.delete();
//        }
//    }


    
}