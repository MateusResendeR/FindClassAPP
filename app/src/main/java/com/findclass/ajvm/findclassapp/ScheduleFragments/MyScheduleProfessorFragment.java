package com.findclass.ajvm.findclassapp.ScheduleFragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.findclass.ajvm.findclassapp.Adapter.MyScheduleProfessorAdapter;
import com.findclass.ajvm.findclassapp.Model.ScheduleObject;
import com.findclass.ajvm.findclassapp.Model.Subject;
import com.findclass.ajvm.findclassapp.Model.User;
import com.findclass.ajvm.findclassapp.R;
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
public class MyScheduleProfessorFragment extends Fragment {
    private RecyclerView recyclerViewMyScheduleList;
    private MyScheduleProfessorAdapter adapter;
    private DatabaseReference schedulesRef;
    private DatabaseReference rootRef;
    private FirebaseAuth auth;
    private ArrayList<ScheduleObject> myScheduleObjects = new ArrayList<>();
    private ValueEventListener valueEventListener;
    //private ArrayList<Schedule> mySchedules = new ArrayList<>();

    public MyScheduleProfessorFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_schedule_professor, container, false);

        auth = FirebaseAuth.getInstance();
        rootRef = FirebaseDatabase.getInstance().getReference();
        schedulesRef = rootRef.child("schedule");

        recyclerViewMyScheduleList = view.findViewById(R.id.recyclerViewMySchedule);

        for (ScheduleObject s:myScheduleObjects){
            Log.d("teste",s.getSubject().getName());
            Log.d("teste",s.getProfessor().getName());
            Log.d("teste",s.getStudent().getName());

        }

        adapter = new MyScheduleProfessorAdapter(myScheduleObjects, getActivity());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewMyScheduleList.setLayoutManager(layoutManager);
        recyclerViewMyScheduleList.setHasFixedSize(true);
        recyclerViewMyScheduleList.setAdapter(adapter);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        retriveMySchedules();
    }

    @Override
    public void onStop() {
        super.onStop();
        schedulesRef.removeEventListener(valueEventListener);
    }

    public void retriveMySchedules(){

        myScheduleObjects.clear();
        final ArrayList<DataSnapshot>  array = new ArrayList<>();

        valueEventListener = schedulesRef.child(auth.getCurrentUser().getUid()).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot dado1: dataSnapshot.getChildren()){
                            for (DataSnapshot dado2: dado1.getChildren()){
                               array.add(dado2);
                            }adapter.notifyDataSetChanged();

                            for (final DataSnapshot schedule: array){
                                final DatabaseReference usersRef = rootRef.child("users");
                                final DatabaseReference subjectRef = rootRef.child("subjects");
                                DatabaseReference datatimeRef = rootRef.child("datetime");

                                final User professor = new User();
                                final User student = new User();
                                final Subject subject = new Subject();
                                adapter.notifyDataSetChanged();
                                usersRef
                                        .child(schedule.child("professor_id").getValue(String.class))
                                        .addValueEventListener(
                                        new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                professor.setUser(dataSnapshot.getValue(User.class));

                                                usersRef
                                                        .child(schedule.child("student_id").getValue(String.class))
                                                        .addValueEventListener(
                                                                new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                                        student.setUser(dataSnapshot.getValue(User.class));

                                                                        subjectRef
                                                                                .child(schedule.child("subject_id").getValue(String.class))
                                                                                .addValueEventListener(
                                                                                        new ValueEventListener() {
                                                                                            @Override
                                                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                                subject.setSubject(dataSnapshot.getValue(Subject.class));

                                                                                                if(professor.getName() != null &&
                                                                                                        student.getName() != null &&
                                                                                                        subject.getName() != null){
                                                                                                    ScheduleObject obj = new ScheduleObject(professor,student,subject);
                                                                                                    myScheduleObjects.add(obj);

                                                                                                    adapter.notifyDataSetChanged();
                                                                                                }
                                                                                                adapter.notifyDataSetChanged();
                                                                                            }

                                                                                            @Override
                                                                                            public void onCancelled(DatabaseError databaseError) {
                                                                                                //
                                                                                            }
                                                                                        }
                                                                                );
                                                                        adapter.notifyDataSetChanged();
                                                                    }


                                                                    @Override
                                                                    public void onCancelled(DatabaseError databaseError) {
                                                                        //
                                                                    }
                                                                }
                                                        );
                                                adapter.notifyDataSetChanged();


                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                                //
                                            }
                                        }
                                );
                                adapter.notifyDataSetChanged();


                            }adapter.notifyDataSetChanged();
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //
                    }
                }
        );

        adapter.notifyDataSetChanged();

    }

}
