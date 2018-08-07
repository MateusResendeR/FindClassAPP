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
public class MyScheduleStudentFragment extends Fragment {
    private RecyclerView recyclerViewMyScheduleList;
    private MyScheduleProfessorAdapter adapter;
    private DatabaseReference schedulesRef;
    private DatabaseReference rootRef;
    private FirebaseAuth auth;
    private ArrayList<ScheduleObject> myScheduleObjects = new ArrayList<>();
    private ValueEventListener valueEventListener;
    //private ArrayList<Schedule> mySchedules = new ArrayList<>();

    public MyScheduleStudentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_schedule_student, container, false);

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

        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(getActivity());
        recyclerViewMyScheduleList.setLayoutManager(layoutManager1);
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

        valueEventListener = schedulesRef.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            if (dataSnapshot1.hasChild(auth.getCurrentUser().getUid())) {
                                for (DataSnapshot dataSnapshot2 : dataSnapshot1.child(auth
                                        .getCurrentUser()
                                        .getUid()).getChildren()) {
                                    array.add(dataSnapshot2);
                                }

                                for (final DataSnapshot schedule : array) {
                                    final DatabaseReference usersRef = rootRef.child("users");
                                    final DatabaseReference subjectRef = rootRef.child("subjects");
                                    DatabaseReference datatimeRef = rootRef.child("datetime");

                                    final User professor = new User();
                                    final User student = new User();
                                    final Subject subject = new Subject();
                                    adapter.notifyDataSetChanged();

                                    Log.e("DEBUG1",schedule.child("professor_id").getValue(String.class));

                                    usersRef
                                            .child(schedule.child("professor_id").getValue(String.class))
                                            .addValueEventListener(
                                                    new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            Log.e("DEBUG2",dataSnapshot.toString());
                                                            professor.setUser(dataSnapshot.getValue(User.class));



                                                            Log.e("DEBUG3",schedule.child("student_id").getValue(String.class));
                                                            usersRef
                                                                    .child(schedule.child("student_id").getValue(String.class))
                                                                    .addValueEventListener(
                                                                            new ValueEventListener() {
                                                                                @Override
                                                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                    Log.e("DEBUG4",dataSnapshot.toString());
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


                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //
                    }
                }
        );
    }

}
