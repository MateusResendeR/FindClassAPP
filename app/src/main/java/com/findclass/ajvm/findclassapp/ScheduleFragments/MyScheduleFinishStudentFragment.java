package com.findclass.ajvm.findclassapp.ScheduleFragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyScheduleFinishStudentFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    //Elemenetos do firebase
    private DatabaseReference schedulesRef;
    private DatabaseReference rootRef;
    private FirebaseAuth auth;
    //Elementos gráficos
    private RecyclerView recyclerViewMyScheduleList;
    private SwipeRefreshLayout mSwipeToRefresh;
    private ProgressDialog progress;
    //Elementos auxiliares
    private MyScheduleStudentAdapter adapter;
    private ArrayList<ScheduleObject> myScheduleObjects = new ArrayList<>();
    private ArrayList<Schedule> mySchedules = new ArrayList<>();

    public MyScheduleFinishStudentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_schedule_student, container, false);
        //setando atributos
        adapter = new MyScheduleStudentAdapter(myScheduleObjects, mySchedules);
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
        //Adição do evento de clique aos itens da lista
        recyclerViewMyScheduleList.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getActivity(),
                        recyclerViewMyScheduleList,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            //Definição da ação do clique.
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

    //Método que define as ações que devem ser executadas ao iniciar o Fragment
    @Override
    public void onStart() {
        super.onStart();
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

    //Método para recarregar lista completa de aulas marcadas
    public void reloadList() {
        adapter = new MyScheduleStudentAdapter(myScheduleObjects,mySchedules);
        recyclerViewMyScheduleList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    //Método de buscar aula por: professor, disciplina ou nível
    public void searchSchedule(String text) {
        List<ScheduleObject> listScheduleSearch = new ArrayList<>();
        List<Schedule> listRealScheduleSearch = new ArrayList<>();
        for (ScheduleObject scheduleObject : myScheduleObjects) {
            String subject = treatText(scheduleObject.getSubject().getName());
            String professor = treatText(scheduleObject.getProfessor().getName());
            String level = treatText(scheduleObject.getSubject().getLevel());
            if (subject.contains(text)||professor.contains(text)||level.contains(text)) {
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

    //Método para tratar strings acentuadas e com letras maiúsculas
    public static String treatText(String text){
        return Normalizer.normalize(text, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "").toLowerCase();
    }

    //Método para atualizar a lista
    @Override
    public void onRefresh() {
        retrieveMySchedules();
        mSwipeToRefresh.setRefreshing(false);
    }
}