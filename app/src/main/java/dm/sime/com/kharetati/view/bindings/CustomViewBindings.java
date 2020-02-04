package dm.sime.com.kharetati.view.bindings;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import dm.sime.com.kharetati.R;

public class CustomViewBindings {
    @BindingAdapter("setVerticalAdapter")
    public static void bindRecyclerViewAdapter(RecyclerView recyclerView, RecyclerView.Adapter<?> adapter) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(adapter);
    }

    @BindingAdapter("setHorizontalAdapter")
    public static void bindHorizontalRecyclerViewAdapter(RecyclerView recyclerView, RecyclerView.Adapter<?> adapter) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), RecyclerView.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);
    }

    @BindingAdapter("setStaggeredAdapter")
    public static void bindStaggeredRecyclerViewAdapter(RecyclerView recyclerView, RecyclerView.Adapter<?> adapter) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);
    }

    @BindingAdapter("imageUrl")
    public static void bindRecyclerViewAdapterWithImage(ImageView imageView, String imageUrl) {
        /*if (imageUrl != null) {
            // If we don't do this, you'll see the old image appear briefly
            // before it's replaced with the current image
            if (imageView.getTag(R.id.image_url) == null || !imageView.getTag(R.id.image_url).equals(imageUrl)) {
                imageView.setImageBitmap(null);
                imageView.setTag(R.id.image_url, imageUrl);
                Glide.with(imageView).load(imageUrl).into(imageView);
            }
        } else {
            imageView.setTag(R.id.image_url, null);
            imageView.setImageBitmap(null);
        }*/
    }
}
