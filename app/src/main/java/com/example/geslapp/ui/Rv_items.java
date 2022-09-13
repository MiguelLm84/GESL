package com.example.geslapp.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.geslapp.R;
import com.example.geslapp.core.adapters.RvItemAdapter;
import com.example.geslapp.core.clases.ConfigPreferences;
import com.example.geslapp.core.clases.Etqs_invent;
import com.example.geslapp.core.clases.Material_invent;
import com.example.geslapp.core.databaseInvent.Etqs_Invent_Local_DB;
import com.example.geslapp.core.databaseInvent.Fotos_Invent_Local_DB;
import com.example.geslapp.core.databaseInvent.Inventario_Local_DB;
import com.example.geslapp.core.databaseInvent.Material_Invent_Local_DB;
import com.example.geslapp.core.uploads.Upload_Etqs_Invent;
import com.example.geslapp.core.uploads.Upload_material_invent;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class Rv_items extends AppCompatActivity {

    Button butupload,butback;
    private static int id_invent;
    EditText edtmade;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rv_items);
        id_invent = getIntent().getIntExtra("id_invent",-1);
        RecyclerView rvitem = findViewById(R.id.rvItems);
        rvitem.setHasFixedSize(true);
        ArrayList<Material_invent> listaMaterial = fillMaterial();
        Material_Invent_Local_DB material_invent_local_db = new Material_Invent_Local_DB(getApplicationContext());
        material_invent_local_db.firstInsert(id_invent);
        RecyclerView.LayoutManager layoutManageritem = new LinearLayoutManager(getApplicationContext());
        rvitem.setLayoutManager(layoutManageritem);
        RecyclerView.Adapter<RvItemAdapter.MyViewHolder> rvadapteritem = new RvItemAdapter(getApplicationContext(), listaMaterial, id_invent, Rv_items.this);
        edtmade = findViewById(R.id.edtmadeitems);
        rvitem.setAdapter(rvadapteritem);
        butupload = findViewById(R.id.butUploadItems);
        checkData(id_invent);
        Inventario_Local_DB inventario_local_db =new Inventario_Local_DB(getApplicationContext());
        edtmade.setText(inventario_local_db.getMade(id_invent));
        butback = findViewById(R.id.butbackitems);

        butback.setOnClickListener(v -> onBackPressed());

        //end ONCLICK
        butupload.setOnClickListener(v -> {
            ConfigPreferences config = new ConfigPreferences();
            String IP = config.getIP(getApplicationContext());
            String REC = config.getRec(getApplicationContext());
            Inventario_Local_DB inventario_local_db1 = new Inventario_Local_DB(getApplicationContext());
            inventario_local_db1.updateState(id_invent);
            String made = edtmade.getText().toString();

            ArrayList<Material_invent> listaSendItems = new ArrayList<>();

            for(int i=0;i<listaMaterial.size();i++) {

                String itemname = listaMaterial.get(i).getDbName();
                Material_invent material_invent = material_invent_local_db.getAllData(id_invent,itemname);
                listaSendItems.add(material_invent);
            }

            for(int i = 0; i<listaSendItems.size();i++) {

                Response.Listener<String> respoListener = response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        int success = jsonResponse.getInt("success");
                        String request = jsonResponse.getString("message");

                        if (success == 1) {
                           // Toast.makeText(getApplicationContext(),"Insert realizado exitosamente " +request, Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(getApplicationContext(),"Ha currido un problema, volver a intentar-ITEMS"+response, Toast.LENGTH_SHORT).show();
                            Toast.makeText(getApplicationContext(),request, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(),"Ha currido un problema, volver a intentar-ITEMS"+response, Toast.LENGTH_SHORT).show();
                    }
                };

                Upload_material_invent upload_material_invent = new Upload_material_invent(respoListener,listaSendItems.get(i),IP,REC,id_invent,made);
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(upload_material_invent);

            }//END FOR

            //ETIQUETAS-------------------------------------------------------------------------
            Etqs_Invent_Local_DB etqs_invent_local_db = new Etqs_Invent_Local_DB(getApplicationContext());
            ArrayList<Etqs_invent> listaEtqs = new ArrayList<>();
            listaEtqs.addAll(etqs_invent_local_db.fillArray(id_invent, Rv_items.this));

            for(int i = 0; i<listaEtqs.size();i++) {

                Response.Listener<String> respoListener = response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        int success = jsonResponse.getInt("success");
                        String request = jsonResponse.getString("message");

                        if (success == 1) {
                            //Toast.makeText(getApplicationContext(),"Insert realizado exitosamente-ETQS " +request, Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(getApplicationContext(),"Ha currido un problema, volver a intentar-ETQ" +response, Toast.LENGTH_SHORT).show();
                            Toast.makeText(getApplicationContext(),request, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(),"Ha currido un problema, volver a intentar-ETQ" +response, Toast.LENGTH_SHORT).show();
                    }
                };
                Upload_Etqs_Invent upload_etqs_invent = new Upload_Etqs_Invent(respoListener,listaEtqs.get(i),IP,REC,id_invent,getApplicationContext());
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(upload_etqs_invent);

            }//END FOR

            Fotos_Invent_Local_DB fotos_invent_local_db = new Fotos_Invent_Local_DB(getApplicationContext());
            fotos_invent_local_db.getAllFotos(id_invent,getApplicationContext(),Rv_items.this);
            Toast.makeText(Rv_items.this, "Se han subido los datos", Toast.LENGTH_SHORT).show();
        });
    }

    private void checkData(int id_invent) {
        Inventario_Local_DB inventario_local_db = new Inventario_Local_DB(getApplicationContext());
        String estado = inventario_local_db.getState(id_invent);
        if(estado.equals("Abierto")) {

           butupload.setEnabled(false);
        }
    }

    public ArrayList<Material_invent> fillMaterial() {

        ArrayList<Material_invent> listaMaterial = new ArrayList<>();
        Material_invent clip_liso = new Material_invent("Clip liso para perfiles","clip_liso","","","","");
        listaMaterial.add(clip_liso);
        Material_invent clip_cruz = new Material_invent("Clip cruceta/mariposa","clip_cruz","","","","");
        listaMaterial.add(clip_cruz);
        Material_invent pie_10 = new Material_invent("Pie 10 cm","pie_10","","","","");
        listaMaterial.add(pie_10);
        Material_invent pie_15 = new Material_invent("Pie 15cm","pie_15","","","","");
        listaMaterial.add(pie_15);
        Material_invent bases = new Material_invent("Bases imantadas","bases","","","","");
        listaMaterial.add(bases);
        Material_invent pincho_pesc = new Material_invent("Pinchos pescadería","pincho_pesc","","","","");
        listaMaterial.add(pincho_pesc);
        Material_invent pincho_largo = new Material_invent("Pinchos de cruceta largos","pincho_largo","","","","");
        listaMaterial.add(pincho_largo);
        Material_invent pincho_corto = new Material_invent("Pinchos de cruceta cortos","pincho_corto","","","","");
        listaMaterial.add(pincho_corto);
        Material_invent saca_clips = new Material_invent("Saca clips","saca_clips","","","","");
        listaMaterial.add(saca_clips);
        Material_invent perfiles = new Material_invent("Perfiles","perfiles","","","","");
        listaMaterial.add(perfiles);
        Material_invent term_perfiles = new Material_invent("Terminación perfiles","term_perfiles","","","","");
        listaMaterial.add(term_perfiles);
        Material_invent pinzas = new Material_invent("Pinzas","pinzas","","","","");
        listaMaterial.add(pinzas);
        Material_invent eleva_metal = new Material_invent("Elevadores de metal","eleva_metal","","","","");
        listaMaterial.add(eleva_metal);
        Material_invent eleva_plastic = new Material_invent("Elevadores plásticos","eleva_plastic","","","","");
        listaMaterial.add(eleva_plastic);
        return listaMaterial;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.zoom_back_in, R.anim.zoom_back_out);
    }
}
