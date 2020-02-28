package dm.sime.com.kharetati.view.viewmodelfactories;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import dm.sime.com.kharetati.datas.repositories.ContactusRepository;

public class ContactusViewModelFactory extends ViewModelProvider.NewInstanceFactory{

private ContactusRepository repository;
private Activity appContext;

public ContactusViewModelFactory(Activity context,ContactusRepository repository){
        this.repository = repository;
        this.appContext = context;
        }

@NonNull
@Override
public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {

        return (T) new dm.sime.com.kharetati.view.viewModels.ContactusViewModel(appContext,repository);
        }
}
