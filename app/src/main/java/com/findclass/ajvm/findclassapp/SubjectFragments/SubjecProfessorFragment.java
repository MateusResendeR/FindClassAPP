package com.findclass.ajvm.findclassapp.SubjectFragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.findclass.ajvm.findclassapp.Adapter.SubjectProfessorAdapter;
import com.findclass.ajvm.findclassapp.Model.Professor_Subject;
import com.findclass.ajvm.findclassapp.Model.Subject;
import com.findclass.ajvm.findclassapp.Model.Subject_Professor;
import com.findclass.ajvm.findclassapp.Model.User;
import com.findclass.ajvm.findclassapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SubjecProfessorFragment extends Fragment {
    private RecyclerView recyclerView;
    private SubjectProfessorAdapter adapter;
    private ArrayList<Subject_Professor> listProfessor = new ArrayList<>();
    private DatabaseReference professorSubjectRef;
    private DatabaseReference userRef;
    private DatabaseReference subjectRef;
    private ValueEventListener valueEventListenerProfessores;



    public SubjecProfessorFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_subjec_professor, container, false);

        //configuracoes iniciais
        recyclerView = view.findViewById(R.id.recycleViewProfessores);
        professorSubjectRef = FirebaseDatabase.getInstance().getReference().child("professorSubjects");
        userRef = FirebaseDatabase.getInstance().getReference().child("users");
        subjectRef = FirebaseDatabase.getInstance().getReference().child("subjects");

        //configuracao adapter
        adapter = new SubjectProfessorAdapter(listProfessor,getActivity());
        //configuracao recycleview
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        retrieveProfessors();
    }

    @Override
    public void onStop() {
        super.onStop();
        subjectRef.removeEventListener(valueEventListenerProfessores);
        professorSubjectRef.removeEventListener(valueEventListenerProfessores);
        userRef.removeEventListener(valueEventListenerProfessores);
    }

    public void searchProfessor(String text){
        List<Subject_Professor> listProfessorSearch = new ArrayList<>();
        for(Subject_Professor professor: listProfessor){
            String subject = professor.getSubject().getName().toLowerCase();
            subject = subject.replace( 'á' , 'a');
            subject = subject.replace( 'ã' , 'a');
            subject = subject.replace( 'é' , 'e');
            subject = subject.replace( 'ê' , 'e');
            subject = subject.replace( 'ó' , 'o');
            subject = subject.replace( 'õ' , 'o');
            subject = subject.replace( 'ú' , 'u');
            subject = subject.replace( 'í' , 'i');


            String level = professor.getSubject().getLevel().toLowerCase();
            level = level.replace( 'á' , 'a');
            level = level.replace( 'ã' , 'a');
            level = level.replace( 'é' , 'e');
            level = level.replace( 'ê' , 'e');
            level = level.replace( 'ó' , 'o');
            level = level.replace( 'õ' , 'o');
            level = level.replace( 'ú' , 'u');
            level = level.replace( 'í' , 'i');

            if(subject.contains(text)||level.contains(text)){
                listProfessorSearch.add(professor);

            }
        }
        adapter = new SubjectProfessorAdapter(listProfessorSearch,getActivity());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void reloadList(){
        adapter = new SubjectProfessorAdapter(listProfessor,getActivity());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void retrieveProfessors(){
        listProfessor.clear();
        valueEventListenerProfessores = professorSubjectRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(final DataSnapshot dados: dataSnapshot.getChildren()){
                    professorSubjectRef.child(dados.getKey()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(final DataSnapshot dado: dataSnapshot.getChildren()){
                                final Subject_Professor sp = new Subject_Professor();
                                final Professor_Subject ps = dado.getValue(Professor_Subject.class);

                                userRef.addValueEventListener(
                                        new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                for (DataSnapshot d: dataSnapshot.getChildren()){
                                                    User user = d.getValue(User.class);
                                                    if (d.getKey().equals(ps.getProfessorUid())){
                                                        sp.setUser(user);

                                                        Log.e("teste",user.getName());

                                                        subjectRef.addValueEventListener(
                                                                new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                                        for (DataSnapshot d: dataSnapshot.getChildren()){
                                                                            Subject subject = d.getValue(Subject.class);
                                                                            if (d.getKey().equals(ps.getSubjectId())){
                                                                                sp.setSubject(subject);
                                                                                listProfessor.add(sp);



                                                                            }adapter.notifyDataSetChanged();
                                                                        }adapter.notifyDataSetChanged();
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


                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                                //
                                            }
                                        }
                                );
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            //
                        }
                    });



                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //
            }
        });
    }

}
