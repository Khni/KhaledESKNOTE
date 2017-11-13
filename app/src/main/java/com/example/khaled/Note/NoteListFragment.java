package com.example.khaled.Note;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.khaled.Note.activities.ViewPagerActivity;
import com.example.khaled.Note.adapters.NoteMListAdapter;
import com.example.khaled.Note.interfaces.InterfaceOnLongClick;
import com.example.khaled.Note.interfaces.InterfacePopupMenuMainRecycler;
import com.example.khaled.Note.models.Note;
import com.example.khaled.Note.models.NoteLab;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class NoteListFragment extends Fragment implements InterfaceOnLongClick,InterfacePopupMenuMainRecycler, SearchView.OnQueryTextListener, NavigationView.OnNavigationItemSelectedListener {
    public static final String TAG = "TAG";
    NoteMListAdapter mAdapter;
    Toolbar mToolbar;
    public String Folder ="MainList";
    FloatingActionButton mFAB;
    private DrawerLayout mDrawerLayout;
    //private ActionBarDrawerToggle mActionBarDrawerToggle;
    static boolean isSelected = false;
    List<Note> notes;
    ArrayList<Note> SelectedItems = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
      //  mAdapter = new NoteMListAdapter();

       // mAdapter.setOnlongClick(this);

    }

    RecyclerView mRecyclerView;
    public NoteListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);



mRecyclerView =(RecyclerView)view.findViewById(R.id.mRecyclerviewID);
        mToolbar=(Toolbar)view.findViewById(R.id.ToolbarrecyclerviewID);

        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        mDrawerLayout=(DrawerLayout)view.findViewById(R.id.DrawerLayoutMainID);

        ActionBarDrawerToggle mActionBarDrawerToggle = new ActionBarDrawerToggle(getActivity(), mDrawerLayout,mToolbar,R.string.Enter_Content ,R.string.date_picker_title);

        if (((AppCompatActivity)getActivity()).getSupportActionBar()!=null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);


            ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        }
        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);
        mActionBarDrawerToggle.syncState();

        NavigationView navigationView =(NavigationView)view.findViewById(R.id.NavigationMainID);
        navigationView.setNavigationItemSelectedListener(this);



        TextView textViewToolbar =(TextView)view.findViewById(R.id.TextviewToolbarRecyclerviewID);



        mFAB =(FloatingActionButton)view.findViewById(R.id.FABmainID);
        mFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewCrime();
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        RecyclerUpdate();

        if (!isSelected){
            textViewToolbar.setVisibility(View.GONE);

        }



        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        /*getActivity().finish();
        startActivity(getActivity().getIntent());
         */

        RecyclerUpdate();
    }

    private void RecyclerUpdate(){
        NoteLab noteLab = NoteLab.get(getActivity());
         notes = noteLab.getCrimes();
        if (mAdapter == null) {
            mAdapter = new NoteMListAdapter(notes,this, this,getActivity());
            mRecyclerView.setAdapter(mAdapter);
        }else {
            mAdapter.setCrimes(notes);
            mAdapter.notifyDataSetChanged();
        } //the condation added in order to work with onResume to notify only




    }
    public void onBackPressed(){

    }

    @Override
    public void onLongClickInterface(View view, int position) {
        Note note = new Note();
        String Folder_Name = NoteLab.get(getActivity()).getCrime(note.getId()).getFolder();

        Toast.makeText(getActivity(), Folder_Name, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onClickPopUpMenuMainRecycler(MenuItem item, Context context, int Position) {
        Note note = notes.get(Position);
        int id = item.getItemId();
        if (id == R.id.deletemenudotsmainID){
            NoteLab.get(getActivity()).deleteNote(note);
            RecyclerUpdate();
           /* mAdapter = new NoteMListAdapter(notes,this, this,getActivity());
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.setCrimes(notes);
            mAdapter.notifyDataSetChanged();*/
            Toast.makeText(getActivity(), R.string.Deleted , Toast.LENGTH_SHORT).show();
            Log.d(TAG,"menu interface done!!!............................"+ note.getId().toString()+ Position);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        /*
         ArrayList<StoryModelM> newList = new ArrayList<>();
        for (StoryModelM storyModel : mModel) {
            String name = storyModel.getTitleModel();
            if (name.contains(newText))
                newList.add(storyModel);
        }
        adapter.setFilter(newList);

        return true;

         */

        ArrayList<Note>newList = new ArrayList<>();
        for (Note note : notes){
            String name =" ";
            String content =" ";
            if (note.getTitle()!=null) {
                 name = note.getTitle().toLowerCase();
            }

            if (note.getContent()!=null) {
                 content = note.getContent().toLowerCase();
            }
            if (name.contains(newText)|| content.contains(newText)){
                newList.add(note);
            }
            mAdapter.setFilter(newList);
        }
        return false;
    }


    //***********************************************************************

    //************************************************************************





    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu_list, menu);
        MenuItem menuItem = menu.findItem(R.id.search_main_listID);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(this);

        super.onCreateOptionsMenu(menu, inflater);
    }
    public void AddNewCrime(){
        Note note = new Note();
        NoteLab.get(getActivity()).addCrime(note);
        Intent intent = ViewPagerActivity.newIntent(getActivity(), note.getId(), Folder);
        startActivity(intent);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.menu_item_new_crime:
               AddNewCrime();






                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onNavigationItemSelected( MenuItem item) {

        int id = item.getItemId();

        if (id== R.id.GalleryMainMAINID){

            Folder= "birthday";
            Toast.makeText(getActivity(), "donnnnnneeee!!!", Toast.LENGTH_SHORT).show();

        }
        mDrawerLayout.closeDrawer(GravityCompat.START);


        return true;
    }
}
