package com.example.ito.codigoqr;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.blikoon.qrcodescanner.QrCodeActivity;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static com.android.volley.Request.Method.POST;


public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_QR_SCAN = 101;
    String lectura;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View v) {
        Intent i = new Intent(MainActivity.this, QrCodeActivity.class);
        startActivityForResult(i, REQUEST_CODE_QR_SCAN);
    }

   public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != Activity.RESULT_OK) {
            Toast.makeText(getApplicationContext(), "No se pudo obtener una respuesta", Toast.LENGTH_SHORT).show();
            String resultado = data.getStringExtra("com.blikoon.qrcodescanner.error_decoding_image");
            if (resultado != null) {
                Toast.makeText(getApplicationContext(), "No se pudo escanear el código QR", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        if (requestCode == REQUEST_CODE_QR_SCAN) {
            if (data != null) {
                lectura = data.getStringExtra("com.blikoon.qrcodescanner.got_qr_scan_relult");
                Toast.makeText(getApplicationContext(), "Leído: " + lectura, Toast.LENGTH_SHORT).show();
                ejecutarServicio("192.168.1.65:80/ito/insertar_codigo.php");

            }
        }
    }

    private void ejecutarServicio(String URL){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(),"OPERACION EXITOSA",Toast.LENGTH_SHORT).show();
                }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError{
                       Map<String, String> parametros = new HashMap<String, String>();
                       parametros.put("codigo",lectura.toString());
                       return parametros;
                }
            };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}

