package com.bbeaggoo.poitest;

import com.bbeaggoo.poitest.data.Attribute;
import com.bbeaggoo.poitest.data.FloorName;
import com.bbeaggoo.poitest.data.Name;
import com.bbeaggoo.poitest.data.POIData;
import com.bbeaggoo.poitest.data.Position;

import io.realm.annotations.RealmModule;

@RealmModule(classes = {POIData.class, Attribute.class, FloorName.class, Name.class, Position.class})
public class AllPOIModules { }
