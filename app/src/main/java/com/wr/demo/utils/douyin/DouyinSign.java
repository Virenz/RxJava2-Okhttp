package com.wr.demo.utils.douyin;

import android.text.TextUtils;

import com.wr.demo.config.Config;
import com.wr.demo.config.Constants;
import com.wr.demo.utils.LogWritter;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DouyinSign {

    private static final String BYTETABLE = "D6 28 3B 71 70 76 BE 1B A4 FE 19 57 5E 6C BC 21 B2 14 37 7D 8C A2 FA 67 55 6A 95 E3 FA 67 78 ED 8E 55 33 89 A8 CE 36 B3 5C D6 B2 6F 96 C4 34 B9 6A EC 34 95 C4 FA 72 FF B8 42 8D FB EC 70 F0 85 46 D8 B2 A1 E0 CE AE 4B 7D AE A4 87 CE E3 AC 51 55 C4 36 AD FC C4 EA 97 70 6A 85 37 6A C8 68 FA FE B0 33 B9 67 7E CE E3 CC 86 D6 9F 76 74 89 E9 DA 9C 78 C5 95 AA B0 34 B3 F2 7D B2 A2 ED E0 B5 B6 88 95 D1 51 D6 9E 7D D1 C8 F9 B7 70 CC 9C B6 92 C5 FA DD 9F 28 DA C7 E0 CA 95 B2 DA 34 97 CE 74 FA 37 E9 7D C4 A2 37 FB FA F1 CF AA 89 7D 55 AE 87 BC F5 E9 6A C4 68 C7 FA 76 85 14 D0 D0 E5 CE FF 19 D6 E5 D6 CC F1 F4 6C E9 E7 89 B2 B7 AE 28 89 BE 5E DC 87 6C F7 51 F2 67 78 AE B3 4B A2 B3 21 3B 55 F8 B3 76 B2 CF B3 B3 FF B3 5E 71 7D FA FC FF A8 7D FE D8 9C 1B C4 6A F9 88 B5 E5";
    private static final String NULL_MD5_STRING = "00000000000000000000000000000000";

    public static byte[] StrToByte(String str) {
        String str2 = str;
        Object[] objArr = new Object[1];
        int i = 0;
        objArr[0] = str2;

        int length = str.length();
        byte[] bArr = new byte[(length / 2)];
        while (i < length) {
            bArr[i / 2] = (byte) ((Character.digit(str2.charAt(i), 16) << 4) + Character.digit(str2.charAt(i + 1), 16));
            i += 2;
        }
        return bArr;
    }

    public static String ByteToStr(byte[] bArr) {

        int i = 0;

        char[] toCharArray = "0123456789abcdef".toCharArray();
        char[] cArr = new char[(bArr.length * 2)];
        while (i < bArr.length) {
            int i2 = bArr[i] & 255;
            int i3 = i * 2;
            cArr[i3] = toCharArray[i2 >>> 4];
            cArr[i3 + 1] = toCharArray[i2 & 15];
            i++;
        }
        return new String(cArr);
    }

    public static ArrayList<String> input(long timeMillis, byte[] inputBytes) {
        ArrayList<String> result = new ArrayList<>();
        String temp;
        for(int i = 0; i < 4; i++) {
            temp = Integer.toHexString(inputBytes[i]);
            if(inputBytes[i] < 0) {
                result.add(temp.substring(6));
            }
            else {
                result.add(temp);
            }

        }
        for(int i = 0; i < 4; i++) {
            result.add("0");
        }
        for(int i = 0; i < 4; i++) {
            temp = Integer.toHexString(inputBytes[i + 32]);
            if(inputBytes[i + 32] < 0){
                result.add(temp.substring(6));
            }
            else {
                result.add(temp);
            }
        }
        for(int i = 0; i < 4; i++) {
            result.add("0");
        }

        String tempByte = Integer.toHexString((int)timeMillis);
        tempByte = tempByte.replace("0x", "");

        for(int i = 0; i < 4; i++) {
            result.add(tempByte.substring(i * 2, i * 2 + 2));
        }

        for(int i = 0; i < result.size(); i++) {
            result.set(i, result.get(i).replace("0x", ""));
        }
        return result;
    }

    public static ArrayList<String> initialize(ArrayList<String> data) {
        int myhex = 0;
        String[] byteTable = BYTETABLE.split(" ");

        for(int i = 0; i < data.size(); i++) {
            int hex1;
            if (i == 0) {
                hex1 = Integer.parseInt(byteTable[Integer.parseInt(byteTable[0], 16) - 1], 16);
                byteTable[i] = Integer.toHexString(hex1);
            } else if (i == 1) {
                int temp = Integer.parseInt("D6", 16) + Integer.parseInt("28", 16);
                hex1 = Integer.parseInt(byteTable[temp%256 - 1], 16);
                myhex = temp%256;
                byteTable[i] = Integer.toHexString(hex1);
            } else {
                int temp = myhex + Integer.parseInt(byteTable[i], 16);
                LogWritter.LogStr(temp + "-----" + myhex + "-----" + byteTable[i] + "----" + Integer.parseInt(byteTable[i], 16));
                hex1 = Integer.parseInt(byteTable[temp%256 - 1], 16);
                myhex = temp%256;
                byteTable[i] = Integer.toHexString(hex1);
            }

            if (hex1 * 2 > 256)
                hex1 = hex1 * 2 - 256;
            else
                hex1 = hex1 * 2;

            String hex2 = byteTable[hex1 - 1];
            int result = Integer.parseInt(hex2, 16) ^ Integer.parseInt(data.get(i), 16);
            data.set(i, Integer.toHexString(result));
        }
        for(int i = 0; i < data.size(); i++) {
            data.set(i, data.get(i).replace("0x", ""));
        }
        return data;
    }

    public static ArrayList<String> handle(ArrayList<String> data) {
        String byte1;
        for(int i = 0; i < data.size(); i++) {
            byte1 = data.get(i);
            if (byte1.length() < 2)
                byte1 += '0';
            else
                byte1 = data.get(i).substring(1) + data.get(i).substring(0, 1);
            if (i < data.size() - 1)
                byte1 = Integer.toHexString(Integer.parseInt(byte1, 16) ^ Integer.parseInt(data.get(i + 1), 16)).replace("0x", "");
            else
                byte1 = Integer.toHexString(Integer.parseInt(byte1, 16) ^ Integer.parseInt(data.get(0), 16)).replace("0x", "");

            byte1 = byte1.replace("0x", "");
            float a = (Integer.parseInt(byte1, 16) & Integer.parseInt("AA", 16)) / 2;
            int b = (int) (Math.abs(a));
            int byte2 = ((Integer.parseInt(byte1, 16) & Integer.parseInt("55", 16)) * 2) | b;
            byte2 = ((byte2 & Integer.parseInt("33", 16)) * 4) | (int) ((byte2 & Integer.parseInt("cc", 16)) / 4);
            String byte3 = Integer.toHexString(byte2).replace("0x", "");
            if (byte3.length() > 1)
                byte3 = byte3.substring(1) + byte3.substring(0, 1);
            else
                byte3 += "0";

            int byte4 = Integer.parseInt(byte3, 16) ^ Integer.parseInt("FF", 16);
            byte4 = byte4 ^ Integer.parseInt("14", 16);
            data.set(i, Integer.toHexString(byte4).replace("0x", ""));
        }
        return data;
    }

    public static String encryption(String str) {
        String re_md5 = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes("UTF-8"));
            byte b[] = md.digest();

            int i;

            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }

            re_md5 = buf.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return re_md5.toLowerCase();
    }

    public static String getXGon(String params, String stub, String cookies) {
        StringBuilder sb = new StringBuilder();
        if (TextUtils.isEmpty(params)) {
            sb.append(NULL_MD5_STRING);
        } else {
            sb.append(encryption(params));
        }

        if (TextUtils.isEmpty(stub)) {
            sb.append(NULL_MD5_STRING);
        } else {
            sb.append(stub);
        }

        if (TextUtils.isEmpty(cookies)) {
            sb.append(NULL_MD5_STRING);
        } else {
            sb.append(encryption(cookies));
        }
        return sb.toString();
    }

    public static String xGorgon(long timeMillis, byte[] inputBytes) {
        ArrayList<String> data1 = new ArrayList<>();
        data1.add("3");
        data1.add("61");
        data1.add("41");
        data1.add("10");
        data1.add("80");
        data1.add("0");

        ArrayList<String> data2 = input(timeMillis, inputBytes);
        data2 = initialize(data2);
        data2 = handle(data2);
        data1.addAll(data2);

        StringBuilder xGorgonStr = new StringBuilder();
        for(int i = 0; i < data1.size(); i++) {
            String temp = data1.get(i);
            if (temp.length() <= 1) {
                xGorgonStr.append("0");
            }
            xGorgonStr.append(temp);
        }
        return xGorgonStr.toString();
    }

    public static String replaceUrlParam(String url, String name, String value) {
        int index = url.indexOf(name + "=");
        if (index != -1) {
            StringBuilder sb = new StringBuilder();
            sb.append(url.substring(0, index)).append(name).append("=").append(value);
            int idx = url.indexOf("&", index);
            if (idx != -1) {
                sb.append(url.substring(idx));
            }
            url = sb.toString();
        }
        return url;
    }

    public static Map<String, String> getQueryMapParam(String url) {
        Map<String, String> queryMap = new HashMap<>();
        String[] index = url.split("&");
        for(int i = 0; i < index.length; i++) {
            String[] mapParam = index[i].split("=");
            queryMap.put(mapParam[0], mapParam[1]);
            LogWritter.LogStr(mapParam[0] + ":" + mapParam[1]);
        }
        return queryMap;
    }

    public static String get_X_gorgon(String url, String cookies, long ts) {
        String STUB = "";
        String params = url.substring(url.indexOf("?") + 1, url.length());
        String s = getXGon(params, STUB, cookies);
        String gorgon = xGorgon(ts, StrToByte(s));

        return gorgon;
    }

    public static long get_rticket() {
        long _rticket = System.currentTimeMillis();
        return _rticket;
    }

    public static Map<String, Object> get_headers(String params, long _rticket) {
        String cookies = Constants.DOUYIN_COOKIES;
        Map<String, Object> headers = new HashMap<>();
        long ts = _rticket/1000;
        headers.put("x-tt-trace-id", "00-dc4fbb7709de78c5692a11bfc7550919-dc4fbb7709de78c5-01");
        headers.put("X-Khronos", ts);
        headers.put("sdk-version", "2");
        headers.put("X-SS-REQ-TICKET", _rticket);
        headers.put("Connection", "Keep-Alive");
        headers.put("User-Agent", "com.ss.android.ugc.aweme.lite/120900 (Linux; U; Android 6.0.1; zh_CN; Nexus 5; Build/MOB30Y; Cronet/TTNetVersion:a87ab8c7 2020-11-24 QuicVersion:47946d2a 2020-10-14)");
        headers.put("X-Gorgon", get_X_gorgon(params, cookies, ts));
        headers.put("Host", "aweme-eagle-lq.snssdk.com");
        headers.put("x-ss-dp", "2329");
        headers.put("passport-sdk-version", "19");
        headers.put("x-common-params-v2", "os_api=23&device_platform=android&device_type=Nexus%205&iid=1107961669234743&version_code=120900&app_name=douyin_lite&openudid=1c253d8aa6af6661&device_id=59719308946&os_version=6.0.1&aid=2329&channel=tengxun_2329_1210&ssmix=a&manifest_version_code=120900&dpi=480&cdid=c60ac262-8dc6-4cdf-b034-631cbc7b723f&version_name=12.9.0&resolution=1080*1776&language=zh&device_brand=google&app_type=normal&ac=wifi&update_version_code=12909900");

        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Object> m : headers.entrySet()) {
            sb.append(m.getKey() + ":" + m.getValue() + "\n");
        }
        LogWritter.LogStr( sb.toString() + "\n" + params);

        return headers;
    }

    public static String get_params(long _rticket) {
        long ts = _rticket / 1000;

        String params = Constants.DOUYIN_FEED_PARAMS;

        params = replaceUrlParam(params, "ts", String.valueOf(ts));
        params = replaceUrlParam(params, "_rticket", String.valueOf(_rticket));

        LogWritter.LogStr(params);

        return Config.DOUYIN_VIDEO_API + Constants.DOUYIN_VIDEOS_API_ID + "?" + params;
    }
}
