package com.findclass.ajvm.findclassapp.SubjectFragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.findclass.ajvm.findclassapp.Adapter.AddSubjectAdapter;
import com.findclass.ajvm.findclassapp.Helper.RecyclerItemClickListener;
import com.findclass.ajvm.findclassapp.Model.Professor_Subject;
import com.findclass.ajvm.findclassapp.Model.Subject;
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
public class AddSubjectListFragment extends Fragment {
    private RecyclerView recyclerViewAddSubjecs;
    private AddSubjectAdapter adapter;
    private ArrayList<Subject> subjectsToAdd = new ArrayList<>();
    private DatabaseReference rootRef;
    private DatabaseReference subjectsRef;
    private DatabaseReference professorSubjectsRef;
    private ValueEventListener valueEventListener;
    private FirebaseAuth auth;

    public AddSubjectListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_subject_list, container, false);

        //setar variáveis do firebase;
        auth = FirebaseAuth.getInstance();
        rootRef = FirebaseDatabase.getInstance().getReference();

        //setar o elemento visual da interface;
        recyclerViewAddSubjecs = view.findViewById(R.id.addSubjectsRecyclerView);

        //configurar o adapter
        adapter = new AddSubjectAdapter(subjectsToAdd,getActivity());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewAddSubjecs.setLayoutManager(layoutManager);
        recyclerViewAddSubjecs.setHasFixedSize(true);
        recyclerViewAddSubjecs.setAdapter(adapter);

        //configurar o touch para cada uma das disciplinas;
        recyclerViewAddSubjecs
                .addOnItemTouchListener(
                        new RecyclerItemClickListener(
                                getActivity(),
                                recyclerViewAddSubjecs,
                                new RecyclerItemClickListener.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(View view, int position) {
                                        Subject subject = subjectsToAdd.get(position);
                                        addSubjectToMe(subjectsToAdd.get(position),auth.getCurrentUser().getUid());
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

    //função que faz a adição das disciplinas ao currículo do professor;
    private void addSubjectToMe(Subject subject, String uid) {
        professorSubjectsRef
                .child(uid)
                .child(subject.getId())
                .setValue(
                        new Professor_Subject(uid,subject.getId())
                );

        getActivity().finish();
    }

    @Override
    public void onStart() {
        super.onStart();
        retrieveSubjectsToAdd();
    }

    @Override
    public void onPause() {
        super.onPause();
        subjectsRef.removeEventListener(valueEventListener);
    }

    public void retrieveSubjectsToAdd(){
        //resetar o arraylist que o adapter gerencia;
        subjectsToAdd.clear();

        //setar a referência no banco de dados;
        professorSubjectsRef = rootRef.child("professorSubjects");

        //executar a consulta;
        valueEventListener = professorSubjectsRef
                .child(auth.getCurrentUser().getUid())
                .addValueEventListener(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                final ArrayList<String> subjectsId = new ArrayList<>();

                                //pegar todos as disciplinas que esse professor já leciona;
                                for (DataSnapshot d: dataSnapshot.getChildren()){
                                    subjectsId.add(d.getKey());
                                }

                                //recuperar todas as subjects que o professor não leciona pra possibilitar inserção.
                                subjectsRef = rootRef.child("subjects");
                                valueEventListener = subjectsRef.orderByChild("name").addValueEventListener(
                                        new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                for (DataSnapshot subject: dataSnapshot.getChildren()){
                                                    if (!subjectsId.contains(subject.getKey())){

                                                        //adicionar as disciplinas para o adapter tratar;
                                                        Subject thisSubject = subject.getValue(Subject.class);
                                                        subjectsToAdd.add(thisSubject);
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

    }

}
