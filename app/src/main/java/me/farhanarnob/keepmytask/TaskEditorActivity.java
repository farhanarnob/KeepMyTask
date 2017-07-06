package me.farhanarnob.keepmytask;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
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

public class TaskEditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int EXISTING_PET_LOADER = 1;
    private static final int BACK_PRESSED_BUTTON = 2;
    private static final int HOME_BUTTON = 3;
    Uri mCurrentTaskUri;
    boolean mTaskHasChanged = true;
    /**
     * EditText field to enter the Task's name
     */
    private EditText mNameEditText;
    /**
     * EditText field to enter the Task's breed
     */
    private EditText mDescriptionEditText;
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mTaskHasChanged = true;
            return false;
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor_task);
        mCurrentTaskUri = getIntent().getData();

        if (mCurrentTaskUri == null) {
            setTitle(R.string.add_new_task);

            // Enable hiding delete menu button to prevent delete option in insert pet mode
            // next we have to override OnPrepareOptionMenu to hide delete menu option
            invalidateOptionsMenu();
        } else {
            getLoaderManager().initLoader(EXISTING_PET_LOADER, null, this);
            setTitle(R.string.view_and_edit_task);
        }


        // Find all relevant views that we will need to read user input from
        mNameEditText = (EditText) findViewById(R.id.edit_task_name);
        mDescriptionEditText = (EditText) findViewById(R.id.edit_description);


        mNameEditText.setOnTouchListener(mTouchListener);
        mDescriptionEditText.setOnTouchListener(mTouchListener);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_task_editor, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        if (mCurrentTaskUri == null) {
            MenuItem deleteMenu = menu.findItem(R.id.action_delete);
            deleteMenu.setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                saveTask();
                finish();
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                showDeleteConfirmationDialog();
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // Navigate back to parent activity (CatalogActivity)
                if (!mTaskHasChanged) {
                    NavUtils.navigateUpFromSameTask(this);
                    return true;
                } else {
                    showUnsavedChangeDialog(HOME_BUTTON);
                    return true;
                }


        }
        return super.onOptionsItemSelected(item);
    }

    private void saveTask() {
        String nameString = mNameEditText.getText().toString().trim();
        String descriptionString = mDescriptionEditText.getText().toString();
        int unixTime = (int) (System.currentTimeMillis());
        if (TextUtils.isEmpty(nameString)) {
            return;
        }

        // Creating contentValues which will be used for database data insert
        ContentValues values = new ContentValues();
        values.put(TaskEntry.COLUMN_TASK_NAME, nameString);
        values.put(TaskEntry.COLUMN_TASK_DESCRIPTION, descriptionString);

        if (mCurrentTaskUri == null) {
            values.put(TaskEntry.COLUMN_TASK_DATE_CREATED, unixTime);
            Uri newUri = getContentResolver().insert(TaskEntry.CONTENT_URI, values);
            if (newUri == null) {
                Toast.makeText(getApplicationContext(), getString(R.string.editor_insert_task_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), getString(R.string.editor_insert_task_successful),
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            values.put(TaskEntry.COLUMN_TASK_DATE_UPDATED, unixTime);

            int rowAffected = getContentResolver().update(mCurrentTaskUri, values, null, null);
            if (rowAffected == 0) {
                Toast.makeText(getApplicationContext(), getString(R.string.editor_task_update_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), getString(R.string.editor_task_update_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                TaskEntry._ID,
                TaskEntry.COLUMN_TASK_NAME,
                TaskEntry.COLUMN_TASK_DESCRIPTION,
        };
        return new CursorLoader(
                getApplicationContext(),
                mCurrentTaskUri,
                projection,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor.moveToFirst()) {
            int nameColumnIndex = cursor.getColumnIndex(TaskEntry.COLUMN_TASK_NAME);
            int descriptionColumnIndex = cursor.getColumnIndex(TaskEntry.COLUMN_TASK_DESCRIPTION);

            String name = cursor.getString(nameColumnIndex);
            String description = cursor.getString(descriptionColumnIndex);

            mNameEditText.setText(name);
            mDescriptionEditText.setText(description);
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }


    private void showUnsavedChangeDialog(final int buttonID) {
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (buttonID == BACK_PRESSED_BUTTON) {
                            finish();
                        } else if (buttonID == HOME_BUTTON) {
                            NavUtils.navigateUpFromSameTask(TaskEditorActivity.this);
                        }
                    }
                };
        DialogInterface.OnClickListener keepEditingListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked the "keep editing" button, so dismiss the dialog and
                        // continue editing the pet.
                        if (dialogInterface != null) {
                            dialogInterface.dismiss();
                        }
                    }
                };
        // Create an AlertDialog.Builder and the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, keepEditingListener);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteTask();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (dialogInterface != null) {
                    dialogInterface.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteTask() {
        int rowDeleted = getContentResolver().delete(mCurrentTaskUri, null, null);
        if (rowDeleted == 0) {
            Toast.makeText(getApplicationContext(), R.string.editor_delete_task_failed,
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), R.string.editor_delete_task_successful,
                    Toast.LENGTH_SHORT).show();
        }
        finish();
    }

    @Override
    public void onBackPressed() {
        if (!mTaskHasChanged) {
            super.onBackPressed();
            return;
        }
        showUnsavedChangeDialog(BACK_PRESSED_BUTTON);
    }
}
