package com.example.crudstatus;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class ListaPedidosActivity extends AppCompatActivity {

    PedidoDBHelper dbHelper;
    ListView listViewPedidos;
    ArrayList<String> pedidosList;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_pedidos);

        listViewPedidos = findViewById(R.id.listViewPedidos);
        dbHelper = new PedidoDBHelper(this);
        pedidosList = new ArrayList<>();

        // Carregar os pedidos no ListView
        carregarPedidos();

        // Configurar o ArrayAdapter para exibir a lista de pedidos
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, pedidosList);
        listViewPedidos.setAdapter(adapter);

        // Configurar listener para clique longo para excluir pedido
        listViewPedidos.setOnItemLongClickListener((parent, view, position, id) -> {
            String item = pedidosList.get(position);
            String[] partes = item.split(" - ");
            final int pedidoId = Integer.parseInt(partes[0].replace("ID: ", ""));

            // Exibir diálogo de confirmação para excluir
            new AlertDialog.Builder(ListaPedidosActivity.this)
                    .setTitle("Excluir Pedido")
                    .setMessage("Tem certeza que deseja excluir o pedido com ID " + pedidoId + "?")
                    .setPositiveButton("Sim", (dialog, which) -> {
                        // Excluir pedido do banco de dados e atualizar a lista
                        dbHelper.deletarPedido(pedidoId);
                        pedidosList.remove(position);
                        adapter.notifyDataSetChanged();
                        Toast.makeText(ListaPedidosActivity.this, "Pedido " + pedidoId + " excluído", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Não", null)
                    .show();

            return true;
        });

        // Botão para voltar para a MainActivity
        Button buttonVoltar = findViewById(R.id.buttonVoltar);
        buttonVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ir para MainActivity
                Intent intent = new Intent(ListaPedidosActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // Opcional: Finaliza a activity atual para não ficar empilhada
            }
        });
    }

    private void carregarPedidos() {
        Cursor cursor = dbHelper.listarPedidos();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String status = cursor.getString(cursor.getColumnIndexOrThrow("status"));
                pedidosList.add("ID: " + id + " - Status: " + status);
            }
            cursor.close();
        }
    }
}
