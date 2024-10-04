package com.example.crudstatus;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PedidoDBHelper extends SQLiteOpenHelper {

    // Nome e versão do banco de dados
    private static final String DATABASE_NAME = "pedidos.db";
    private static final int DATABASE_VERSION = 1;

    // Nome da tabela e colunas
    private static final String TABLE_PEDIDO = "pedido";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_STATUS = "status";

    // SQL para criação da tabela
    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_PEDIDO + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_STATUS + " TEXT);";

    public PedidoDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PEDIDO);
        onCreate(db);
    }

    // Método para inserir um pedido
    public long inserirPedido(String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_STATUS, status);
        return db.insert(TABLE_PEDIDO, null, values);
    }

    // Método para atualizar o status de um pedido
    public int atualizarPedido(int id, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_STATUS, status);
        return db.update(TABLE_PEDIDO, values, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }

    // Método para ler o status de um pedido pelo ID
    public Cursor lerPedido(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_PEDIDO, new String[]{COLUMN_ID, COLUMN_STATUS}, COLUMN_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);
    }

    // Método para deletar um pedido
    public int deletarPedido(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_PEDIDO, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }

    // Método para obter todos os pedidos
    public Cursor listarPedidos() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_PEDIDO, new String[]{COLUMN_ID, COLUMN_STATUS}, null, null, null, null, null);
    }
    // Método para atualizar o status de um pedido
    public int atualizarStatusPedido(int clienteId, String novoStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("status", novoStatus);

        // Atualizar o status do pedido baseado na ID do cliente
        return db.update("pedido", values, "id = ?", new String[]{String.valueOf(clienteId)});
    }
}