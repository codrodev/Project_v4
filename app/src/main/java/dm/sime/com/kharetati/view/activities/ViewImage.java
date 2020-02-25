package dm.sime.com.kharetati.view.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import dm.sime.com.kharetati.R;
import dm.sime.com.kharetati.utility.FontChangeCrawler;
import dm.sime.com.kharetati.utility.Global;
import dm.sime.com.kharetati.view.fragments.AttachmentFragment;


public class ViewImage extends AppCompatActivity {

    ImageView imageView;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_fragment);
        FontChangeCrawler fontChanger = new FontChangeCrawler(getAssets(), "Dubai-Regular.ttf");
        fontChanger.replaceFonts((ViewGroup)this.findViewById(android.R.id.content));
        ImageView back=(ImageView) findViewById(R.id.backicon_view);
        ImageView forward=(ImageView) findViewById(R.id.forwardicon_view);
        forward.setVisibility(View.GONE);
        //ImageView back=(ImageView)findViewById(R.id.backicon);
        ImageView imageView=(ImageView) findViewById(R.id.viewImage);
         String imgPath = getIntent().getStringExtra("bitmap");
        Bitmap bitmap = BitmapFactory.decodeFile(imgPath);
        if(Global.CURRENT_LOCALE.equals("en")){

            back.setRotationY(0);

        }
        else{

            back.setVisibility(View.GONE);
            forward.setRotation(180);
            forward.setVisibility(View.VISIBLE);

        }
        if(bitmap!=null)
            imageView.setImageBitmap(bitmap);
        else Toast.makeText(this, "NULL", Toast.LENGTH_SHORT).show();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

       /* back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });*/

    }
}
