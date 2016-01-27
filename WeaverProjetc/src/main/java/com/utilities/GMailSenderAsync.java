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
            emailSender.sendMail("Peticion creada.","Usuario: "+params[0]+" - Celular: "+params[1]+" - Detalle: "+params[2],"contacto@weavercol.com",params[3]);
        }
        catch (Exception e1)
        {
            e1.printStackTrace();
        }
        return null;
    }
}