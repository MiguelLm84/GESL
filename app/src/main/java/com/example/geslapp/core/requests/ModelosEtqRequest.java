package com.example.geslapp.core.requests;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

public class ModelosEtqRequest extends StringRequest {
    public ModelosEtqRequest(Response.Listener<String> listener, String IP, String REC) {
        super(Method.POST, "http://"+IP+"/gesl/"+REC+"/datos_modelos_etq.php", listener, null);

    }
}
