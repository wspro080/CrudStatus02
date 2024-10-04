package com.example.crudstatus;

import android.database.Cursor;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    PedidoDBHelper dbHelper;
    TextView textViewStatus;
    EditText editTextId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new PedidoDBHelper(this);
        textViewStatus = findViewById(R.id.textViewStatus);
        editTextId = findViewById(R.id.editTextId);

        // Botão para iniciar pedido
        Button buttonIniciar = findViewById(R.id.buttonIniciar);
        buttonIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long id = dbHelper.inserirPedido("pedido iniciado");
                textViewStatus.setText("Pedido iniciado com ID: " + id);
            }
        });

        // Botão para atualizar status do pedido
        Button buttonAtualizar = findViewById(R.id.buttonAtualizar);
        buttonAtualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = Integer.parseInt(editTextId.getText().toString());
                dbHelper.atualizarPedido(id, "pedido em andamento");
                textViewStatus.setText("Status do pedido " + id + " atualizado para 'pedido em andamento'");
            }
        });

        // Botão para buscar pedido
        Button buttonBuscar = findViewById(R.id.buttonBuscar);
        buttonBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = Integer.parseInt(editTextId.getText().toString());
                Cursor cursor = dbHelper.lerPedido(id);
                if (cursor != null && cursor.moveToFirst()) {
                    String status = cursor.getString(cursor.getColumnIndexOrThrow("status"));
                    textViewStatus.setText("Status do pedido " + id + ": " + status);
                } else {
                    textViewStatus.setText("Pedido não encontrado");
                }
                cursor.close();
            }
        });

        // Botão para deletar pedido
        Button buttonDeletar = findViewById(R.id.buttonDeletar);
        buttonDeletar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = Integer.parseInt(editTextId.getText().toString());
                int deletedRows = dbHelper.deletarPedido(id);
                if (deletedRows > 0) {
                    textViewStatus.setText("Pedido " + id + " deletado");
                } else {
                    textViewStatus.setText("Pedido não encontrado");
                }
            }
        });

        // Botão para ver lista de pedidos
        Button buttonVerPedidos = findViewById(R.id.buttonVerPedidos);
        buttonVerPedidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListaPedidosActivity.class);
                startActivity(intent);
            }
        });
        Button buttonEmRotaDeEntrega = findViewById(R.id.buttonEmRotaDeEntrega);
        buttonEmRotaDeEntrega.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = Integer.parseInt(editTextId.getText().toString());
                dbHelper.atualizarPedido(id, "em rota de entrega");
                textViewStatus.setText("Status do pedido " + id + " atualizado para 'em rota de entrega'");
            }
        });
    }
}