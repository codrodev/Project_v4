package dm.sime.com.kharetati.view.viewmodelfactories;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import dm.sime.com.kharetati.datas.repositories.HomeRepository;

public class HomeViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private HomeRepository repository;
    private Activity appContext;

    public HomeViewModelFactory(Activity context,HomeRepository repository){
        this.repository = repository;
        this.appContext = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {

        return (T) new dm.sime.com.kharetati.view.viewModels.HomeViewModel(appContext,repository);
    }
}
