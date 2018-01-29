package com.example.pablo.perrsa_admin;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.pablo.perrsa_admin.Objetos.Pedido;
import com.google.firebase.auth.FirebaseAuth;
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

    private DatabaseReference mDatabasePedidos;
    private DatabaseReference mDatabaseUsuarios;
    private Button button;
    private ArrayAdapter<String> adapter;

    AlertDialog.Builder builder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_lista_pedidos);


        mDatabasePedidos = FirebaseDatabase.getInstance().getReference("pedidos");
        mDatabaseUsuarios = FirebaseDatabase.getInstance().getReference("users");

        pedidos = new ArrayList<Pedido>();
        pedidosString = new ArrayList<String>();

        builder = new AlertDialog.Builder(this);

        listViewPedidos = findViewById(R.id.listview);

        mDatabasePedidos.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //clearing the previous artist list
                pedidos.clear();
                pedidosString.clear();

                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting artist
                    Pedido pedido = postSnapshot.getValue(Pedido.class);
                    String pedidoString = pedido.toString();
                    //adding artist to the list
                    pedidos.add(pedido);
                    pedidosString.add(pedidoString);
                }

                //creating adapter
                adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, pedidosString);
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
                builder.setTitle("¿Desea borrar éste pedido?");
                builder.setMessage(pedidos.get(position).getPedidoDetailString())
                        .setPositiveButton("Aceptar", (dialog, id1) -> {
                            String pedidoString = pedidosString.get(position);
                            Pedido pedido = pedidos.get(position);

                            pedidosString.remove(pedidoString);
                            pedidos.remove(pedido);
                            mDatabaseUsuarios.child(pedido.getUserId()).child("pedidos").child(pedido.getPushId()).removeValue();
                            mDatabasePedidos.child(pedido.getPushId()).removeValue();


                            adapter.notifyDataSetChanged();
                            dialog.dismiss();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_menu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            FirebaseAuth.getInstance().signOut();
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }


}
