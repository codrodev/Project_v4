package dm.sime.com.kharetati.datas.repositories;

import dm.sime.com.kharetati.datas.network.MyApiService;

public class MainRepository {
    private MyApiService api;

    public MainRepository(MyApiService apiService){
        this.api = apiService;
    }

}
