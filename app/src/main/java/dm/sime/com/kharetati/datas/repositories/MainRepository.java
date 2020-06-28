package dm.sime.com.kharetati.datas.repositories;

import dm.sime.com.kharetati.datas.models.NotificationResponse;
import dm.sime.com.kharetati.datas.models.SerializeGetAppRequestModel;
import dm.sime.com.kharetati.datas.network.MyApiService;
import io.reactivex.Completable;
import io.reactivex.Observable;

public class MainRepository {
    private MyApiService api;

    public MainRepository(MyApiService apiService){
        this.api = apiService;
    }

    public Observable<NotificationResponse> getAppNotifications(String url, SerializeGetAppRequestModel model) {
        return api.getAppNotifications(url,model);
    }
}
