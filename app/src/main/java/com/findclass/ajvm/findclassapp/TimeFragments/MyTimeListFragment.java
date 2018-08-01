package com.findclass.ajvm.findclassapp.TimeFragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.findclass.ajvm.findclassapp.Adapter.MySubjectsAdapter;
import com.findclass.ajvm.findclassapp.Adapter.MyTimesAdapter;
import com.findclass.ajvm.findclassapp.Helper.RecyclerItemClickListener;
import com.findclass.ajvm.findclassapp.Model.Subject;
import com.findclass.ajvm.findclassapp.R;
import com.findclass.ajvm.findclassapp.SubjectActivities.MySubjectInfoActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyTimeListFragment extends Fragment {

    private RecyclerView recyclerViewMyTimesList;
    private MyTimesAdapter adapter;
    private ArrayList<String> myStartTimeList = new ArrayList<>();
    private ArrayList<String> myEndTimeList = new ArrayList<>();
    private DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference timeRef;
    private ValueEventListener valueEventListener;
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    public MyTimeListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_my_calendar_professor, container, false);

        recyclerViewMyTimesList = view.findViewById(R.id.myTimesRecyclerView);

        adapter = new MyTimesAdapter(myStartTimeList,myEndTimeList, getActivity());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewMyTimesList.setLayoutManager(layoutManager);
        recyclerViewMyTimesList.setHasFixedSize(true);
        recyclerViewMyTimesList.setAdapter(adapter);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        //mySubjectsList = new ArrayList<>();
        retrieveMySubjects();
    }

    @Override
    public void onStop() {
        super.onStop();
        timeRef.removeEventListener(valueEventListener);
    }

    public void retrieveMySubjects(){
        myStartTimeList.clear();



        DatabaseReference calendarList = rootRef.child("availability");

        valueEventListener = calendarList
                .child(auth.getCurrentUser().getUid())
                .addValueEventListener(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                final ArrayList<String> subjectsId = new ArrayList<>();
                                for (DataSnapshot d: dataSnapshot.getChildren()){
                                    subjectsId.add(d.getKey());
                                }

                                timeRef = rootRef.child("startTime");
                                valueEventListener = timeRef.orderByChild("time").addValueEventListener(
                                        new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                for (DataSnapshot subject: dataSnapshot.getChildren()){
                                                    if (subjectsId.contains(subject.getKey())){
                                                        String thisStartTime = subject.getValue(Subject.class);
                                                        myStartTimeList.add(thisSubject);
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

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                //
                            }
                        }
                );

        /*subjectsRef = rootRef.child("subjects");
        valueEventListener = subjectsRef.orderByChild("name").addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot subject: dataSnapshot.getChildren()){
                            Subject thisSubject = subject.getValue(Subject.class);
                            mySubjectsList.add(thisSubject);
                        }

                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //
                    }
                }
        );*/
    }

}
