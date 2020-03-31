package dm.sime.com.kharetati.utility;

import android.app.Activity;
import android.content.Context;

import java.io.IOException;

import dm.sime.com.kharetati.R;

public class Exceptions {
    public static class ApiException extends IOException{
        String message;

        public ApiException(String message){
            this.message =message;
        }

    }
    public static class NoInternetException extends IOException{
        String message;
        Context context;
        public NoInternetException(Context context,String message){
            this.message =message;
            this.context =context;
            Global.showNoInternetAlert(context,message);


        }

    }

}
