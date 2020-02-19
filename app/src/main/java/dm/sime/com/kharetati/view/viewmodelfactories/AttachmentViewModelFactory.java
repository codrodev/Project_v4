package dm.sime.com.kharetati.view.viewmodelfactories;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import dm.sime.com.kharetati.datas.repositories.AttachmentRepository;

public class AttachmentViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private AttachmentRepository repository;
    private Activity appContext;

    public AttachmentViewModelFactory(Activity context,AttachmentRepository repository){
        this.repository = repository;
        this.appContext = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {

        return (T) new dm.sime.com.kharetati.view.viewModels.AttachmentViewModel(appContext,repository);
    }
}
