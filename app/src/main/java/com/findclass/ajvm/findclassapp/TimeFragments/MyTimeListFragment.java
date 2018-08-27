package com.findclass.ajvm.findclassapp.TimeFragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.findclass.ajvm.findclassapp.Adapter.MyTimesAdapter;
import com.findclass.ajvm.findclassapp.Helper.RecyclerItemClickListener;
import com.findclass.ajvm.findclassapp.Model.Time;
import com.findclass.ajvm.findclassapp.R;

import java.util.ArrayList;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyTimeListFragment extends Fragment {

    private RecyclerView recyclerViewMyTimeList;
    private MyTimesAdapter adapter;
    private ArrayList<Time> myTimeList = new ArrayList<>();
    private DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference timesRef;
    private ValueEventListener valueEventListener;
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    public MyTimeListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_time_list, container, false);

        recyclerViewMyTimeList = view.findViewById(R.id.myTimesRecyclerView);

        adapter = new MyTimesAdapter(myTimeList, getActivity());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewMyTimeList.setLayoutManager(layoutManager);
        recyclerViewMyTimeList.setHasFixedSize(true);
        recyclerViewMyTimeList.setAdapter(adapter);

        recyclerViewMyTimeList.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getActivity(),
                        recyclerViewMyTimeList,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                //Ainda lugar nenhum
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {
                                //
                            }

                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                //
                            }
                        }
                )
        );

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        //myTimeList = new ArrayList<>();
        retrieveMyTimes();
    }

    @Override
    public void onStop() {
        super.onStop();
        timesRef.removeEventListener(valueEventListener);
    }

    public void retrieveMyTimes(){
        myTimeList.clear();

        DatabaseReference professorTimesRef = rootRef.child("availability");

        valueEventListener = professorTimesRef
                .child(auth.getCurrentUser().getUid())
                .child("times")
                .addValueEventListener(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                final ArrayList<String> timesId = new ArrayList<>();
                                for (DataSnapshot d: dataSnapshot.getChildren()){
                                    timesId.add(d.getKey());
                                }

                                retrieveTimes(timesId);

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                //
                            }
                        }
                );

        
    }

    public void retrieveTimes(final ArrayList<String> timesId){
        timesRef = rootRef.child("availability").child(auth.getCurrentUser().getUid()).child("times");
        valueEventListener = timesRef.orderByChild("startTime").addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot time: dataSnapshot.getChildren()){
                            if (timesId.contains(time.getKey())){
                                Time thisTime = time.getValue(Time.class);
                                myTimeList.add(thisTime);
                            }
                        }

                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //
                    }
                }
        );
    }

}
