package com.alequinonboard.notes.activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.alequinonboard.notes.BooleanMenuItem;
import com.alequinonboard.notes.R;
import com.alequinonboard.notes.SoftInputVisibilityController;
import com.alequinonboard.notes.database.NotesDatabase;

public class NotesMainActivity extends NoteActivity {

    public static final int UPDATE_REQUEST_CODE = 1;
    public static final int UPDATE_RESULT_CODE = 1;
    public static final String NOTE_ID_EXTRA = "NOTE_ID_EXTRA";

    private SimpleCursorAdapter listAdapter;
    private Cursor listCursor;

    private final BooleanMenuItem filterByFavouritesButton = new BooleanMenuItem();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ListView listView = (ListView) findViewById(R.id.notes_list_main_activity);
        initialiseListCursorAndListAdapter();
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(getListListener());

        this.setUpFilterByFavouritesButton();

        EditText searchBox = (EditText)findViewById(R.id.search_bar_main_activity);
        searchBox.setOnKeyListener(getSearchBarKeyListener());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_notes_main, menu);

        filterByFavouritesButton.setMenuItem(menu.getItem(2));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch(id){

            case R.id.add_icon_main_activity:
                startActivityForResult(new Intent(this, NewNoteActivity.class), UPDATE_REQUEST_CODE);
                break;

            case R.id.search_icon_main_activity:
                this.onPressSearchButton();
                break;

            case R.id.filer_favourites_main_activity:
                this.onPressFilterFavouritesButton();
                break;

            case R.id.action_settings:

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void onPressSearchButton(){
        if(this.isSearchBarVisible()){
            this.hideSearchBar();
            SoftInputVisibilityController.hideAndResetSoftInput(this);
        }else{
            this.showSearchBarAndRequestFocus();
            SoftInputVisibilityController.showSoftInput(this);
        }
        this.updateListViewWithSearchTerm(this.getSearchBarText());
    }

    private void onPressFilterFavouritesButton(){
        filterByFavouritesButton.setState(!filterByFavouritesButton.isStateTrue());
        this.updateListViewWithSearchTerm(this.getSearchBarText());
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(isSearchBarVisible()){
            this.hideSearchBarAndUpdateList();
        }
    }

    private void setUpFilterByFavouritesButton(){

        filterByFavouritesButton.setTitleToUseWhenTrue(getString(R.string.show_all_icon_title));
        filterByFavouritesButton.setTitleToUseWhenFalse(getString(R.string.filter_favourites_icon_title));
    }

    private void initialiseListCursorAndListAdapter(){
        listCursor = getListCursor();
        listAdapter = new SimpleCursorAdapter(
                this, android.R.layout.simple_list_item_1, listCursor, new String[]{NotesDatabase.TITLE},
                new int[]{android.R.id.text1}, SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER
        );
    }

    private View.OnKeyListener getSearchBarKeyListener(){
        return new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                updateListViewWithSearchTerm(getSearchBarText());
                return false;
            }
        };
    }

    private String getSearchBarText(){
        return ((EditText)findViewById(R.id.search_bar_main_activity)).getText().toString();
    }

    private boolean isSearchBarVisible(){
        return (findViewById(R.id.search_bar_layout_main_activity)).getVisibility() == View.VISIBLE;
    }

    private void showSearchBarAndRequestFocus(){
        View searchBar = findViewById(R.id.search_bar_layout_main_activity);
        searchBar.setVisibility(View.VISIBLE);
        searchBar.requestFocus();
    }

    private void hideSearchBar(){
        (findViewById(R.id.search_bar_layout_main_activity)).setVisibility(View.GONE);
    }

    private void hideSearchBarAndUpdateList(){
        this.hideSearchBar();
        this.updateListView();
    }

    private void updateListView(){
        this.updateListViewWithSearchTerm(null);
    }

    private void updateListViewWithSearchTerm(String searchTerm){
        new UpdateListFromThread().execute(searchTerm);
    }

    private class UpdateListFromThread extends AsyncTask<String, Void, Void>{

        private Cursor threadCursor;

        @Override
        protected Void doInBackground(String...searchTerm) {
            threadCursor = getListCursorWithSearchTerm(searchTerm[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            listCursor.close();
            listCursor = threadCursor;
            listAdapter.swapCursor(listCursor);
        }
    }

    private Cursor getListCursor(){
        return this.getListCursorWithSearchTerm(null);
    }

    private Cursor getListCursorWithSearchTerm(String searchTerm){
        Cursor cursor;
        if(!filterByFavouritesButton.isStateTrue()){
            cursor = database.getNotesTableQueryByTitle(searchTerm);
        }else{
            cursor = database.getFavouriteNotesTableQueryByTitle(searchTerm);
        }

        return cursor;
    }

    private AdapterView.OnItemClickListener getListListener(){
        final Context CURRENT_CONTEXT = this;
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent startNoteViewer = new Intent(CURRENT_CONTEXT, NoteViewerActivity.class);

                listCursor.moveToPosition(position);
                int idColumnIndex = listCursor.getColumnIndex(NotesDatabase.ID);
                startNoteViewer.putExtra(NOTE_ID_EXTRA, listCursor.getInt(idColumnIndex));

                startActivityForResult(startNoteViewer, UPDATE_REQUEST_CODE);
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == UPDATE_REQUEST_CODE && resultCode == UPDATE_RESULT_CODE){
            updateListView();
        }

    }

}
