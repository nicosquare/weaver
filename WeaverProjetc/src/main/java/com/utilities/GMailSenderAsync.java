package com.utilities;

import android.os.AsyncTask;

/**
 * Created by madog on 26/01/16.
 */
public class GMailSenderAsync extends AsyncTask<String, Void, Void>
{

    @Override
    protected Void doInBackground(String... params)
    {
        GMailSender emailSender = new GMailSender("nicolas@weavercol.com","Jackhammer9011");
        try
        {
            emailSender.sendMail("Peticion: "+params[0], params[1],"contacto@weavercol.com", params[2]);
        }
        catch (Exception e1)
        {
            e1.printStackTrace();
        }
        return null;
    }
}