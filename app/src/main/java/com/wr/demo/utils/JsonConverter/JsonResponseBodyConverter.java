package com.wr.demo.utils.JsonConverter;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.wr.demo.utils.LogWritter;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * 自定义响应ResponseBody
 * @param <T>
 */

public class JsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {

    private Gson mGson;
    private TypeAdapter<T> mAdapter;

    public JsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.mGson = gson;
        this.mAdapter = adapter;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        //清除错误的字段
        StringBuffer sb = new StringBuffer(value.string());
        String jsons = sb.substring(0, sb.length());
        //这部分代码参考GsonConverterFactory中GsonResponseBodyConverter<T>的源码对json的处理
        LogWritter.LogStr(jsons);
        Reader reader = StringToReader(jsons);
        JsonReader jsonReader = mGson.newJsonReader(reader);
        try {
            return mAdapter.read(jsonReader);
        } finally {
            value.close();
        }
    }

    /**
     * String转Reader
     * @param json
     * @return
     */
    private Reader StringToReader(String json){
        Reader reader  = new StringReader(json);
        return reader;
    }
}
