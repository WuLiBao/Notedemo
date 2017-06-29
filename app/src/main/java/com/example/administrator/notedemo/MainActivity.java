package com.example.administrator.notedemo;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

public class MainActivity extends ListActivity {

    private static final int INSERT_ID = Menu.FIRST;

    private static final int DELETE_ID = Menu.FIRST + 1;

    private static final int ACTIVITY_CREATE = 0;

    private static final int ACTIVITY_EDIT = 1;

    private DiaryDbAdapter diaryDb;

    private Cursor cursor;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        diaryDb = new DiaryDbAdapter(this);
        diaryDb.open();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void showListView() {
        cursor = diaryDb.getAllNotes();

        String[] from = new String[]{DiaryDbAdapter.KEY_TITLE, DiaryDbAdapter.KEY_BODY};
        int[] to = new int[]{R.id.text1, R.id.created};

        ListAdapter cursorAdapter = new SimpleAdapter(this, R.layout.diary_row, cursor, from, to);

        setListAdapter(cursorAdapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        // TODO Auto-generated method stub
        super.onListItemClick(l, v, position, id);
        Cursor c = cursor;
        c.move(position);
        Intent intent = new Intent(this, ActivityDiary.class);
        intent.putExtra(DiaryDbAdapter.KEY_ROWID, id);
        intent.putExtra(DiaryDbAdapter.KEY_TITLE, c.getString(c
                .getColumnIndexOrThrow(DiaryDbAdapter.KEY_TITLE)));
        intent.putExtra(DiaryDbAdapter.KEY_BODY, c.getString(c
                .getColumnIndexOrThrow(DiaryDbAdapter.KEY_BODY)));
        startActivityForResult(intent, ACTIVITY_EDIT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        showListView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        super.onCreateOptionsMenu(menu);
        menu.add(0, INSERT_ID, 0, R.string.menu_insert);
        menu.add(0, DELETE_ID, 0, R.string.menu_delete);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            case INSERT_ID:
                Log.i("INSERT:", String.valueOf(INSERT_ID));
                createDiary();
                return true;
            case DELETE_ID:
                Log.i("DELETE_ID:", String.valueOf(getListView().getSelectedItemId()));
                diaryDb.deleteDiary(getListView().getSelectedItemId());
                showListView();
                return true;
        }
        return super.onMenuItemSelected(featureId, item);
    }

    private void createDiary() {

        Intent intent = new Intent();
        intent.setClass(this, ActivityDiary.class);
        startActivityForResult(intent, ACTIVITY_CREATE);
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
