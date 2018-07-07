package com.example.rohit.management;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class Webservices {

    String login_url = "http://classsearch.esy.es/management_webservices.php";

    public String addNewOrder(String webserviceName,String billNo,String billDate,String customerName,String typeOfMurti,String totalAmt,String balanceAmt,String advanceAmt,String contactNo,String status ){

        try {
            URL url = new URL(login_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String data =
                    URLEncoder.encode("action", "UTF-8") + "=" + URLEncoder.encode(webserviceName, "UTF-8") + "&" +
                            URLEncoder.encode("billNo", "UTF-8") + "=" + URLEncoder.encode(billNo, "UTF-8") + "&" +
                            URLEncoder.encode("billDate", "UTF-8") + "=" + URLEncoder.encode(billDate, "UTF-8") + "&" +
                            URLEncoder.encode("customerName", "UTF-8") + "=" + URLEncoder.encode(customerName, "UTF-8") +"&" +
                            URLEncoder.encode("typeOfMurti", "UTF-8") + "=" + URLEncoder.encode(typeOfMurti, "UTF-8")+ "&" +
                            URLEncoder.encode("totalAmt", "UTF-8") + "=" + URLEncoder.encode(totalAmt, "UTF-8")+ "&" +
                            URLEncoder.encode("balanceAmt", "UTF-8") + "=" + URLEncoder.encode(balanceAmt, "UTF-8") +"&" +
                            URLEncoder.encode("advanceAmt", "UTF-8") + "=" + URLEncoder.encode(advanceAmt, "UTF-8") +"&" +
                            URLEncoder.encode("contactNo", "UTF-8") + "=" + URLEncoder.encode(contactNo, "UTF-8")  +"&" +
                            URLEncoder.encode("status", "UTF-8") + "=" + URLEncoder.encode(status, "UTF-8") ; /*+"&" +*/
//                           URLEncoder.encode("add_time", "UTF-8") + "=" + URLEncoder.encode(params[10], "UTF-8")  ;

            bufferedWriter.write(data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();
            outputStream.close();

            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
            String response = "";
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                response = response + line;
            }
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();
            return response;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public String updateOrder(String webserviceName,String billNo,String billDate,String customerName,String typeOfMurti,String totalAmt,String balanceAmt,String advanceAmt,String contactNo,String status,String imgBase64 ){

        try {
            URL url = new URL(login_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String data =
                    URLEncoder.encode("action", "UTF-8") + "=" + URLEncoder.encode(webserviceName, "UTF-8") + "&" +
                            URLEncoder.encode("billNo", "UTF-8") + "=" + URLEncoder.encode(billNo, "UTF-8") + "&" +
                            URLEncoder.encode("billDate", "UTF-8") + "=" + URLEncoder.encode(billDate, "UTF-8") + "&" +
                            URLEncoder.encode("customerName", "UTF-8") + "=" + URLEncoder.encode(customerName, "UTF-8") +"&" +
                            URLEncoder.encode("typeOfMurti", "UTF-8") + "=" + URLEncoder.encode(typeOfMurti, "UTF-8")+ "&" +
                            URLEncoder.encode("totalAmt", "UTF-8") + "=" + URLEncoder.encode(totalAmt, "UTF-8")+ "&" +
                            URLEncoder.encode("balanceAmt", "UTF-8") + "=" + URLEncoder.encode(balanceAmt, "UTF-8") +"&" +
                            URLEncoder.encode("advanceAmt", "UTF-8") + "=" + URLEncoder.encode(advanceAmt, "UTF-8") +"&" +
                            URLEncoder.encode("contactNo", "UTF-8") + "=" + URLEncoder.encode(contactNo, "UTF-8")  +"&" +
                            URLEncoder.encode("status", "UTF-8") + "=" + URLEncoder.encode(status, "UTF-8")  +"&" +
                            URLEncoder.encode("imgBase64", "UTF-8") + "=" + URLEncoder.encode(imgBase64, "UTF-8")  ;

            bufferedWriter.write(data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();
            outputStream.close();

            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
            String response = "";
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                response = response + line;
            }
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();
            return response;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public String getAllOrder(String webserviceName ){

        try {
            URL url = new URL(login_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String data =
                    URLEncoder.encode("action", "UTF-8") + "=" + URLEncoder.encode(webserviceName, "UTF-8") ;

            bufferedWriter.write(data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();
            outputStream.close();

            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
            String response = "";
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                response = response + line;
            }
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();
            return response;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
