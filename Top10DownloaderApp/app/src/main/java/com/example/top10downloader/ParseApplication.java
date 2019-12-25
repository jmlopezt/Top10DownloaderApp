package com.example.top10downloader;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ParseApplication {
    private String xmlData;
    private ArrayList<Application> applications;

    public ParseApplication(String xmlData) {
        this.xmlData = xmlData;
        applications = new ArrayList<Application>();
    }

    public ArrayList<Application> getApplications() {
        return applications;
    }

    public boolean process(){
        boolean status = true;
        Application currentRecord = null;
        boolean inEntry = false;
        String textValue = "";

        try {

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new StringReader(this.xmlData));
            int evenType = xpp.getEventType();

            while (evenType != XmlPullParser.END_DOCUMENT){
                String tagName = xpp.getName();
                switch (evenType) {
                    case XmlPullParser.START_TAG:
                        //Log.d("ParseApplication", "Starting tag for " + tagName);
                        if (tagName.equalsIgnoreCase("entry")){
                            inEntry = true;
                            currentRecord = new Application();
                        }
                        break;
                    case XmlPullParser.TEXT:
                        textValue = xpp.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        //Log.d("ParseApplication", "Ending tag for " + tagName);
                        if (inEntry){
                            if (tagName.equalsIgnoreCase("entry")){
                                applications.add(currentRecord);
                                inEntry = false;
                            } else if (tagName.equalsIgnoreCase("name")){
                                currentRecord.setName(textValue);
                            } else if (tagName.equalsIgnoreCase("artist")){
                                currentRecord.setArtist(textValue);
                            } else if (tagName.equalsIgnoreCase("releaseDate")){
                                textValue = getDate(textValue);
                                currentRecord.setReleaseDate(textValue);
                            }

                        }
                        break;

                    default:
                            // Nothing to do
                }
                evenType = xpp.next();
            }
            status = true;

        } catch (Exception e){
            status = false;
            e.printStackTrace();
        }

        return status;
    }

    private String getDate(String date){
        String dateTime = null;
        try {
            dateTime = date.substring(0,10);
              Log.d("ParseApplication","Date is " + dateTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateTime;
    }


}
