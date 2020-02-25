package dm.sime.com.kharetati.view.viewmodelfactories;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import dm.sime.com.kharetati.datas.repositories.MainRepository;
import dm.sime.com.kharetati.datas.repositories.MapRepository;

public class MainViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private MainRepository repository;
    private Activity appContext;

    public MainViewModelFactory(Activity context,MainRepository repository){
        this.repository = repository;
        this.appContext = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {

        return (T) new dm.sime.com.kharetati.view.viewModels.MainViewModel(appContext,repository);
    }
}
