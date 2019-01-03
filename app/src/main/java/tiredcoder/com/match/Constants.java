package tiredcoder.com.match;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class Constants {
    public static final String M_ID = "Yedivi66781622325372"; //Paytm Merchand Id we got it in paytm credentials
    public static final String CHANNEL_ID = "WAP"; //Paytm Channel Id, got it in paytm credentials
    public static final String INDUSTRY_TYPE_ID = "Retail"; //Paytm industry type got it in paytm credential
    public  static final String ip="http://atleticoarena.com/";
//   public  static final String ip="http://192.168.1.102/";
    public static final String WEBSITE = "APPSTAGING";
    public static final String CALLBACK_URL = "https://securegw-stage.paytm.in/theia/paytmCallback?ORDER_ID=";
    public static boolean checknet(Context context)
    {
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        boolean x= activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if(!x)
        {
            Toast.makeText(context,"No internet connection",Toast.LENGTH_SHORT).show();

        }
    return x;
    }
}


