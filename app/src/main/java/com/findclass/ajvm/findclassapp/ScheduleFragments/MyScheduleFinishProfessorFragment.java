package com.findclass.ajvm.findclassapp.ScheduleFragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.findclass.ajvm.findclassapp.Adapter.MyScheduleProfessorAdapter;
import com.findclass.ajvm.findclassapp.Model.Date_Status;
import com.findclass.ajvm.findclassapp.Model.Date_Time;
import com.findclass.ajvm.findclassapp.Model.Schedule;
import com.findclass.ajvm.findclassapp.Model.ScheduleObject;
import com.findclass.ajvm.findclassapp.Model.Subject;
import com.findclass.ajvm.findclassapp.Model.Time;
import com.findclass.ajvm.findclassapp.Model.User;
import com.findclass.ajvm.findclassapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyScheduleFinishProfessorFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    //Elemenetos do firebase
    private DatabaseReference schedulesRef;
    private DatabaseReference rootRef;
    private FirebaseAuth auth;
    //Elementos gráficos
    private ProgressDialog progress;
    private SwipeRefreshLayout mSwipeToRefresh;
    private RecyclerView recyclerViewMyScheduleList;
    //Elementos auxiliares
    private MyScheduleProfessorAdapter adapter;
    private ArrayList<ScheduleObject> myScheduleObjects = new ArrayList<>();

    public MyScheduleFinishProfessorFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_schedule_professor, container, false);
        //setando atributos
        adapter = new MyScheduleProfessorAdapter(myScheduleObjects);
        //setando atributos do firebase
        auth = FirebaseAuth.getInstance();
        rootRef = FirebaseDatabase.getInstance().getReference();
        schedulesRef = rootRef.child("schedule");
        //Setando atributos gráficos
        recyclerViewMyScheduleList = view.findViewById(R.id.recyclerViewMySchedule);
        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(getActivity());
        recyclerViewMyScheduleList.setLayoutManager(layoutManager1);
        recyclerViewMyScheduleList.setHasFixedSize(true);
        recyclerViewMyScheduleList.setAdapter(adapter);
        mSwipeToRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_container);
        mSwipeToRefresh.setOnRefreshListener(this);

        return view;
    }

    //Método que define as ações que devem ser executadas ao iniciar o Fragment
    @Override
    public void onStart() {
        super.onStart();
        myScheduleObjects.clear();
        retrieveMySchedules();
    }

    //Método que define as ações que devem ser executadas ao abandonar o Fragment
    @Override
    public void onStop() {
        super.onStop();
    }

    //Método para buscar no banco de dados minha lista de aulas finalizadas
    public void retrieveMySchedules(){
        progress = new ProgressDialog(getActivity());
        progress.setMessage("Carregando...");
        progress.show();

        myScheduleObjects.clear();

        final ArrayList<DataSnapshot> myScheduleSnapshots = new ArrayList<>();
        schedulesRef
                .child(auth.getCurrentUser().getUid())
                .addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                                    for (DataSnapshot scheduleSnap: dataSnapshot1.getChildren()){
                                        if (scheduleSnap.child("finish").getValue(Integer.class).equals(1)){
                                            myScheduleSnapshots.add(scheduleSnap);
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

    //Método para buscar no banco de dados os professores das aulas finalizadas
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

    //Método para buscar no banco de dados o aluno das aulas finalizadas
    public void retrieveStudent(final Schedule schedule, final User professor){
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

    //Método para buscar no banco de dados as disciplinas das aulas
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

    //Método para buscar no banco de dados a data da aula
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

    //Método para buscar no banco de dados a data da aula
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

    //Método para buscar no banco de dados a hora da aula
    public void retrieveTime(final Schedule schedule, final User professor, final User student, final Subject subject, DatabaseReference datetimeRef, Date_Time date_time, final Date_Status date){
        datetimeRef
                .child("times")
                .child(date_time.getTime_id())
                .addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Time time = dataSnapshot.getValue(Time.class);

                                ScheduleObject scheduleObject = new ScheduleObject(professor, student, subject, time, date, schedule.getId(),schedule.getCancel());
                                myScheduleObjects.add(scheduleObject);
                                sortMySchedules();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                //
                            }
                        }
                );
    }

    //Método para atualizar a lista
    @Override
    public void onRefresh() {
        retrieveMySchedules();
        mSwipeToRefresh.setRefreshing(false);
    }

    //Método para ordenar a lista por data
    public void sortMySchedules() {
        Collections.sort(myScheduleObjects);
        Collections.reverse(myScheduleObjects);
        ArrayList<ScheduleObject> canceledScheduleObjects = new ArrayList<>();
        ArrayList<ScheduleObject> notCanceledScheduleObjects = new ArrayList<>();
        for (ScheduleObject scheduleObject : myScheduleObjects) {
            if (scheduleObject.getCancel() == 1) {
                canceledScheduleObjects.add(scheduleObject);
            } else {
                notCanceledScheduleObjects.add(scheduleObject);
            }
        }
        myScheduleObjects.clear();
        myScheduleObjects.addAll(notCanceledScheduleObjects);
        myScheduleObjects.addAll(canceledScheduleObjects);
        adapter.notifyDataSetChanged();
    }
}