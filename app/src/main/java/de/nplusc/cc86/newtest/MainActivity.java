package de.nplusc.cc86.newtest;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.geojson.LngLatAlt;
import org.geojson.MultiPoint;
import org.geojson.Point;

import java.util.HashMap;
import java.util.logging.Logger;


public class MainActivity extends Activity implements View.OnClickListener
{
    TextView hw;
    MapView mv;
    GoogleMap  mmap;
    private HashMap<LatLng,TreeMeta> metadata;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupmap();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupmap();
    }

    private void setupmap()
    {

        if(mmap==null)
        {
            FragmentManager f = getFragmentManager();
            MapFragment frg = (MapFragment) f.findFragmentById(R.id.mmap);
            Log.d("ALZR",frg + "");
            mmap = frg .getMap();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.button)
        {
            addTrees();
        }
        else
        {

        }
    }

    public void addTrees() {

        metadata=new HashMap<LatLng, TreeMeta>();
        int maxCounter = 0;
        /*FeatureCollection fc = CastaneaReader.with(this).read(R.raw.kastanien);


        mmap.setInfoWindowAdapter(new TreePopup());
        for (Feature feature : fc) {
            MultiPoint point = (MultiPoint)feature.getGeometry();
            LngLatAlt position = point.getCoordinates().get(0);
            String kastaniensorte = feature.getProperty("BAUMART");
            String kronendurchmesser=feature.getProperty("KRONE_DM"+"");
            String pflanzjahr=feature.getProperty("PFLANZJAHR")+"";
            String stammumfang=feature.getProperty("STAMMUMFAN")+"";
           /* String additionalInfo = "Kronendurchmesser: "+kronendurchmesser+"\n"+
                    "Stammumfang: "+stammumfang+"\n"+
                    "Pflanzjahr: "+pflanzjahr;*/
        /*
            TreeMeta tm = new TreeMeta(pflanzjahr,kronendurchmesser,stammumfang);
            LatLng metapos = new LatLng(position.getLatitude(), position.getLongitude());
            metadata.put(metapos,tm);
            mmap.addMarker(new MarkerOptions().position(metapos).title(kastaniensorte).snippet("  "));

            if (maxCounter++ > 20) {
                break;
            }
        }*/
        int[] parseListOSM = {R.raw.castanea,R.raw.aesculus};
        for(int parsefiles:parseListOSM)
        {
            maxCounter = 0;
            FeatureCollection fc=CastaneaReader.with(this).read(parsefiles);
            for (Feature feature : fc)
            {
                Point point = (Point)feature.getGeometry();
                LngLatAlt position = point.getCoordinates();


                TreeMeta tm = new TreeMeta("unbekannt","unbekannt","unbekannt");
                LatLng metapos = new LatLng(position.getLatitude(), position.getLongitude());
                metadata.put(metapos,tm);
                mmap.addMarker(new MarkerOptions().position(metapos).title(feature.getProperty("genus")+"").snippet("  "));
                if (maxCounter++ > 100) {
                    break;
                }
            }

        }
    }

    public class TreeMeta{
        private String treePlantYear;
        private String treeTopSize;
        private String treeTrunkSize;

        public TreeMeta(String treePlantYear, String treeTopSize, String treeTrunkSize) {
            this.treePlantYear = treePlantYear;
            this.treeTopSize = treeTopSize;
            this.treeTrunkSize = treeTrunkSize;
        }

        public String getTreePlantYear() {
            return treePlantYear;
        }

        public String getTreeTopSize() {
            return treeTopSize;
        }

        public String getTreeTrunkSize() {
            return treeTrunkSize;
        }
    }

    public class TreePopup implements GoogleMap.InfoWindowAdapter {


        @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker)
            {

            View v  = getLayoutInflater().inflate(R.layout.treeinfolayout, null);
           TextView l1 = (TextView) v.findViewById(R.id.treeTopSize);
            TextView l2 = (TextView) v.findViewById(R.id.treeTrunkSize);
            TextView l3 = (TextView) v.findViewById(R.id.treePlantYear);
                TextView name = (TextView) v.findViewById(R.id.baumname);

                TreeMeta data=metadata.get(marker.getPosition());
            l1.setText("Kronendurchmesser: "+data.getTreeTopSize());
            l2.setText("Stammumfang: "+data.getTreeTrunkSize());
            l3.setText("Pflanzjahr: "+data.getTreePlantYear());
            name.setText("Art: "+marker.getTitle());
            return v;
        }
    }

}
