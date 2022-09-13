package com.example.geslapp.ui;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.example.geslapp.R;
import com.example.geslapp.core.clases.CheckConnection;
import com.example.geslapp.core.clases.ConfigPreferences;
import com.example.geslapp.core.database.Login_Local_DB;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class LoginActivity extends AppCompatActivity {

    EditText etqName,etqPass;
    Button b2, btnLogin;

    private static final int contlog = 0;

    private static String IP,REC;
    private final ConfigPreferences config = new ConfigPreferences();
    Login_Local_DB login_local_db = new Login_Local_DB(this);

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        overridePendingTransition(R.anim.fade_in,R.anim.fade_in);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //Asociamos las variables al nombre que tienen en el layout
        btnLogin = (Button) findViewById(R.id.btnLogin);
        etqName = (EditText) findViewById(R.id.txtName);
        etqPass = (EditText) findViewById(R.id.txtPass);
        b2 = (Button) findViewById(R.id.btnRegister);

        int inicio = config.getFirstTimeRun(getApplicationContext());
        IP = config.getIP(getApplicationContext());
        if(inicio == 0 || IP == null) {
            showconfingdialog();
            IP = config.getIP(getApplicationContext());
            REC = config.getRec(getApplicationContext());
        }
        IP = config.getIP(getApplicationContext());
        REC = config.getRec(getApplicationContext());

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.REQUEST_INSTALL_PACKAGES)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.REQUEST_INSTALL_PACKAGES},101);
        }

        //Listener del boton Register
        b2.setOnClickListener(this::Registro);
        btnLogin.setOnClickListener(v -> connection());
    }

    private void connection() {

        new CheckConnection().execute("http://"+IP+"/gesl/"+REC+"/");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String serverResponse = new CheckConnection().getServerResponse();

        new CheckConnection().setServer_response();
        String password;
        String username;
        if (serverResponse != null) {

            if (etqName.getText().toString().isEmpty()) {
                Toast.makeText(getApplicationContext(), "Campo Nombre vacío", Toast.LENGTH_LONG).show();
            } else {
                if (etqPass.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Campo password vacío", Toast.LENGTH_LONG).show();
                }
            }
            username = etqName.getText().toString().trim();
            password = etqPass.getText().toString().trim();
            //  Toast.makeText(getApplicationContext(), password, Toast.LENGTH_SHORT).show();
            if (etqName.length() == 0 || etqPass.length() == 0) {

            } else {

                Intent t = new Intent(LoginActivity.this,ActivityCarga.class);
                t.putExtra("username", username);
                t.putExtra("password", password);
                startActivity(t);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);

                //Inventario_Local_DB inventario_local_db = new Inventario_Local_DB(getApplicationContext());
                //inventario_local_db.fillInventario(getApplicationContext(),username);
            }
        } else {
            username = etqName.getText().toString().trim();
            password = etqPass.getText().toString().trim();
            password = getMD5(password);


            if (login_local_db.checkUser(username, password)) {
                config.setCon(getApplicationContext(),false);
                Intent t = new Intent(LoginActivity.this, MenuActivity.class);
                t.putExtra("username", username);
                startActivity(t);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                Toast.makeText(getApplicationContext(), "LOGIN LOCAL CORRECTO", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "LOGIN LOCAL INCORRECTO", Toast.LENGTH_SHORT).show();
            }

            Toast.makeText(getApplicationContext(), "NO SE HA PODIDO ESTABLECER LA CONEXION CON EL HOST, TRABAJANDO EN LOCAL", Toast.LENGTH_SHORT).show();
        }
    }

    public void Registro(View v){
        Intent intent4 = new Intent (this, RegistroActivity.class);
        startActivity(intent4);
    }

    public static String getMD5(String password) {

        final byte[] defaultBytes = password.getBytes();
        try {
            final MessageDigest md5MsgDigest = MessageDigest.getInstance("MD5");
            md5MsgDigest.reset();
            md5MsgDigest.update(defaultBytes);
            final byte[] messageDigest = md5MsgDigest.digest();

            final StringBuilder hexString = new StringBuilder();
            for (final byte element : messageDigest) {
                final String hex = Integer.toHexString(0xFF & element);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            password = hexString + "";
        } catch (final NoSuchAlgorithmException nsae) {
            nsae.printStackTrace();
        }
        return password;
    }

    @SuppressLint("SetTextI18n")
    private void showconfingdialog() {

        final Dialog dialog = new Dialog(LoginActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomconfig);
        dialog.setCancelable(false);

        EditText edtip = dialog.findViewById(R.id.edtip);
        EditText edtrec = dialog.findViewById(R.id.edtrecurso);
        TextView txtconfig = dialog.findViewById(R.id.txtconfig);
        txtconfig.setText("CONFIGURACION INICIAL");
        txtconfig.setTextSize(24);
        Button butsave = dialog.findViewById(R.id.butCsave);
        TextView txtversion = dialog.findViewById(R.id.txtVconfig);

        butsave.setOnClickListener(v -> {
            String ip = edtip.getText().toString();
            String rec = edtrec.getText().toString();

            if(isValidIPAddress(ip)) {
                IP = ip;
                if(rec.equals("")) rec = "ASDFAFDS";
                REC = rec;
                new CheckConnection().cancel(true);
                new CheckConnection().execute("http://"+ip+"/gesl/"+rec+"/");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String serverResponse = new CheckConnection().getServerResponse();

                if (serverResponse == null) {
                    Toast.makeText(LoginActivity.this, "No hay conexión con el host", Toast.LENGTH_SHORT).show();
                    config.createPreferences(getApplicationContext(),null,null);

                } else {
                    config.createPreferences(getApplicationContext(),ip,rec);
                    Toast.makeText(LoginActivity.this, "Configuración guardada", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
            else Toast.makeText(LoginActivity.this, "Datos erróneos", Toast.LENGTH_SHORT).show();
        });
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.show();
    }

    public static boolean isValidIPAddress(String ip) {

        String zeroTo255
                = "(\\d{1,2}|(0|1)\\"
                + "d{2}|2[0-4]\\d|25[0-5])";

        String regex
                = zeroTo255 + "\\."
                + zeroTo255 + "\\."
                + zeroTo255 + "\\."
                + zeroTo255;

        Pattern p = Pattern.compile(regex);

        if (ip == null) {
            return false;
        }

        Matcher m = p.matcher(ip);

        return m.matches();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

