package com.example.pablo.perrsa_admin;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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

public class MainActivity extends AppCompatActivity {

    ListView listViewPedidos;
    List<Pedido> pedidos;
    List<String> pedidosString;

    private DatabaseReference mDatabase;
    private Button button;
    private ArrayAdapter<String> adapter;

    AlertDialog.Builder builder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_lista_pedidos);
        mDatabase = FirebaseDatabase.getInstance().getReference("pedidos");

        pedidos = new ArrayList<Pedido>();
        pedidosString = new ArrayList<String>();

        builder = new AlertDialog.Builder(this);

        listViewPedidos = findViewById(R.id.listview);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //clearing the previous artist list
                pedidos.clear();

                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting artist
                    Pedido pedido = postSnapshot.getValue(Pedido.class);
                    String pedidoString = pedido.toString() + " " + pedido.getProductosString();
                    //adding artist to the list
                    pedidos.add(pedido);
                    pedidosString.add(pedidoString);
                }

                //creating adapter
                adapter=new ArrayAdapter<String>(getApplicationContext() ,android.R.layout.simple_list_item_1 ,pedidosString);
//                PedidosList pedidosList = new PedidosList(getActivity(), pedidos);
                //attaching adapter to the listview
                listViewPedidos.setAdapter(adapter);



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        listViewPedidos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, final View view, final int position, long id) {

                builder.setMessage("¿Borrar éste pedido?")
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                               String pedido = pedidosString.get(position);
                               pedidosString.remove(pedido);
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
                AlertDialog dialog = builder.create();
                dialog.show();

                return true;
            }
        });



    }


}
