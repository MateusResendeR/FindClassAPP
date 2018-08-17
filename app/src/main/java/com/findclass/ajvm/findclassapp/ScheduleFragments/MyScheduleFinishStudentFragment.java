package com.findclass.ajvm.findclassapp.ScheduleFragments;


import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.findclass.ajvm.findclassapp.Adapter.MyScheduleProfessorAdapter;
import com.findclass.ajvm.findclassapp.Helper.RecyclerItemClickListener;
import com.findclass.ajvm.findclassapp.Model.ScheduleObject;
import com.findclass.ajvm.findclassapp.Model.Subject;
import com.findclass.ajvm.findclassapp.Model.User;
import com.findclass.ajvm.findclassapp.R;
import com.findclass.ajvm.findclassapp.menuActivities.RatingProfessorActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyScheduleFinishStudentFragment extends Fragment {
    private RecyclerView recyclerViewMyScheduleList;
    private MyScheduleProfessorAdapter adapter;
    private DatabaseReference schedulesRef;
    private DatabaseReference rootRef;
    private FirebaseAuth auth;
    private ArrayList<ScheduleObject> myScheduleObjects = new ArrayList<>();
    private ValueEventListener valueEventListener;


    public MyScheduleFinishStudentFragment() {
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

        adapter = new MyScheduleProfessorAdapter(myScheduleObjects);

        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(getActivity());
        recyclerViewMyScheduleList.setLayoutManager(layoutManager1);
        recyclerViewMyScheduleList.setHasFixedSize(true);
        recyclerViewMyScheduleList.setAdapter(adapter);
        //clique
        recyclerViewMyScheduleList.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getActivity(),
                        recyclerViewMyScheduleList,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                if(myScheduleObjects.get(position).getRating().equals("0")){
                                    Intent intent = new Intent(getContext(),RatingProfessorActivity.class);
                                    intent.putExtra("user", myScheduleObjects.get(position).getProfessor());
                                    intent.putExtra("subject",myScheduleObjects.get(position).getSubject());
                                    intent.putExtra("schedule",myScheduleObjects.get(position));
                                    startActivity(intent);
                                }
                                else{
                                    Toast.makeText(getActivity(), "Aula já avaliada!", Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                            }

                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            }
                        }
                )
        );

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
                                    final String key = schedule.getKey();

                                    if(schedule.child("finish").getValue(Integer.class) == 1){
                                        final String rating = schedule.child("rating").getValue().toString();
                                        final DatabaseReference usersRef = rootRef.child("users");
                                        final DatabaseReference subjectRef = rootRef.child("subjects");
                                        DatabaseReference datatimeRef = rootRef.child("datetime");

                                        final User professor = new User();
                                        final User student = new User();
                                        final Subject subject = new Subject();


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
                                                                                                                    ScheduleObject obj = new ScheduleObject(professor, student, subject, rating,key);
                                                                                                                    myScheduleObjects.add(obj);



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



                                                            }

                                                            @Override
                                                            public void onCancelled(DatabaseError databaseError) {
                                                                //
                                                            }
                                                        }
                                                );

                                    }


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