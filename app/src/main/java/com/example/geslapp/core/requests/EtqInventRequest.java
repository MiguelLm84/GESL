package com.example.geslapp.core.requests;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

public class EtqInventRequest extends StringRequest {
    public EtqInventRequest(Response.Listener<String> listener, String IP, String REC) {
        super(Method.POST, "http://"+IP+"/gesl/"+REC+"/datos_etq_invent.php", listener, null);

    }
}
