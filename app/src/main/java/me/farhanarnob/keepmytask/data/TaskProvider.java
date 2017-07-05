package me.farhanarnob.keepmytask.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

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

public class TaskProvider extends ContentProvider {

    public static final String LOG_TAG = TaskProvider.class.getSimpleName();

    // URI matcher code for the content URI for the tasks table
    public static final int TASK = 1000;

    // URI matcher code for the content URI for a single task in the pets table
    public static final int TASK_ID = 1001;

    // UriMatcher object to match a content Uri to a corresponding code.
    /// default is UriMatcher.NO_MATCH
    private static final UriMatcher sURI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    // static initializer for the UriMatcher
    static {
        sURI_MATCHER.addURI(TaskContract.CONTENT_AUTHORITY, TaskContract.PATH_TASKS, TASK);
        sURI_MATCHER.addURI(TaskContract.CONTENT_AUTHORITY, TaskContract.PATH_TASKS + "/#", TASK_ID);
    }

    private TaskDBHelper mTaskDBHelper;

    @Override
    public boolean onCreate() {
        mTaskDBHelper = new TaskDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        // Get readable database
        SQLiteDatabase database = mTaskDBHelper.getReadableDatabase();

        // This cursor will hold the result of the query
        Cursor cursor;

        // Figure out if the URI matcher can match the URI to a specific code
        int match = sURI_MATCHER.match(uri);
        switch (match) {

            case TASK:
                // For the PETS code, query the pets table directly with the given
                // projection, selection, selection arguments, and sort order. The cursor
                // could contain multiple rows of the pets table.
                cursor = database.query(TaskEntry.TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;

            case TASK_ID:
                selection = TaskEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                // This will perform a query on the pets table where the _id equals 3 to return a
                // Cursor containing that row of the table.
                cursor = database.query(TaskEntry.TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(),
                uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final int match = sURI_MATCHER.match(uri);
        switch (match) {
            case TASK:
                return insertTask(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertTask(Uri uri, ContentValues contentValues) {
        sanityCheck(contentValues);
        SQLiteDatabase database = mTaskDBHelper.getWritableDatabase();
        long id = database.insert(TaskEntry.TABLE_NAME, null, contentValues);
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        // Notify all lestenrs that the data has changes for the pet content URI
        notifyChanges(uri);

        return ContentUris.withAppendedId(uri, id);
    }

    // notifying change of data in database
    private void notifyChanges(Uri uri) {
        getContext().getContentResolver().notifyChange(uri, null);
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase database = mTaskDBHelper.getWritableDatabase();
        final int match = sURI_MATCHER.match(uri);
        int rowDeleted;
        switch (match) {
            case TASK:
                rowDeleted = database.delete(TaskEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case TASK_ID:
                selection = TaskEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowDeleted = database.delete(TaskEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("database to delete for this uri " + uri);
        }
        if (rowDeleted > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowDeleted;
    }


    @Override
    public int update(@NonNull Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        final int match = sURI_MATCHER.match(uri);
        switch (match) {
            case TASK:
                return updateTask(uri, contentValues, selection, selectionArgs);
            case TASK_ID:
                selection = TaskEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateTask(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }


    public int updateTask(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        sanityCheck(contentValues);
        SQLiteDatabase database = mTaskDBHelper.getWritableDatabase();
        int numbersOfRow = database.update(TaskEntry.TABLE_NAME, contentValues, selection,
                selectionArgs);
        if (numbersOfRow > 0) {
            notifyChanges(uri);
        }
        return numbersOfRow;
    }

    private void sanityCheck(ContentValues contentValues) {
        String name = contentValues.getAsString(TaskEntry.COLUMN_TASK_NAME);
        if (name == null || name.equals("")) {
            throw new IllegalArgumentException("Task requires a task name");
        }

        Integer creatingDate = contentValues.getAsInteger(TaskEntry.COLUMN_TASK_DATE_CREATED);
        if (creatingDate != null && creatingDate < 0) {
            throw new IllegalArgumentException("Task requires valid creating Date");
        }
    }
}
