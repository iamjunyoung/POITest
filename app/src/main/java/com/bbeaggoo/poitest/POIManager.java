package com.bbeaggoo.poitest;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.bbeaggoo.poitest.DatabaseContract.POIColumns;
import com.bbeaggoo.poitest.data.POIData;
import com.bbeaggoo.poitest.data.Position;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

import static com.bbeaggoo.poitest.DatabaseContract.DATABASE_CREATE;
import static com.bbeaggoo.poitest.DatabaseContract.TABLE_POIS;

/**
 * The {@code <POIManager>} class provides methods to open, reload, find and add some facilities.
 * POI(Point Of Interest) data file.</p>
 */
public class POIManager extends ContentProvider {
    private static final String TAG = POIManager.class.getSimpleName();

    public static final String ID = "_id";
    public static final String POI_ID = "poi_id";
    public static final String FLOOR_ID = "floor_id";
    public static final String FLOOR_NAME_EN = "floor_name_en";
    public static final String FLOOR_NAME_KR = "floor_name_kr";
    public static final String FLOOR_ORDER = "floor_order";
    public static final String FLOOR_INDEX = "floor_index";
    public static final String ATTRIBUTE_EL_ID = "attribute_el_id";
    public static final String ATTRIBUTE_EL_VENDER = "attribute_el_vendor";
    public static final String ATTRIBUTE_EL_FLOOR_LIST = "attribute_el_floor_list";
    public static final String ATTRIBUTE_DESC = "attribute_desc";
    public static final String ATTRIBUTE_TEL = "attribute_tel";
    public static final String RADIUS = "radius";
    public static final String IS_RESTRICTED = "is_restricted";
    public static final String NAME_KR = "name_kr";
    public static final String POSITION_X = "position_x";
    public static final String POSITION_Y = "position_y";
    public static final String POSITION_Z = "position_z";
    public static final String THETA = "theta";
    public static final String TYPE = "type";
    public static final String IS_HOME = "is_home";
    public static final String IS_CHARGER = "is_charger";
    public static final String IS_IN_POILIST = "is_in_poi_list";

    final String poiName = "poi.json";
    final String largeName = "category_large.json";
    final String mediumName = "category_medium.json";

    private Context context;
    private String floor = "B2";
    //private final RealmConfiguration realmConfig;
    private RealmConfiguration realmConfig;
    private Realm realm;

    private static final int CLEANUP_JOB_ID = 43;
    private static final int POIS = 100;
    private static final int POIS_WITH_ID = 101;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        // content://com.example.rgher.realmtodo/tasks
        sUriMatcher.addURI(DatabaseContract.CONTENT_AUTHORITY,
                DatabaseContract.TABLE_POIS,
                POIS);

        // content://com.example.rgher.realmtodo/tasks/id
        sUriMatcher.addURI(DatabaseContract.CONTENT_AUTHORITY,
                DatabaseContract.TABLE_POIS + "/#",
                POIS_WITH_ID);
    }

    ArrayList<POIData> allPOIDatas = null;

    DBHelper mOpenHelper = null;

    public POIManager() {

    }

    /**
     * <span class="lang en">Construct a {@code POIManager} with the context and the floor and init Realm&#8228;</span>
     * <span class="lang ko">POIManager 생성, Realm 초기화.</span>
     *
     * @param context the context of activity, service
     * @param floor default floor (ex: B1, 1F, 2F, 3F)<br>
     *         {@code 1F} default value if empty
     */

    /*
    public POIManager(Context context, String floor) {
        this.context = context;
        this.floor = floor.isEmpty() ? "1F" : floor;
        Log.d(TAG, "POIManager");

        Realm.init(context);
        realmConfig = new RealmConfiguration
                .Builder()
                //.name("library.facility.realm")
                .name("poiData.realm")
                //.modules(new AllFacilitiesModules())
                .modules(new AllPOIModules())
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
//                .migration((realm, oldVersion, newVersion) -> {
//                  RealmSchema schema = realm.getSchema();
//                  if (oldVersion == 0) {
//                     RealmObjectSchema facilitiesSchema = schema.get("Facilities");
//                     facilitiesSchema.addField("fullName", String.class, FieldAttribute.REQUIRED);
//                     facilitiesSchema.removeField("lastName");
//
//                     oldVersion++;
//                  }
//                })
                .build();
    }
     */
    public void generatePoi() {
        JSONUtil generatePoi = new JSONUtil(getContext());
        JSONArray array = generatePoi.jsonParsing(generatePoi.getJsonString());
        generatePOIDatabase(array);
    }

    public void generatePOIDatabase(JSONArray array) {
        try {
            for (int i = 0 ; i < array.length() ; i++) {
                Log.d(TAG, "generatePOIDatabase object to db " + i);
                insertToDB(array.getJSONObject(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void insertToDB(JSONObject object) {
        ContentValues cv = new ContentValues();
        /*
            POIColumns.POI_ID + " text, " +
            POIColumns.FLOOR_ID + " text, " +
            POIColumns.FLOOR_NAME_EN + " text, " +
            POIColumns.FLOOR_NAME_KR + " text, " +
            POIColumns.FLOOR_ORDER + " integer, " +
            POIColumns.ATTRIBUTE_EL_ID + " text, " +
            POIColumns.ATTRIBUTE_EL_VENDER + " text, " +
            POIColumns.ATTRIBUTE_EL_FLOOR_LIST + " text, " +
            POIColumns.ATTRIBUTE_DESC + " text, " +
            POIColumns.ATTRIBUTE_TEL + " text, " +
            POIColumns.RADIUS + " integer, " +
            POIColumns.IS_RESTRICTED + " integer, " +
            POIColumns.NAME_KR + " text, " +
            POIColumns.POSITION_X + " integer, " +
            POIColumns.POSITION_Y + " integer, " +
            POIColumns.POSITION_Z + " integer, " +
            POIColumns.THETA + " integer, " +
            POIColumns.TYPE + " integer, " +
            POIColumns.IS_HOME + " text, " +
            POIColumns.IS_CHARGER + " text);";
         */
        /*
            public static final String POI_ID = "poi_id";
            public static final String FLOOR_ID = "floor_id";
            public static final String FLOOR_NAME_EN = "floor_name_en";
            public static final String FLOOR_NAME_KR = "floor_name_kr";
            public static final String FLOOR_ORDER = "floor_order";
            public static final String ATTRIBUTE_EL_ID = "attribute_el_id";
            public static final String ATTRIBUTE_EL_VENDER = "attribute_el_vendor";
            public static final String ATTRIBUTE_EL_FLOOR_LIST = "attribute_el_floor_list";
            public static final String ATTRIBUTE_DESC = "attribute_desc";
            public static final String ATTRIBUTE_TEL = "attribute_tel";
            public static final String RADIUS = "radius";
            public static final String IS_RESTRICTED = "is_restricted";
            public static final String NAME_KR = "name_kr";
            public static final String POSITION_X = "position_x";
            public static final String POSITION_Y = "position_y";
            public static final String POSITION_Z = "position_z";
            public static final String THETA = "theta";
            public static final String TYPE = "type";
            public static final String IS_HOME = "is_home";
            public static final String IS_CHARGER = "is_charger";
         */
            /*
                    newObject.put("poiId", poiId);
                    newObject.put("floorId", floor);
                    newObject.put("floorName", floorsObject.getJSONObject(floor).getJSONObject("name"));
                    newObject.put("floorOrder", floorsObject.getJSONObject(floor).getInt("order"));

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

                    newObject.put("isHome", false);
                    newObject.put("isCharger", false);
            */
        try {
            Log.d("poi", "" + object.getJSONObject("attributes").optString("elevatorID", "N/A") + "  "
                    + object.getJSONObject("attributes").optString("elevatorVendor", "N/A") + "  "
                    + object.getJSONObject("attributes").optString("elevatorFloorList", "N/A") + "  "
                    + object.getJSONObject("attributes").optString("desc", "N/A") + "  "
                    + object.getJSONObject("attributes").optString("tel", "N/A"));
            cv.put(POI_ID, object.getString("poiId"));
            cv.put(FLOOR_ID, object.getString("floorId"));
            cv.put(FLOOR_NAME_EN, object.getJSONObject("floorName").getString("en"));
            cv.put(FLOOR_NAME_KR, object.getJSONObject("floorName").getString("kr"));
            cv.put(FLOOR_ORDER, object.getInt("floorOrder"));
            cv.put(FLOOR_INDEX, object.getInt("floorIndex"));
            cv.put(ATTRIBUTE_EL_ID, object.getJSONObject("attributes").optString("elevatorID", "N/A"));
            cv.put(ATTRIBUTE_EL_VENDER, object.getJSONObject("attributes").optString("elevatorVendor", "N/A"));
            cv.put(ATTRIBUTE_EL_FLOOR_LIST, object.getJSONObject("attributes").optString("elevatorFloorList", "N/A"));
            cv.put(ATTRIBUTE_DESC, object.getJSONObject("attributes").optString("desc", "N/A"));
            cv.put(ATTRIBUTE_TEL, object.getJSONObject("attributes").optString("tel", "N/A"));

            //double  real
            cv.put(RADIUS, object.getInt("radius"));
            cv.put(IS_RESTRICTED, object.getInt("isRestricted"));
            cv.put(NAME_KR, object.getJSONObject("name").getString("kr"));
            cv.put(POSITION_X, object.getJSONObject("pos").getDouble("x"));
            cv.put(POSITION_Y, object.getJSONObject("pos").getDouble("y"));
            cv.put(POSITION_Z, object.getJSONObject("pos").getInt("z"));
            cv.put(THETA, object.getInt("theta"));
            cv.put(TYPE, object.getInt("type"));
            cv.put(IS_HOME, "false");
            cv.put(IS_CHARGER, "false");
            cv.put(IS_IN_POILIST, "true");
            /*
            boolean isHome = object.getBoolean("isHome");
            if (isHome) {
                cv.put(IS_HOME, "true");
            } else {
                cv.put(IS_HOME, "false");
            }
            boolean isCharger = object.getBoolean("isCharger");
            if (isCharger) {
                cv.put(IS_CHARGER, "true");
            } else {
                cv.put(IS_CHARGER, "false");
            }
            cv.put(IS_IN_POILIST, object.getBoolean("isInPOIList"));
             */

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Uri retUir  = insert(DatabaseContract.CONTENT_URI, cv);
        Log.d(TAG, "Insert object to db ");

    }

    public void open2(JSONArray array) {
        Log.d(TAG, "open() check result " + array );
        realm = Realm.getInstance(realmConfig);

        RealmResults<POIData> poiDatas = realm.where(POIData.class).findAll();
        Log.d(TAG, ""+ poiDatas);

        if (poiDatas.size() == 0) {
            realm.executeTransaction(realm -> {
                // Facilities 업데이트 파일 유무
                Log.d(TAG, "open() createAllFromJson from asset " + array );
                realm.createAllFromJson(POIData.class, array);

            });
        }

        String status = "";
        for (POIData poiData : realm.where(POIData.class).findAll()) {
            Log.d(TAG, "\n" + poiData);
        }
    }

    public ArrayList<POIData> getAllPOI() {
        ArrayList<POIData> allPOIDatas = new ArrayList<>();
        if (realm == null) {
            realm = Realm.getInstance(realmConfig);
        }
        RealmResults<POIData> realmResults = realm.where(POIData.class).findAll();
        for (POIData item : realmResults) {
            allPOIDatas.add(item);
        }
        return allPOIDatas;
    }

    public ArrayList<POIData> findHomePOI() {
        ArrayList<POIData> homePOIDatas = new ArrayList<>();
        if (realm == null) {
            realm = Realm.getInstance(realmConfig);
        }
        RealmResults<POIData> realmResults = realm.where(POIData.class)
                .equalTo("isHome", true)
                .findAll();
        for (POIData item : realmResults) {
            homePOIDatas.add(item);
        }
        return homePOIDatas;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        long rowId = db.insert(DatabaseContract.TABLE_POIS, null, values);
        getContext().getContentResolver().notifyChange(uri, null);
        return null;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        //return dbManager.insertAll(values);
        return 0;
    }

    public void registerHomePOI(String poiId) { // poiId input exist in DB
        // id 중에서 유효성 check하는 루틴 먼저.
        // 있는 id 중에서 home을 register할 수 있다.
        if (!verifyPOIId(poiId)) {
            return;
        }

        if (realm == null) {
            realm = Realm.getInstance(realmConfig);
        }
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
				/*
				final POIData obj = new POIData();
				obj.setPoiId(poiId);
				obj.setHome(true);

				realm.copyToRealmOrUpdate(obj);
				*/
                POIData obj = realm.where(POIData.class).equalTo("poiId", poiId).findFirst();// to be modified.
                obj.setHome(true);
            }
        });
    }


    public void registerHomePOI(String newPoiId, int x, int y, int z, int theta) { // new PoiId
		/*
		if (verifyPOIId(newPoiId)) {
			Toast.makeText(context, "This POIID is exist aleady", Toast.LENGTH_LONG);
			return;
		}
		 */
        if (realm == null) {
            realm = Realm.getInstance(realmConfig);
        }
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                POIData obj = realm.createObject(POIData.class);
                obj.setPoiId(newPoiId);

                Log.d(TAG, "registerHomePOI" + newPoiId);

                Position posObj = realm.createObject(Position.class);
                posObj.setX(x);
                posObj.setY(y);
                posObj.setZ(z);
                obj.setPos(posObj);
                obj.setTheta(theta);
                obj.setHome(true);

                realm.copyToRealm(obj);
            }
        });
    }

    public ArrayList<POIData> findDockPOI() {
        ArrayList<POIData> dockPOIDatas = new ArrayList<>();
        if (realm == null) {
            realm = Realm.getInstance(realmConfig);
        }
        RealmResults<POIData> realmResults = realm.where(POIData.class)
                .equalTo("isCharger", true)
                .findAll();
        for (POIData item : realmResults) {
            dockPOIDatas.add(item);
        }
        return dockPOIDatas;
    }

    public void registerDockPOI(String newPoiId, int x, int y, int z, int theta) { // newPoiId
		/*
		if (verifyPOIId(newPoiId)) {
			Toast.makeText(context, "This POIID is exist aleady", Toast.LENGTH_LONG);
			return;
		}
		 */
        if (realm == null) {
            realm = Realm.getInstance(realmConfig);
        }
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                POIData obj = realm.createObject(POIData.class);
                obj.setPoiId(newPoiId);

                Log.d(TAG, "registerDockPOI" + newPoiId);

                Position posObj = realm.createObject(Position.class);
                posObj.setX(x);
                posObj.setY(y);
                posObj.setZ(z);
                obj.setPos(posObj);
                obj.setTheta(theta);
                obj.setCharger(true);

                realm.copyToRealm(obj);
            }
        });
    }

    public  void removeHomePOI() {
        if (realm == null) {
            realm = Realm.getInstance(realmConfig);
        }
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                for (POIData poiData : realm.where(POIData.class).equalTo("isHome", true).findAll()) {
                    poiData.setHome(false);
                }
            }
        });
    }

    public void removeDockPOI() {
        if (realm == null) {
            realm = Realm.getInstance(realmConfig);
        }
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                for (POIData poiData : realm.where(POIData.class).equalTo("isCharger", true).findAll()) {
                    poiData.setCharger(false);
                }
            }
        });
    }

    public boolean verifyPOIId(String poiId) {
        if (realm == null) {
            realm = Realm.getInstance(realmConfig);
        }
        RealmResults<POIData> realmResults = realm.where(POIData.class)
                .equalTo("poiId", poiId)
                .findAll();

        if (realmResults.size() != 0) {
            return true; // exist in db
        } else {
            return false; // not exist in db
        }
    }

    public String getPOIDatas2Json(POIData poiDatas) {
        Log.d(TAG, poiDatas + "  getF2J " + realm.copyFromRealm(poiDatas).toString());
        String temp = new Gson().toJson(realm.copyFromRealm(poiDatas));
        Log.d(TAG, "temp : " + temp);
        return temp;
    }

    public void showAllPOIDatasInDB() {
        String status = "";
        for (POIData poiData : realm.where(POIData.class).findAll()) {
            Log.d(TAG, "\n" + poiData);
        }
    }

    @Override
    public boolean onCreate() {
        //this.context = context;
        Log.d(TAG, "POIManager ContentProvider onCreate" );

        /*
        this.context = getContext();
        floor = floor.isEmpty() ? "1F" : floor;
        Log.d(TAG, "POIManager");

        //Realm.init(context);
        Realm.init(this.context);
        realmConfig = new RealmConfiguration
                .Builder()
                //.name("library.facility.realm")
                .name("poiData.realm")
                //.modules(new AllFacilitiesModules())
                .modules(new AllPOIModules())
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
//                .migration((realm, oldVersion, newVersion) -> {
//                  RealmSchema schema = realm.getSchema();
//                  if (oldVersion == 0) {
//                     RealmObjectSchema facilitiesSchema = schema.get("Facilities");
//                     facilitiesSchema.addField("fullName", String.class, FieldAttribute.REQUIRED);
//                     facilitiesSchema.removeField("lastName");
//
//                     oldVersion++;
//                  }
//                })
                .build();

        generatePoi();

        showAllPOIDatasInDB();

        allPOIDatas = new ArrayList<>();
        if (realm == null) {
            realm = Realm.getInstance(realmConfig);
        }
        RealmResults<POIData> realmResults = realm.where(POIData.class).findAll();
        for (POIData item : realmResults) {
            allPOIDatas.add(item);
        }

        Cursor c = query(CONTENT_URI
                , new String[] {_ID, FLOOR_ID, FLOOR_NAME_EN, FLOOR_NAME_KR
                        , FLOOR_ORDER, ATTRIBUTE_EL_ID, ATTRIBUTE_EL_VENDER, ATTRIBUTE_EL_FLOOR_LIST
                        , ATTRIBUTE_DESC, ATTRIBUTE_TEL, RADIUS, IS_RESTRICTED, NAME_KR
                        , POSITION_X, POSITION_Y, POSITION_Z, THETA, TYPE, IS_HOME, IS_CHARGER}
                , null
                , null
                , null);

        if (c != null) {
            while (c.moveToNext()) {
                Log.d("hello", c.getString(0) + "\n");
            }
        }
        */

        // 충전중이고 Event를 받으면 아래 코드를 그대로 실행해주면 됨. 날리고 create하고 generate
        mOpenHelper = new DBHelper(getContext()); // DBHelper 싱글톤 선언 필요.
        if (mOpenHelper != null) {
            SQLiteDatabase db = mOpenHelper.getWritableDatabase();
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_POIS);
            db.execSQL(DATABASE_CREATE);

            generatePoi();
            return true;
        }
        return false;
    }
    /*
        public static final String _ID = "poi_id";
        public static final String FLOOR_ID = "floor_id";
        public static final String FLOOR_NAME_EN = "floor_name_en";
        public static final String FLOOR_NAME_KR = "floor_name_kr";
        public static final String FLOOR_ORDER = "floor_order";
        public static final String ATTRIBUTE_EL_ID = "attribute_el_id";
        public static final String ATTRIBUTE_EL_VENDER = "attribute_el_vendor";
        public static final String ATTRIBUTE_EL_FLOOR_LIST = "attribute_el_floor_list";
        public static final String ATTRIBUTE_DESC = "attribute_desc";
        public static final String ATTRIBUTE_TEL = "attribute_tel";
        public static final String RADIUS = "radius";
        public static final String IS_RESTRICTED = "is_restricted";
        public static final String NAME_KR = "name_kr";
        public static final String POSITION_X = "position_x";
        public static final String POSITION_Y = "position_y";
        public static final String POSITION_Z = "position_z";
        public static final String IS_HOME = "is_home";
        public static final String IS_CHARGER = "is_charger";
    */
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        //int match = sUriMatcher.match(uri);

        //ArrayList<POIData> allPOIDatas = new ArrayList<>();
        /*
        if (realm == null) {
            realm = Realm.getInstance(realmConfig);
        }
         */

        /*
        MatrixCursor myCursor = new MatrixCursor( new String[]{POIColumns._ID
                , POIColumns.FLOOR_ID, POIColumns.FLOOR_NAME_EN
                , POIColumns.FLOOR_NAME_KR, POIColumns.FLOOR_ORDER
                , POIColumns.ATTRIBUTE_EL_ID, POIColumns.ATTRIBUTE_EL_VENDER
                , POIColumns.ATTRIBUTE_EL_FLOOR_LIST, POIColumns.ATTRIBUTE_DESC
                , POIColumns.ATTRIBUTE_TEL, POIColumns.RADIUS
                , POIColumns.IS_RESTRICTED, POIColumns.NAME_KR
                , POIColumns.POSITION_X, POIColumns.POSITION_Y
                , POIColumns.POSITION_Z, POIColumns.IS_HOME
                , POIColumns.IS_CHARGER
        });

        try {
            switch (match) {
                //Expected "query all" Uri: content://com.example.rgher.realmtodo/tasks

                case POIS:

                    RealmResults<POIData> realmResults = realm.where(POIData.class).findAllAsync();
                    for (POIData poiData : realmResults) {
                        Object[] rowData = new Object[] {poiData.getPoiId()
                                , poiData.getFloorId(), poiData.getFloorName().getEn()
                                , poiData.getFloorName().getKr(), poiData.getFlooirOrder()
                                , poiData.getAttributes().getElevatorID(), poiData.getAttributes().getElevatorVendor()
                                , poiData.getAttributes().getElvatorFloorList(), poiData.getAttributes().getDesc()
                                , poiData.getAttributes().getTel(), poiData.getRadius()
                                , poiData.getIsRestricted(), poiData.getName().getKr()
                                , poiData.getPos().getX(), poiData.getPos().getY()
                                , poiData.getPos().getZ(), poiData.isHome()
                                , poiData.isCharger()};
                        myCursor.addRow(rowData);
                        Log.v("RealmDB", poiData.toString());
                    }
                */
                     /*
                    for (POIData poiData : allPOIDatas) {
                        Object[] rowData = new Object[] {poiData.getPoiId()
                                , poiData.getFloorId(), poiData.getFloorName().getEn()
                                , poiData.getFloorName().getKr(), poiData.getFlooirOrder()
                                , poiData.getAttributes().getElevatorID(), poiData.getAttributes().getElevatorVendor()
                                , poiData.getAttributes().getElvatorFloorList(), poiData.getAttributes().getDesc()
                                , poiData.getAttributes().getTel(), poiData.getRadius()
                                , poiData.getIsRestricted(), poiData.getName().getKr()
                                , poiData.getPos().getX(), poiData.getPos().getY()
                                , poiData.getPos().getZ(), poiData.isHome()
                                , poiData.isCharger()};
                        myCursor.addRow(rowData);
                        Log.v("RealmDB", poiData.toString());
                    }
                      */
/*
                    break;

                //Expected "query one" Uri: content://com.example.rgher.realmtodo/tasks/{id}
                default:
                    throw new UnsupportedOperationException("Unknown uri: " + uri);
            }

            myCursor.setNotificationUri(getContext().getContentResolver(), uri);

        } finally {
            realm.close();
        }
*/
        /*

        ArrayList<POIData> allPOIDatas = new ArrayList<>();

        RealmResults<POIData> realmResults = realm.where(POIData.class).findAll();
        for (POIData item : realmResults) {
            allPOIDatas.add(item);
        }

         */

        //return myCursor;
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        Cursor c = null;
        c = db.query(DatabaseContract.TABLE_POIS,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);

        return c;

    }


    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }



    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int count;
        count = db.delete(DatabaseContract.TABLE_POIS, selection, selectionArgs);
        return count;
    }


    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int count;
        count = db.update(DatabaseContract.TABLE_POIS, values, selection, selectionArgs);
        return count;
    }


}
