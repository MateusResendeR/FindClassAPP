package com.findclass.ajvm.findclassapp.SubjectFragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.findclass.ajvm.findclassapp.Adapter.MySubjectsAdapter;
import com.findclass.ajvm.findclassapp.R;

import java.util.ArrayList;

import com.findclass.ajvm.findclassapp.Model.Subject;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class MySubjectListFragment extends Fragment {

    private RecyclerView recyclerViewMySubjectsList;
    private MySubjectsAdapter adapter;
    private ArrayList<Subject> mySubjectsList = new ArrayList<>();
    private DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference subjectsRef;
    private ValueEventListener valueEventListener;
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    public MySubjectListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_subject_list, container, false);

        recyclerViewMySubjectsList = view.findViewById(R.id.mySubjectsRecyclerView);

        adapter = new MySubjectsAdapter(mySubjectsList, getActivity());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewMySubjectsList.setLayoutManager(layoutManager);
        recyclerViewMySubjectsList.setHasFixedSize(true);
        recyclerViewMySubjectsList.setAdapter(adapter);


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
        subjectsRef.removeEventListener(valueEventListener);
    }

    public void retrieveMySubjects(){
        mySubjectsList.clear();



        DatabaseReference professorSubjectsRef = rootRef.child("professorSubjects");

        valueEventListener = professorSubjectsRef
                .child(auth.getCurrentUser().getUid())
                .addValueEventListener(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                final ArrayList<String> subjectsId = new ArrayList<>();
                                for (DataSnapshot d: dataSnapshot.getChildren()){
                                    subjectsId.add(d.getKey());
                                }

                                subjectsRef = rootRef.child("subjects");
                                valueEventListener = subjectsRef.orderByChild("name").addValueEventListener(
                                        new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                for (DataSnapshot subject: dataSnapshot.getChildren()){
                                                    if (subjectsId.contains(subject.getKey())){
                                                        Subject thisSubject = subject.getValue(Subject.class);
                                                        mySubjectsList.add(thisSubject);
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
                            mySubjectsList.addTime(thisSubject);
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
