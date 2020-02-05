package dm.sime.com.kharetati.view.fragments;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import ae.dsg.happiness.*;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import java.util.Date;

import dm.sime.com.kharetati.R;
import dm.sime.com.kharetati.databinding.FragmentHappinessBinding;
import dm.sime.com.kharetati.utility.AlertDialogUtil;
import dm.sime.com.kharetati.utility.FontChangeCrawler;
import dm.sime.com.kharetati.utility.Global;
import dm.sime.com.kharetati.utility.constants.AppUrls;
import dm.sime.com.kharetati.view.viewModels.HappinessViewModel;

public class HappinessFragment extends Fragment {
    FragmentHappinessBinding binding;
    HappinessViewModel model;
    private View mRootView;

    enum TYPE{
        TRANSACTION,
        WITH_MICROAPP,
        WITHOUT_MICROAPP
    }

    private static final String SECRET = "E4917C5A1CCC0FA3";
    private static final String SERVICE_PROVIDER = "DM";
    private static final String CLIENT_ID = "dmbeatuser";
    private TYPE currentType = TYPE.TRANSACTION;
    private WebView webView = null;

    public static HappinessFragment newInstance(){
        HappinessFragment fragment = new HappinessFragment();
        return fragment;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        FontChangeCrawler fontChanger = new FontChangeCrawler(getActivity().getAssets(), "Dubai-Regular.ttf");
        fontChanger.replaceFonts((ViewGroup) this.getView());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = ViewModelProviders.of(this).get(HappinessViewModel.class);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_happiness, container, false);
        binding.setFragmentHappinessVM(model);
        mRootView = binding.getRoot();
        initializePage();
        return binding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initializePage() {
        binding.webHappiness.setNestedScrollingEnabled(true);
        webView =binding.webHappiness;
        if(!Global.isConnected(getContext())){
            AlertDialogUtil.errorAlertDialog(getString(R.string.lbl_warning), getString(R.string.internet_connection_problem1), getString(R.string.ok), getContext());
            getActivity().getSupportFragmentManager().popBackStack();
        }
        else{
            load(currentType);
        }
    }
    private void load(TYPE type) {
        currentType = type;


        String secret = SECRET;
        String serviceProvider = SERVICE_PROVIDER;
        String clientID = CLIENT_ID;

        VotingRequest request = new VotingRequest();
        User user = new User();
        if (type == TYPE.TRANSACTION) {
            Transaction transaction = new Transaction();
            transaction.setGessEnabled("true");
            transaction.setNotes("Kharetati Vote");
            transaction.setServiceDescription("Kharetati");
            transaction.setChannel("SMARTAPP");
            transaction.setServiceCode("2952");
            transaction.setTransactionID("Happiness Vote " + new Date().getTime());
            request.setTransaction(transaction);
        } else {
            Application application = new Application("Kharetati", AppUrls.URL_RATE_US_EN, "SMART", "ANDROID");
            application.setNotes("Kharetati Vote");
            request.setApplication(application);
        }
        String timeStamp = Utils.getUTCDate();
        Header header = new Header();
        header.setTimeStamp(timeStamp);
        header.setServiceProvider(serviceProvider);
        header.setThemeColor("#00ff00");
        // Set MicroApp details
        if (type == TYPE.WITH_MICROAPP) {
            header.setMicroApp("Kharetati");
            header.setMicroAppDisplay("Kharetati App");
        }

        request.setHeader(header);
        request.setUser(user);

        /**
         *This is QA URL. Replace it with production once it is ready for production.
         */
        VotingManager.setHappinessUrl("https://happinessmeterqa.dubai.gov.ae/HappinessMeter2/MobilePostDataService");  //staging
        // VotingManager.setHappinessUrl("https://happinessmeter.dubai.gov.ae/HappinessMeter2/MobilePostDataService");//production

        //For arabic pass lang "ar"
        String lang;
        if (Global.CURRENT_LOCALE.equals("ar")) {
            lang = "ar";
        } else {
            lang = "en";
        }
        VotingManager.loadHappiness(webView, request, secret, serviceProvider, clientID, lang);
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
