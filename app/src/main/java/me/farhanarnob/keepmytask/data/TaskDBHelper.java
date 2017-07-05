package me.farhanarnob.keepmytask.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import me.farhanarnob.keepmytask.data.TaskContract.TaskEntry;

/**
 * Created by Farhan Rahman Arnob on ${05-Jul-17}.
 * For coding task of INTELLIJ SYSTEM SOLUTION SDN BHD.
 * <p>
 * Copyright Farhan Rahman Arnob
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

public class TaskDBHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Task.db";

    // creating SQLite create query
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TaskEntry.TABLE_NAME + " (" +
                    TaskEntry._ID + " INTEGER PRIMARY KEY," +
                    TaskEntry.COLUMN_TASK_NAME + " TEXT NOT NULL," +
                    TaskEntry.COLUMN_TASK_DESCRIPTION + " TEXT," +
                    TaskEntry.COLUMN_TASK_DATE_CREATED + " INTEGER NOT NULL," +
                    TaskEntry.COLUMN_TASK_DATE_UPDATED + " INTEGER );";

    // creating SQLite delete query
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TaskEntry.TABLE_NAME;

    public TaskDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}
