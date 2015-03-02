package com.naddiaz.tfg.managermodule;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by nad on 2/02/15.
 */
public class streamToString{

    InputStream in;

    public streamToString(InputStream in) {
        this.in = in;
    }

    public String convert() {
        BufferedReader buffer = new BufferedReader(new InputStreamReader(in));
        StringBuilder str = new StringBuilder();
        String line;

        try {
            while((line = buffer.readLine())!=null)
            {
                String t=line+"\n";
                str.append(t);
            }
            buffer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str.toString();
    }
}
