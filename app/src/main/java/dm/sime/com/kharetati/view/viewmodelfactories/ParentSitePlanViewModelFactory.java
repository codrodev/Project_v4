package dm.sime.com.kharetati.view.viewmodelfactories;



import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import dm.sime.com.kharetati.datas.repositories.ParentSitePlanRepository;
import dm.sime.com.kharetati.view.viewModels.ParentSiteplanViewModel;

public class ParentSitePlanViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private ParentSitePlanRepository repository;
    private Activity appContext;

    public ParentSitePlanViewModelFactory(Activity context, ParentSitePlanRepository repository){
        this.repository = repository;
        this.appContext = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {

        return (T) new ParentSiteplanViewModel(appContext,repository);
    }
}
