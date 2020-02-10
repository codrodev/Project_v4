package dm.sime.com.kharetati.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import dm.sime.com.kharetati.BR;
import dm.sime.com.kharetati.datas.models.ZZBookmark;
import dm.sime.com.kharetati.view.viewModels.BookmarkViewModel;

public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.GenericViewHolder> {
    private int layoutId;
    private BookmarkViewModel viewModel;
    static Context context;
    List<ZZBookmark> lstBookmark;

    public BookmarkAdapter(@LayoutRes int layoutId, BookmarkViewModel viewModel,Context context) {
        this.layoutId = layoutId;
        this.viewModel = viewModel;
        this.context = context;
        lstBookmark = new ArrayList<>();


    }

    private int getLayoutIdForPosition(int position) {
        return layoutId;
    }

    @NonNull
    @Override
    public GenericViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ViewDataBinding binding = DataBindingUtil.inflate(layoutInflater, viewType, parent, false);

        return new BookmarkAdapter.GenericViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull GenericViewHolder holder, int position) {
        holder.bind(viewModel, position);
    }

    @Override
    public int getItemViewType(int position) {
        return getLayoutIdForPosition(position);
    }

    @Override
    public int getItemCount() {
        return lstBookmark.size();
    }

    public void setBookmark(List<ZZBookmark> lstBookmark) {
        this.lstBookmark = lstBookmark;
    }

    public static class GenericViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        final ViewDataBinding binding;
        GenericViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(BookmarkViewModel viewModel, int position) {
            binding.setVariable(BR.adapterBookmarkVM, viewModel);
            binding.setVariable(BR.position, position);

            binding.executePendingBindings();
        }

        @Override
        public void onClick(View v) {

        }
    }
}