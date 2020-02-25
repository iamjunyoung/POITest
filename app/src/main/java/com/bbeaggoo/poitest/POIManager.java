package com.bbeaggoo.poitest;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.util.Log;

import com.bbeaggoo.poitest.DatabaseContract.POIColumns;
import com.bbeaggoo.poitest.data.POIData;
import com.bbeaggoo.poitest.data.Position;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

/**
 * The {@code <POIManager>} class provides methods to open, reload, find and add some facilities.
 * POI(Point Of Interest) data file.</p>
 */
public class POIManager extends ContentProvider {
    private static final String TAG = POIManager.class.getSimpleName();

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
    public static final Uri CONTENT_URI = Uri.parse("content://com.bbeaggoo.poitest/pois");

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
        JSONUtil generatePoi = new JSONUtil(this.context);
        JSONArray array = generatePoi.jsonParsing(generatePoi.getJsonString());
        open2(array);
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
/*
			status += "\nPoiID : " + poiData.getPoiId() +
					"  / floorId : " + poiData.getFloorId() +
					" / floorName : " + poiData.getFloorName() +
					" / floorOrder : " + poiData.getFlooirOrder() +
					" / attribute : " + poiData.getAttributes() +
					" / radius : " + poiData.getRadius() +
					" / isRestricted : " + poiData.getIsRestricted() +
					" / name : " + poiData.getName() +
					" / position : " + poiData.getPoiId() +
					"/ theta : " + poiData.getTheta() +
					"/ type : " + poiData.getType() +
					"/ isHome : " + poiData.isHome() +
					"/ isDock : " + poiData.isDock();

 */
            /*
            private String              poiId;
            private String              floorId;
            private FloorName           floorName;
            private int                 floorOrder;
            private Attribute           attributes;
            private int                 radius;
            private int                 isRestricted;
            private Name                name;
            private Position            pos;
            private int                 theta;
            private int                 type;
            private boolean             isHome;
            private boolean             isDock;
             */
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

        return null;
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
                .equalTo("isDock", true)
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
                for (POIData poiData : realm.where(POIData.class).equalTo("isDock", true).findAll()) {
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
			/*
			status += "\nPoiID : " + poiData.getPoiId() +
					"  / floorId : " + poiData.getFloorId() +
					" / floorName : " + poiData.getFloorName() +
					" / floorOrder : " + poiData.getFlooirOrder() +
					" / attribute : " + poiData.getAttributes() +
					" / radius : " + poiData.getRadius() +
					" / isRestricted : " + poiData.getIsRestricted() +
					" / name : " + poiData.getName() +
					" / position : " + poiData.getPoiId() +
					"/ theta : " + poiData.getTheta() +
					"/ type : " + poiData.getType() +
					"/ isHome : " + poiData.isHome() +
					"/ isDock : " + poiData.isDock();

			*/
            /*
            private String              poiId;
            private String              floorId;
            private FloorName           floorName;
            private int                 floorOrder;
            private Attribute           attributes;
            private int                 radius;
            private int                 isRestricted;
            private Name                name;
            private Position            pos;
            private int                 theta;
            private int                 type;
            private boolean             isHome;
            private boolean             isDock;
             */
        }
    }

    @Override
    public boolean onCreate() {
        //this.context = context;
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
                        , POSITION_X, POSITION_Y, POSITION_Z, IS_HOME, IS_CHARGER}
                , null
                , null
                , null);

        if (c != null) {
            while (c.moveToNext()) {
                Log.d("hello", c.getString(0) + "\n");
            }
        }

        return true;
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
        int match = sUriMatcher.match(uri);

        //ArrayList<POIData> allPOIDatas = new ArrayList<>();
        /*
        if (realm == null) {
            realm = Realm.getInstance(realmConfig);
        }
         */

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

                    break;

                //Expected "query one" Uri: content://com.example.rgher.realmtodo/tasks/{id}
                default:
                    throw new UnsupportedOperationException("Unknown uri: " + uri);
            }

            myCursor.setNotificationUri(getContext().getContentResolver(), uri);
        } finally {
            realm.close();
        }

        /*

        ArrayList<POIData> allPOIDatas = new ArrayList<>();

        RealmResults<POIData> realmResults = realm.where(POIData.class).findAll();
        for (POIData item : realmResults) {
            allPOIDatas.add(item);
        }

         */

        return myCursor;

    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }



    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
