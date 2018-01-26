package com.example.pablo.perrsa_admin;

import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.pablo.perrsa_admin.Objetos.Pedido;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ListActivity {

    ListView listViewPedidos;
    List<String> pedidos;

    private DatabaseReference mDatabase;
    private Button button;
    private ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_lista_pedidos);
        mDatabase = FirebaseDatabase.getInstance().getReference("pedidos");
        pedidos = new ArrayList<String>();


        listViewPedidos = getListView();

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //clearing the previous artist list
                pedidos.clear();

                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting artist
                    String pedido = postSnapshot.getValue(Pedido.class).toString();
                    //adding artist to the list
                    pedidos.add(pedido);
                }

                //creating adapter
                adapter=new ArrayAdapter<String>(getApplicationContext() ,android.R.layout.simple_list_item_1 ,pedidos);
//                PedidosList pedidosList = new PedidosList(getActivity(), pedidos);
                //attaching adapter to the listview
                listViewPedidos.setAdapter(adapter);
                setListAdapter(adapter);



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        setListAdapter(adapter);
        listViewPedidos = getListView();

        listViewPedidos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, final View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getApplication());
                builder.setMessage("¿Borrar éste pedido?")
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                               String pedido = pedidos.get(position);
                               pedidos.remove(pedido);
                               adapter.notifyDataSetChanged();
                               dialog.dismiss();
                            }
                        })
                        .setNegativeButton("cancelar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });

                return true;
            }
        });

        listViewPedidos = getListView();

        listViewPedidos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getApplication());
                builder.setMessage("¿Borrar éste pedido?")
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String pedido = pedidos.get(position);
                                pedidos.remove(pedido);
                                adapter.notifyDataSetChanged();
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("cancelar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });


            }
        });

    }


}
