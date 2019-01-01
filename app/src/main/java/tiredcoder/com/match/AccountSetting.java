package tiredcoder.com.match;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountSetting extends AppCompatActivity {
    EditText password,email,name;
    Button button;
    int flag=0;
    private static final String TAG = MainActivity.class.getSimpleName();
    private String selectedFilePath=null;
    private String SERVER_URL = Constants.ip+"myfiles/upload.php";
    SharedPreferences preferences;
    String fileName;
    SharedPreferences.Editor editor;

    Dialog dialog;
    private static final int PICK_FILE_REQUEST = 1;
    /**********  File Path *************/
    CircleImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        name=findViewById(R.id.changablename);
        password=findViewById(R.id.changablepassword);
        email=findViewById(R.id.changableemail);
        preferences   =getSharedPreferences("userinfo",MODE_PRIVATE);
        imageView=findViewById(R.id.changableimage);
        name.setText(preferences.getString("name",null));
        editor=getSharedPreferences("userinfo", MODE_PRIVATE).edit();
        fileName=preferences.getString("image",null);
        email.setText(preferences.getString("email",null));
        password.setText(preferences.getString("password",null));
        Picasso.get().load(Constants.ip+"Turf/img/"+preferences.getString("image",null)).into(imageView);
      //  new ImageLoader(Constants.ip+"Turf/img/"+preferences.getString("image",null),imageView).execute();
        button= findViewById(R.id.changesettings);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag==0) {
                    button.setText("Save");
                    name.setEnabled(true);
                    password.setEnabled(true);
                    email.setEnabled(true);
                    name.requestFocus();
                    flag=1;
                Drawable res=getResources().getDrawable(R.drawable.plus);
                    imageView.setImageDrawable(res);
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showFileChooser();


                        }
                    });

                }
                else {

                    Drawable res = getResources().getDrawable(R.drawable.placeholder);

                    Log.i("marjaname", name.getText().toString());
                    Log.i("marjafilename", "soja" + fileName);
                    //      new changeSettings(fileName,name.getText().toString(),preferences.getString("id",null),email.getText().toString(),preferences.getString("mobileno",null),password.getText().toString()).execute();
                    if (validate() == 1) {
                        editor.putString("name", name.getText().toString());
                        editor.putString("email", email.getText().toString());
                        editor.putString("password", password.getText().toString());
                        editor.putString("image", fileName);
//                        new ImageLoader("http://192.168.1.103/Turf/img/" + preferences.getString("image", null), imageView).execute();
                        editor.apply();
                        editor.commit();
                        new changeSettings(fileName, name.getText().toString(), email.getText().toString(), preferences.getString("id", null), preferences.getString("mobileno", null), password.getText().toString()).execute();



                        button.setText("Change");
                        name.setEnabled(false);
                        password.setEnabled(false);
                        email.setEnabled(false);
                        //imageView.setImageDrawable(res);
                        flag = 0;
                    }
                }
            }
        });
    }
    private void showFileChooser() {

        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

        startActivityForResult(chooserIntent, PICK_FILE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == PICK_FILE_REQUEST){
                if(data == null){
                    //no data present
                    return;
                }


                Uri selectedFileUri = data.getData();
                selectedFilePath = FilePath.getPath(this,selectedFileUri);
                File file=new File(selectedFilePath);
                String filename=file.getName();
                Log.i(TAG,"Selected File Path:" + selectedFilePath);

                if(selectedFilePath != null && !selectedFilePath.equals("")){
                    if (selectedFilePath != null) {
                        //    dialog=ProgressDialog.show(AccountSetting.this,"uploading file",true);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                uploadFile(selectedFilePath);

                            }
                        }).start();

                    }
                    else{
                        Toast.makeText(AccountSetting.this,"Please choose a File First",Toast.LENGTH_SHORT).show();
                    }
              //      Log.i("soja",selectedFilePath);
                }else{
                    Toast.makeText(this,"Cannot upload file to server",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    public int uploadFile(final String selectedFilePath){

        int serverResponseCode = 0;

        HttpURLConnection connection;
        DataOutputStream dataOutputStream;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";


        int bytesRead,bytesAvailable,bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File selectedFile = new File(selectedFilePath);

        String[] parts = selectedFilePath.split("/");
         fileName = parts[parts.length-1];

        if (!selectedFile.isFile()){
           // dialog.dismiss();


            return 0;
        }else{
            try{
                FileInputStream fileInputStream = new FileInputStream(selectedFile);
                URL url = new URL(SERVER_URL);
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);//Allow Inputs
                connection.setDoOutput(true);//Allow Outputs
                connection.setUseCaches(false);//Don't use a cached Copy
                connection.setRequestMethod("POST");
                Log.i("marja","marja");

                connection.setRequestProperty("Connection", "Keep-Alive");
                connection.setRequestProperty("ENCTYPE", "multipart/form-data");
                connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                connection.setRequestProperty("uploaded_file",selectedFilePath);

                //creating new dataoutputstream
                dataOutputStream = new DataOutputStream(connection.getOutputStream());

                //writing bytes to data outputstream
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                        + selectedFilePath + "\"" + lineEnd);

                dataOutputStream.writeBytes(lineEnd);

                //returns no. of bytes present in fileInputStream
                bytesAvailable = fileInputStream.available();
                //selecting the buffer size as minimum of available bytes or 1 MB
                bufferSize = Math.min(bytesAvailable,maxBufferSize);
                //setting the buffer as byte array of size of bufferSize
                buffer = new byte[bufferSize];

                //reads bytes from FileInputStream(from 0th index of buffer to buffersize)
                bytesRead = fileInputStream.read(buffer,0,bufferSize);

                //loop repeats till bytesRead = -1, i.e., no bytes are left to read
                while (bytesRead > 0){
                    //write the bytes read from inputstream
                    dataOutputStream.write(buffer,0,bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable,maxBufferSize);
                    bytesRead = fileInputStream.read(buffer,0,bufferSize);
                }

                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                serverResponseCode = connection.getResponseCode();
                String serverResponseMessage = connection.getResponseMessage();

                Log.i(TAG, "Server Response is: " + serverResponseMessage + ": " + serverResponseCode);
                if(serverResponseCode == 200){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(AccountSetting.this,"File Upload completed.\n\n You can see the uploaded file here: \n\n" + "http://coderefer.com/extras/uploads/"+ fileName,Toast.LENGTH_SHORT).show();
                        }
                    });
                }else
                {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(AccountSetting.this,"Nhi chal rha ye",Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                //response code of 200 indicates the server status OK
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = null;

                //closing the input and output streams
                fileInputStream.close();
                dataOutputStream.flush();
                dataOutputStream.close();
                Log.i("soja",fileName);
               new ImageLoader(Constants.ip+"Turf/img/"+fileName,imageView).execute();


            } catch (FileNotFoundException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(AccountSetting.this,"File Not Found",Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (MalformedURLException e) {
                e.printStackTrace();
                Toast.makeText(AccountSetting.this, "URL error!", Toast.LENGTH_SHORT).show();

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(AccountSetting.this, "Cannot Read/Write File!", Toast.LENGTH_SHORT).show();
            }
        //    dialog.dismiss();
            return serverResponseCode;
        }

    }
    int validate()
    {
        if(name.getText().toString().equals(""))
        {
            name.setError("Enter name");
            name.requestFocus();
            return -1;

        }
        else
        if(password.getText().toString().equals(""))
        {
            password.setError("Enter password");
            password.requestFocus();
            return -1;
        }
        else
            if (password.length()<8)
            {
                password.setError("Password length should be greater or equal to 8");
                password.requestFocus();
                return -1;
            }
            else
                if(!isValid(email.getText().toString()))
                {
                    email.setError("Enter valid email address");
                    email.requestFocus();
                    return -1;
                }
return 1;
    }
    public static boolean isValid(String email)
    {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }
    public  class changeSettings extends AsyncTask<String,String,String>
    {
        String image,name,email,id,number,pass;

        public changeSettings(String image, String name, String email, String id, String number,String pass) {
            this.image = image;
            this.name = name;
            this.email = email;
            this.id = id;
            this.number = number;
            this.pass=pass;
        }
        changeSettings()
        {

        }
       protected  void onPreExecute()
       {
       }
       @Override
       protected String doInBackground(String ... args)
       {
           try
           {
               String link=Constants.ip+"myfiles/changesettings.php";
               String data;
               data = URLEncoder.encode("imagename", "UTF-8") + "=" + URLEncoder.encode(image, "UTF-8");
               data += "&" + URLEncoder.encode("name", "UTF-8") + "=" +
                       URLEncoder.encode(name, "UTF-8");
               data += "&" + URLEncoder.encode("email", "UTF-8") + "=" +
                       URLEncoder.encode(email, "UTF-8");
               data += "&" + URLEncoder.encode("password", "UTF-8") + "=" +
                       URLEncoder.encode(pass, "UTF-8");
               data += "&" + URLEncoder.encode("id", "UTF-8") + "=" +
                       URLEncoder.encode(id, "UTF-8");
               data += "&" + URLEncoder.encode("number", "UTF-8") + "=" +
                       URLEncoder.encode(number, "UTF-8");
               URL url = new URL(link);
               URLConnection connection = url.openConnection();
               connection.setDoOutput(true);
               OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
               writer.write(data);
               writer.flush();
               Log.i("marjagirja","marja");

               BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
               StringBuilder sb = new StringBuilder();
               String line = null;
               while ((line = br.readLine()) != null) {
                   sb.append(line);
                   break;
               }
               Log.i("marjasoja",sb.toString());
           }
           catch (UnsupportedEncodingException e) {
               Log.i("marja","codingkanatak");
           } catch (MalformedURLException e) {
               Log.i("marja","url");
           } catch (IOException e) {
            Log.i("marja","ioexception");
               }

          return  "";
       }

       @Override
        protected  void onPostExecute(String result)
       {
           Toast.makeText(AccountSetting.this,"Changed settings successfully",Toast.LENGTH_SHORT).show();
       }


          }
}
