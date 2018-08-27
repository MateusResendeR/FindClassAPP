package com.findclass.ajvm.findclassapp.menuActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.findclass.ajvm.findclassapp.AccountActivities.SignInActivity;
import com.findclass.ajvm.findclassapp.Adapter.SubjectProfessorAdapter;
import com.findclass.ajvm.findclassapp.Helper.RecyclerItemClickListener;
import com.findclass.ajvm.findclassapp.Model.Professor_Subject;
import com.findclass.ajvm.findclassapp.Model.Subject;
import com.findclass.ajvm.findclassapp.Model.Subject_Professor;
import com.findclass.ajvm.findclassapp.Model.User;
import com.findclass.ajvm.findclassapp.R;
import com.findclass.ajvm.findclassapp.aboutActivities.StudentAboutActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SubjectCategoryLevelActivity extends AppCompatActivity {
    //Elemnetos do firebase
    private DatabaseReference professorSubjectRef;
    private DatabaseReference userRef;
    private DatabaseReference subjectRef;
    //Elementos gráficos
    private MaterialSearchView searchView;
    private RecyclerView recyclerViewMedio;
    //Elementos auxiliares
    private String nameSubject;
    private String nameLevel;
    private SubjectProfessorAdapter adapter;
    private ArrayList<Subject_Professor> listProfessors = new ArrayList<>();
    private ValueEventListener valueEventListenerProfessores;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_category_level);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //Recuperando dados da Activity passada
        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            nameSubject = (String) bundle.getSerializable("subject");
            nameLevel = (String)bundle.getSerializable("level");
        }
        //setando atributos
        adapter = new SubjectProfessorAdapter(listProfessors, this);
        //setando atributos do firebase
        professorSubjectRef = FirebaseDatabase.getInstance().getReference().child("professorSubjects");
        userRef = FirebaseDatabase.getInstance().getReference().child("users");
        subjectRef = FirebaseDatabase.getInstance().getReference().child("subjects");
        //Setando atributos gráficos
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewMedio = findViewById(R.id.recycleViewListaMedio);
        recyclerViewMedio.setLayoutManager(layoutManager);
        recyclerViewMedio.setHasFixedSize(true);
        recyclerViewMedio.setAdapter(adapter);
        searchView = findViewById(R.id.search_viewProfessor);
        //Criação da barra de pesquisa
        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                //
            }

            //Voltar a lista com todos os professores quando fechar a busca
            @Override
            public void onSearchViewClosed() {
                reloadList();
            }
        });

        //Metodos para busca
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            //Realiza a busca quando o botão de buscar é pressionado.
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query != null && !query.isEmpty()) {
                    searchProfessor(treatText(query));
                }
                return true;
            }

            //Realiza buscas enquanto o texto é digitado
            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText != null && !newText.isEmpty()) {
                    searchProfessor(treatText(newText));
                }

                return true;
            }
        });


        //Adição do evento de clique aos itens da lista
        recyclerViewMedio.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        this,
                        recyclerViewMedio,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            //Definição da ação do clique.
                            @Override
                            public void onItemClick(View view, int position) {
                                Intent intent = new Intent(SubjectCategoryLevelActivity.this,AvailabilityListAlunoActivity.class);
                                Subject_Professor thisSubjectProfessor = listProfessors.get(position);
                                intent.putExtra("professor_uid",thisSubjectProfessor.getProfessorSubject().getProfessorUid());
                                intent.putExtra("subject_id",thisSubjectProfessor.getSubject().getId());
                                startActivity(intent);
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

    }

    //Método que define a ação do botão voltar do hardware
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getBaseContext(), MenuAlunoActivity.class);
        startActivity(intent);
        finish();
    }

    //Método que define as ações que devem ser executadas ao iniciar a Activity
    @Override
    protected void onStart() {
        super.onStart();
        retrieveProfessors();
    }

    //Método que define as ações que devem ser executadas ao abandonar a Activity
    @Override
    protected void onStop() {
        super.onStop();
        subjectRef.removeEventListener(valueEventListenerProfessores);
        professorSubjectRef.removeEventListener(valueEventListenerProfessores);
        userRef.removeEventListener(valueEventListenerProfessores);
    }

    //Método para aplicação do menu (searchView)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_aluno, menu);
        MenuItem item = menu.findItem(R.id.menuPesquisa);
        searchView.setMenuItem(item);
        return true;
    }

    //Método que seleciona qual opção do menu foi escolhida
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logoutAluno) {
            logout(this.findViewById(R.id.toolbar));
        }if(id == R.id.action_aboutStudent){
            startActivity(new Intent(this, StudentAboutActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    //Método para deslogar do aplicativo
    public void logout(View view){
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(this, "Você foi deslogado!", Toast.LENGTH_LONG).show();
        startActivity(new Intent(SubjectCategoryLevelActivity.this,SignInActivity.class));
        finish();
    }

    //Método para tratar strings acentuadas e com letras maiúsculas
    public static String treatText(String text){
        return Normalizer.normalize(text, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "").toLowerCase();
    }

    //Método de buscar professor por nome
    public void searchProfessor(String text) {
        List<Subject_Professor> listProfessorSearch = new ArrayList<>();
        for (Subject_Professor professor : listProfessors) {
            String prof = professor.getUser().getName().toLowerCase();
            prof = treatText(prof);
            if (prof.contains(text)) {
                listProfessorSearch.add(professor);
            }
        }
        adapter = new SubjectProfessorAdapter(listProfessorSearch, this);
        recyclerViewMedio.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    //Método para recarregar lista completa de professores
    public void reloadList() {
        adapter = new SubjectProfessorAdapter(listProfessors, this);
        recyclerViewMedio.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    //Método para buscar no banco de dados lista de professores e suas disciplinas
    public void retrieveProfessors (){
        listProfessors.clear();
        valueEventListenerProfessores = professorSubjectRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (final DataSnapshot dados : dataSnapshot.getChildren()) {
                    professorSubjectRef.child(dados.getKey()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (final DataSnapshot dado : dataSnapshot.getChildren()) {
                                Subject_Professor subject_professor = new Subject_Professor();
                                Professor_Subject ps = dado.getValue(Professor_Subject.class);
                                subject_professor.setProfessorSubject(ps);
                                retriveUser(ps,subject_professor);
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
    //Método para buscar no banco de dados os usuarios
    public void retriveUser(final Professor_Subject ps,final Subject_Professor subject_professor){
        userRef.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot d : dataSnapshot.getChildren()) {
                            User user = d.getValue(User.class);
                            if (d.getKey().equals(ps.getProfessorUid())) {
                                subject_professor.setUser(user);
                                retriveSubject(ps, subject_professor);
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

    //Método para buscar no banco de dados as subjects dos usuarios
    public void retriveSubject(final Professor_Subject ps, final Subject_Professor subject_professor){
        subjectRef.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot d : dataSnapshot.getChildren()) {
                            Subject subject = d.getValue(Subject.class);
                            if (d.getKey().equals(ps.getSubjectId()) && subject.getLevel().equals(nameLevel)&& subject.getName().equals(nameSubject)) {
                                subject_professor.setSubject(subject);
                                listProfessors.add(subject_professor);
                                Collections.sort(listProfessors);
                            }
                            adapter.notifyDataSetChanged();
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

