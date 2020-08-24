package miituo.com.miituo.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class KeyChainStore {
    private SharedPreferences sharedpreferences;
    public KeyChainStore(Context context){
        sharedpreferences = context.getSharedPreferences("miituo", Context.MODE_PRIVATE);
    }

    public void storeValueForKey(String key, String value){
            SharedPreferences.Editor preferencia= sharedpreferences.edit();
//        AesBase64Wrapper a=new AesBase64Wrapper();
//        preferencia.putString(key,a.encryptAndEncode(value));
        preferencia.putString(key,value);
            preferencia.commit();
    }

    public String fetchValueForKey(String key){
        String value=sharedpreferences.getString(key,"");
//        AesBase64Wrapper a=new AesBase64Wrapper();
//        String decripted = a.decodeAndDecrypt(value);
//        return decripted;
        return value;
    }
}
