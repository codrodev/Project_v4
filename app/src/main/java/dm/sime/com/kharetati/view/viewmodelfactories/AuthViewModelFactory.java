package dm.sime.com.kharetati.view.viewmodelfactories;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import dm.sime.com.kharetati.datas.repositories.UserRepository;

public class AuthViewModelFactory extends ViewModelProvider.NewInstanceFactory{

    private UserRepository repository;
    private Activity appContext;

    public AuthViewModelFactory(Activity context,UserRepository repository){
       this.repository = repository;
       this.appContext = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {

        return (T) new dm.sime.com.kharetati.view.viewModels.LoginViewModel(appContext,repository);
    }
}

