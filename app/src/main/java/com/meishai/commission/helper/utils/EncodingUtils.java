package com.meishai.commission.helper.utils;

import android.util.Base64;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * 作者：yl
 * 时间: 2017/8/8 15:08
 * 功能：用于编解密数据的工具类,其内部维护了一个一次性的map集合把(每获取一次对象就会把前面的对象清空),我们只需要把参数put进来内部就能够把他转换成json格式的字符串,并进行编解码
 *
 * 使用:
 *  EncodingUtils.initInstance()//获取使用一次的实例对象
 *  .put("phone", "").put("type", "1")//设置参数
 *  .getBase64();//base64加密
 *  
 *
 */
public class EncodingUtils {
    private static EncodingUtils mInstance;
    public static EncodingUtils initInstance(){
        if(mInstance == null) {
            mInstance = new EncodingUtils();
        }else {
            mInstance.clear();
        }
        return mInstance;
    }

    private Map<String,Object> mMap;

    public EncodingUtils(){
        mMap = new HashMap<>();
    }
    public EncodingUtils(Map<String,Object> map){
        mMap = map;
    }

    public EncodingUtils put(String key, Object value){
        mMap.put(key,value);
        return this;
    }

    public EncodingUtils clear(){
        mMap.clear();
        return this;
    }

    public Map<String,Object> getMap(){
        return mMap;
    }




    /**
     * 把map集合转换成json数据
     * @return
     */
    public String params2Json(){

        Set<String> ks = mMap.keySet();
        Iterator<String> iterator = ks.iterator();
        StringBuilder json = new StringBuilder();
        json.append("{");
        while (iterator.hasNext()){
            String key = iterator.next();
            Object value = mMap.get(key);
            if(value instanceof String && ((String) value).contains("\n")){
                value = ((String) value).replaceAll("\\n"," ");
            }
            if(value instanceof String) {
                json.append("\"").append(key).append("\"").append(":")
                        .append("\"").append(value == null ? "" : value.toString()).append("\"").append(",");
            }else {
                json.append("\"").append(key).append("\"").append(":")
                        .append(value == null ? "" : value.toString()).append(",");
            }
        }
        json.deleteCharAt(json.length()-1);
        json.append("}");

        return json.toString();
    }

    // base64加密
    public String getBase64() {
        String str = params2Json();
        Log.w("EncodingUtils",str);
        String result = "";
        if( str != null) {
            try {
                result = new String(Base64.encode(str.getBytes("utf-8"), Base64.NO_WRAP),"utf-8");

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return result;
    }




}
