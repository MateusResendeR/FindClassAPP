package com.findclass.ajvm.findclassapp.ScheduleFragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.findclass.ajvm.findclassapp.Adapter.MyScheduleProfessorAdapter;
import com.findclass.ajvm.findclassapp.Adapter.MyScheduleStudentAdapter;
import com.findclass.ajvm.findclassapp.Helper.RecyclerItemClickListener;
import com.findclass.ajvm.findclassapp.Model.Date_Status;
import com.findclass.ajvm.findclassapp.Model.Date_Time;
import com.findclass.ajvm.findclassapp.Model.Schedule;
import com.findclass.ajvm.findclassapp.Model.ScheduleObject;
import com.findclass.ajvm.findclassapp.Model.Subject;
import com.findclass.ajvm.findclassapp.Model.Time;
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
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyScheduleFinishStudentFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView recyclerViewMyScheduleList;
    private MyScheduleStudentAdapter adapter;
    private DatabaseReference schedulesRef;
    private DatabaseReference rootRef;
    private FirebaseAuth auth;
    private ArrayList<ScheduleObject> myScheduleObjects = new ArrayList<>();
    private ArrayList<Schedule> mySchedules = new ArrayList<>();
    private ValueEventListener valueEventListener;
    private ProgressDialog progress;
    private SwipeRefreshLayout mSwipeToRefresh;


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

        adapter = new MyScheduleStudentAdapter(myScheduleObjects, mySchedules);

        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(getActivity());
        recyclerViewMyScheduleList.setLayoutManager(layoutManager1);
        recyclerViewMyScheduleList.setHasFixedSize(true);
        recyclerViewMyScheduleList.setAdapter(adapter);

        mSwipeToRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_container);
        mSwipeToRefresh.setOnRefreshListener(this);
        //clique
        recyclerViewMyScheduleList.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getActivity(),
                        recyclerViewMyScheduleList,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                if (myScheduleObjects.get(position).getRating().equals("0")) {
                                    Intent intent = new Intent(getContext(), RatingProfessorActivity.class);
                                    intent.putExtra("user", myScheduleObjects.get(position).getProfessor());
                                    intent.putExtra("subject", myScheduleObjects.get(position).getSubject());
                                    intent.putExtra("schedule", myScheduleObjects.get(position));
                                    intent.putExtra("schedule1", mySchedules.get(position));
                                    startActivity(intent);
                                } else {
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
        retrieveMySchedules();
    }

    @Override
    public void onStop() {
        super.onStop();
    }


    public void retrieveMySchedules(){
        progress = new ProgressDialog(getActivity());
        progress.setMessage("Carregando...");
        progress.show();

        myScheduleObjects.clear();

        final ArrayList<DataSnapshot> myScheduleSnapshots = new ArrayList<>();

        schedulesRef.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                            for (DataSnapshot dataSnapshot2: dataSnapshot1.getChildren()){
                                if(dataSnapshot2.getKey().equals(auth.getCurrentUser().getUid())){
                                    for (DataSnapshot scheduleSnap: dataSnapshot2.getChildren()){
                                        if (scheduleSnap.child("finish").getValue(Integer.class).equals(1)) {
                                            myScheduleSnapshots.add(scheduleSnap);
                                            mySchedules.add(scheduleSnap.getValue(Schedule.class));
                                            adapter.notifyDataSetChanged();
                                        }
                                    }
                                }
                            }
                        }

                        for(DataSnapshot scheduleSnap: myScheduleSnapshots){
                            Schedule schedule = scheduleSnap.getValue(Schedule.class);
                            retrieveProfessor(schedule);
                        }

                        progress.dismiss();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //
                    }
                }
        );
    }

    public void retrieveProfessor(final Schedule schedule){
        DatabaseReference usersRef = rootRef.child("users");
        usersRef
                .child(schedule.getProfessor_id())
                .addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                User professor = dataSnapshot.getValue(User.class);
                                retrieveStudent(schedule,professor);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                //
                            }
                        }
                );
    }

    public void retrieveStudent(final Schedule schedule, final User professor){
        Log.e("DEBUG","Here!");
        DatabaseReference usersRef = rootRef.child("users");
        usersRef
                .child(schedule.getStudent_id())
                .addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                User student = dataSnapshot.getValue(User.class);
                                retrieveSubject(schedule,professor,student);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                //
                            }
                        }
                );
    }

    public void retrieveSubject(final Schedule schedule, final User professor, final User student){
        DatabaseReference subjectsRef = rootRef.child("subjects");
        subjectsRef
                .child(schedule.getSubject_id())
                .addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Subject subject = dataSnapshot.getValue(Subject.class);
                                retrieveDatetime(schedule,professor,student,subject);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                //
                            }
                        }
                );
    }

    public void retrieveDatetime(final Schedule schedule, final User professor, final User student, final Subject subject){
        final DatabaseReference datetimeRef = rootRef.child("availability");
        final DatabaseReference thisDatetimeRef = datetimeRef.child(schedule.getProfessor_id());
        thisDatetimeRef
                .child("dateTimes")
                .child(schedule.getDatetime_id())
                .addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Date_Time date_time = dataSnapshot.getValue(Date_Time.class);
                                retrieveDate(schedule,professor,student,subject,thisDatetimeRef,date_time);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                //
                            }
                        }
                );
    }

    public void retrieveDate(final Schedule schedule, final User professor, final User student, final Subject subject, final DatabaseReference datetimeRef, final Date_Time date_time){
        datetimeRef
                .child("dates")
                .child(date_time.getDate_id())
                .addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Date_Status date = dataSnapshot.getValue(Date_Status.class);
                                retrieveTime(schedule,professor,student,subject,datetimeRef,date_time,date);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                //
                            }
                        }
                );

    }

    public void retrieveTime(final Schedule schedule, final User professor, final User student, final Subject subject, DatabaseReference datetimeRef, Date_Time date_time, final Date_Status date){
        datetimeRef
                .child("times")
                .child(date_time.getTime_id())
                .addValueEventListener(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Time time = dataSnapshot.getValue(Time.class);

                                ScheduleObject scheduleObject = new ScheduleObject(schedule.getRating(), schedule.getId(),professor, student, subject, time, date);
                                myScheduleObjects.add(scheduleObject);
                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                //
                            }
                        }
                );
    }
    public void reloadList() {
        adapter = new MyScheduleStudentAdapter(myScheduleObjects,mySchedules);
        recyclerViewMyScheduleList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void searchSchedule(String text) {
        List<ScheduleObject> listScheduleSearch = new ArrayList<>();
        List<Schedule> listRealScheduleSearch = new ArrayList<>();
        for (ScheduleObject scheduleObject : myScheduleObjects) {
            String subject = scheduleObject.getSubject().getName().toLowerCase();
            subject = subject.replace('á', 'a');
            subject = subject.replace('ã', 'a');
            subject = subject.replace('é', 'e');
            subject = subject.replace('ê', 'e');
            subject = subject.replace('ó', 'o');
            subject = subject.replace('õ', 'o');
            subject = subject.replace('ú', 'u');
            subject = subject.replace('í', 'i');

            if (subject.contains(text)) {
                listScheduleSearch.add(scheduleObject);

            }
        }
        for (ScheduleObject scheduleObject : listScheduleSearch){
            String subject_id = scheduleObject.getId();
            for (Schedule schedule : mySchedules) {
                if (subject_id.equals(schedule.getId())) {
                    listRealScheduleSearch.add(schedule);
                    break;
                }
            }
        }
        adapter = new MyScheduleStudentAdapter(listScheduleSearch, listRealScheduleSearch);
        recyclerViewMyScheduleList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {
        retrieveMySchedules();
        mSwipeToRefresh.setRefreshing(false);
    }
}