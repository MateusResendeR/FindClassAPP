package com.findclass.ajvm.findclassapp.SubjectFragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    public MySubjectListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_subject_list, container, false);

        //recuperar elemento visual onde o adapter trabalhará;
        recyclerViewMySubjectsList = view.findViewById(R.id.mySubjectsRecyclerView);

        //configurar o adapter junto ao recyclerview;
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
        retrieveMySubjects();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    public void retrieveMySubjects(){
        //limpar o arraylist para evitar duplicação no adapter
        mySubjectsList.clear();

        //setar referência para o banco de dados
        DatabaseReference professorSubjectsRef = rootRef.child("professorSubjects");

        //fazer a consulta pra povoar nosso arraylist do adapter;
        professorSubjectsRef
                .child(auth.getCurrentUser().getUid())
                .addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                final ArrayList<String> subjectsId = new ArrayList<>();

                                //recuperar todas os ids das disciplinas  que esse professor está disposto a lecionar;
                                for (DataSnapshot d: dataSnapshot.getChildren()){
                                    subjectsId.add(d.getKey());
                                }

                                retrieveMySubjectsData(subjectsId);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                //Erro;
                            }
                        }
                );
    }

    public void retrieveMySubjectsData(final ArrayList<String> subjectsId){
        //setar referência para as disciplinas no bd;
        subjectsRef = rootRef.child("subjects");

        //recuperar os dados das disciplinas que ele leciona;
        for (final String id: subjectsId) {
            subjectsRef
                    .addListenerForSingleValueEvent(
                            new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    //pegar o objetivo;
                                    Subject thisSubject = dataSnapshot.child(id).getValue(Subject.class);

                                    //adicionar o objeto ao arraylist;
                                    mySubjectsList.add(thisSubject);

                                    //notifico o adapter se necessário;
                                    adapter.notifyDataSetChanged();
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    //Erro;
                                }
                            }
                    );
        }
    }

}
