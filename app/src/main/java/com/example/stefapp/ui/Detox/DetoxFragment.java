package com.example.stefapp.ui.Detox;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jean.jcplayer.model.JcAudio;
import com.example.jean.jcplayer.view.JcPlayerView;
import com.example.stefapp.R;
import com.example.stefapp.addAudio.AudioProperties;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class DetoxFragment extends Fragment {
RecyclerView recyclerView;
Boolean checkin = false;
List<GetAudio> audioList;
DetoxAdapter adapter;
JcPlayerView jcPlayerView;
ArrayList<JcAudio> jcAudios = new ArrayList<>();
    ArrayList<String> arrayListSongsName = new ArrayList<>();
    ArrayList<String> arrayListSongsUrl = new ArrayList<>();


private int currentIndex;
    public DetoxFragment() {
        super(R.layout.fragment_meditation);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_detox, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewDetox);
        jcPlayerView = view.findViewById(R.id.jcplayer);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        recyclerView.setHasFixedSize(true);
        audioList = new ArrayList<>();
        recyclerView.setAdapter(adapter);
        adapter = new DetoxAdapter(getContext(),audioList, new DetoxAdapter.RecyclerItemClickListener() {
            @Override
            public void onClickListener(GetAudio audio, int position) {
                jcPlayerView.playAudio(jcAudios.get(position));
                jcPlayerView.setVisibility(View.VISIBLE);
                jcPlayerView.createNotification();
            }
        });

        CollectionReference dbRef = db.collection("audio").document("Психологический детокс").collection("Lo - бесплатный");
        dbRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        AudioProperties songObj = document.toObject(AudioProperties.class);
                        arrayListSongsName.add(songObj.getName());
                        arrayListSongsUrl.add(songObj.getAudioUrl());
                        jcAudios.add(JcAudio.createFromURL(songObj.getName(), songObj.getAudioUrl()));
                    }
                   jcPlayerView.initPlaylist(jcAudios, null);
                } else {
                    Toast.makeText(getActivity(), "Ошибка загрузки данных", Toast.LENGTH_SHORT).show();
                }
            }
        });







        return view;
    }





}