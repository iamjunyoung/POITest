package com.bbeaggoo.poitest;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

public class JSONUtil {
    public Context context = null;
    public String TAG = "JSONUTIL";

    public JSONUtil(Context context) {
        this.context = context;
    }

    public JSONArray jsonParsing(String json) {
        JSONArray resultArray = null;

        try{
            JSONObject jsonObject = new JSONObject(json);
            JSONObject newObject = null;
            resultArray = new JSONArray();
            JSONObject branchDataObject = jsonObject.getJSONObject("data");
            JSONObject buildingObject = branchDataObject.getJSONObject("6831");
            JSONObject floorsObject = buildingObject.getJSONObject("floors");
            Log.d(TAG, "" + floorsObject);

            Iterator i = floorsObject.keys();
            while (i.hasNext()) {
                String floor = i.next().toString();

                Log.d(TAG, "key " + floor + "  value : " + floorsObject.get(floor));

                Iterator j = floorsObject.getJSONObject(floor).getJSONObject("customPointData").keys();
                while (j.hasNext()) {
                    String poiId = j.next().toString();
                    Log.d(TAG, "poiId " + poiId + "  value : " + floorsObject.getJSONObject(floor).getJSONObject("customPointData").get(poiId));

                    newObject = new JSONObject();

                    newObject.put("poiId", poiId);
                    newObject.put("floorId", floor); // Not SNUH_SEOUL_DH_B2 but -2
                    newObject.put("floorName", floorsObject.getJSONObject(floor).getJSONObject("name"));
                    newObject.put("floorOrder", floorsObject.getJSONObject(floor).getInt("order"));
                    newObject.put("floorIndex", floorsObject.getJSONObject(floor).getInt("index"));
                    newObject.put("attributes", floorsObject.getJSONObject(floor).getJSONObject("customPointData").getJSONObject(poiId).getJSONObject("attributes"));
                    newObject.put("radius", floorsObject.getJSONObject(floor).getJSONObject("customPointData").getJSONObject(poiId).getInt("radius"));
                    newObject.put("isRestricted", floorsObject.getJSONObject(floor).getJSONObject("customPointData").getJSONObject(poiId).getInt("isRestricted"));
                    newObject.put("name", floorsObject.getJSONObject(floor).getJSONObject("customPointData").getJSONObject(poiId).getJSONObject("name"));
                    newObject.put("pos", floorsObject.getJSONObject(floor).getJSONObject("customPointData").getJSONObject(poiId).getJSONObject("pos"));
                    //pos {"x":1011,"y":-1395}
                    JSONObject addZPos = floorsObject.getJSONObject(floor).getJSONObject("customPointData").getJSONObject(poiId).getJSONObject("pos");
                    addZPos.put("z", floorsObject.getJSONObject(floor).getInt("order"));
                    Log.d(TAG, "pos(Added z pos)  " + addZPos);

                    newObject.put("theta", floorsObject.getJSONObject(floor).getJSONObject("customPointData").getJSONObject(poiId).getInt("theta"));
                    newObject.put("type", floorsObject.getJSONObject(floor).getJSONObject("customPointData").getJSONObject(poiId).getInt("type"));

                    newObject.put("isHome", false); // default false
                    newObject.put("isCharger", false); // default false
                    newObject.put("isInPOIList", true); // default true
                    Log.d(TAG, "transfer " + newObject);
                    resultArray.put(newObject);
                }
            }
            Log.d(TAG, "To JSONArray " + resultArray);
        }catch (JSONException e) {
            e.printStackTrace();
        }
        return resultArray;
    }

    public String getJsonString() {
        String json = "";

        try {
            //InputStream is = context.getAssets().open("poiDataSample.json");
            //InputStream is = context.getAssets().open("sample_customPointList_fixed.json");
            InputStream is = context.getAssets().open("sample_customPointList_200227.json"); // 외부 Download 하위에 있는거로 가져올 수 있도록 수정.
            int fileSize = is.available();
            byte[] buffer = new byte[fileSize];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }

        return json;
    }
}
