package me.farhanarnob.keepmytask;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import me.farhanarnob.keepmytask.data.TaskContract.TaskEntry;

/**
 * Created by Farhan Rahman Arnob on ${05-Jul-17}.
 * For coding task of INTELLIJ SYSTEM SOLUTION SDN BHD.
 * <p>
 * Copyright 2017 Farhan Rahman Arnob
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

public class TaskListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int TASK_LOADER = 1;
    private TaskCursorAdapter mTaskCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);

        // Setup FAB to open TaskEditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TaskListActivity.this, TaskEditorActivity.class);
                startActivity(intent);
            }
        });

        // grid view and adding cursor loader
        GridView gridView = (GridView) findViewById(R.id.grid_view_task_list);

        // Add empty View
        View emptyView = findViewById(R.id.empty_view);
        gridView.setEmptyView(emptyView);

        mTaskCursorAdapter = new TaskCursorAdapter(this, null);
        gridView.setAdapter(mTaskCursorAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int positon, long id) {
                Intent editorIntent = new Intent(TaskListActivity.this, TaskEditorActivity.class);
                Uri uriForUpdateOrDelete = ContentUris.withAppendedId(TaskEntry.CONTENT_URI, id);
                editorIntent.setData(uriForUpdateOrDelete);
                startActivity(editorIntent);
            }
        });

        // kick of loader
        getSupportLoaderManager().initLoader(TASK_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String[] projection = {
                TaskEntry._ID,
                TaskEntry.COLUMN_TASK_NAME,
                TaskEntry.COLUMN_TASK_DESCRIPTION,
                TaskEntry.COLUMN_TASK_DATE_CREATED,
                TaskEntry.COLUMN_TASK_DATE_UPDATED
        };
        return new CursorLoader(
                this,
                TaskEntry.CONTENT_URI,
                projection,
                null,
                null,
                null
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_task_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                deleteAllTasks();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteAllTasks() {
        int rowDeleted = getContentResolver().delete(TaskEntry.CONTENT_URI, null, null);
        if (rowDeleted > 0) {
            Toast.makeText(getApplicationContext(), R.string.all_task_deleted,
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), R.string.all_tasks_deletion_failed,
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Toast.makeText(getApplicationContext(), "Provider working", Toast.LENGTH_SHORT).show();
        mTaskCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Toast.makeText(getApplicationContext(), "Provider resetting", Toast.LENGTH_SHORT).show();
        mTaskCursorAdapter.swapCursor(null);
    }
}
