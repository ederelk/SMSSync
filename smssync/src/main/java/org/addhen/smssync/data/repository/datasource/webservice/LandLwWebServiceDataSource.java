
package org.addhen.smssync.data.repository.datasource.webservice;


import org.addhen.smssync.data.database.WebServiceDatabaseHelper;
import org.addhen.smssync.data.entity.SyncUrl;
import org.addhen.smssync.data.entity.SyncScheme;

import android.support.annotation.NonNull;
import org.addhen.smssync.BuildConfig;
import org.json.JSONException;

import java.util.List;
import java.util.Arrays;
import rx.Observable;
import org.json.JSONObject;
/**
 * Retrieves and adds a webService data to the database
 *
 * @author Yaovi Kwasi
 */
public class LandLwWebServiceDataSource implements WebServiceDataSource {

    private final String LL_DEV_URL  = "http://192.168.0.110:1337/api.landlw/functions/sms.payment.confirmation.create";
    private final String LL_PROD_URL  = "http://togo.landlw.com/api-ll-TGO/functions/sms.payment.confirmation.create";


    private final SyncUrl LL_SYNC_URL;

    public LandLwWebServiceDataSource() {
        LL_SYNC_URL = initLandLwSyncUrl();
    }

    @Override
    public Observable<List<SyncUrl>> getWebServiceList() {
        return Observable.defer(() -> {
            return Observable.just(getLLSUrlList());
        });
    }

    @Override
    public Observable<SyncUrl> getWebService(Long webServiceId) {
        return Observable.defer(() -> {
            return Observable.just(LL_SYNC_URL);
        });
    }

    @Override
    public Observable<List<SyncUrl>> getByStatus(SyncUrl.Status status) {
        return Observable.defer(() -> {
            return Observable.just(getLLSUrlList());
        });
    }

    @Override
    public List<SyncUrl> syncGetByStatus(SyncUrl.Status status) {
        return getLLSUrlList();
    }

    @Override
    public Observable<Long> addWebService(SyncUrl syncUrl) {
        return Observable.defer(() -> {
            return Observable.just(1l);
        });
    }

    @Override
    public Observable<Long> updateWebService(SyncUrl syncUrl) {
        return Observable.defer(() -> {
            return Observable.just(1l);
        });
    }

    @Override
    public Observable<Long> deleteWebService(Long webServiceId) {
        return Observable.defer(() -> {
            return Observable.just(1l);
        });
    }

    @Override
    public List<SyncUrl> get(SyncUrl.Status status) {
        return getLLSUrlList();
    }

    @Override
    public List<SyncUrl> listWebServices() {
        return getLLSUrlList();
    }

    private List<SyncUrl> getLLSUrlList(){
        return Arrays.asList(LL_SYNC_URL);
    }

    private String getLLURL(){
        if(BuildConfig.DEBUG){
            return LL_DEV_URL;
        }else{
            return LL_PROD_URL;
        }
    }
    private String getLLJSONSyncScheme(){
        try{
            JSONObject lljson = new JSONObject();
            lljson.put("method", "POST");
            lljson.put("dataFormat", "JSON");
            lljson.put("kSecret", "secret");
            lljson.put("kFrom", "from");
            lljson.put("kSentTimestamp", "sentTimestamp");
            lljson.put("kMessage", "message");
            lljson.put("kSentTo", "sentTo");
            lljson.put("kMessageID", "messageId");
            lljson.put("kDeviceID", "deviceId");

            return lljson.toString();
        }catch(JSONException ex){
            throw new RuntimeException(ex.toString());
        }
    }

    private SyncScheme getLLSyncScheme(){

        return new SyncScheme(getLLJSONSyncScheme());

    }

    private  SyncUrl initLandLwSyncUrl(){

            SyncUrl llSyncUrl = new SyncUrl();
            llSyncUrl._id = 123l;
            llSyncUrl.setSecret("Togo-LandLw-SMS-payment");
            llSyncUrl.setTitle("LandLw");
            llSyncUrl.setUrl(getLLURL());
            // llSyncUrl.setKeywords(webServiceEntity.getKeywords());
            llSyncUrl.setStatus(SyncUrl.Status.ENABLED);
            llSyncUrl.setSyncScheme(getLLSyncScheme());
            return llSyncUrl;

    }

}
