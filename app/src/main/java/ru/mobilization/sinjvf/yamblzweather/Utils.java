package ru.mobilization.sinjvf.yamblzweather;

/**
 * Created by Sinjvf on 16.07.2017.
 * Some utils
 */

public class Utils {


    //if we have many inner classes and can catch the NullPointer Exception while do many get calls
    public static <T> T getDataWithoutException(GetData<T> getData){
        try {
            return getData.getData();
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        return null;
    }
    public interface GetData<T>{
        T getData();
    }
}
