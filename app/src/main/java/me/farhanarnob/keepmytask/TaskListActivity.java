package me.farhanarnob.keepmytask;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import me.farhanarnob.keepmytask.data.TaskContract.TaskEntry;
import me.farhanarnob.keepmytask.data.TaskDBHelper;

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

public class TaskListActivity extends AppCompatActivity {
    private SQLiteDatabase db;
    private TaskDBHelper mTaskDBHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);
        mTaskDBHelper = new TaskDBHelper(this);
        testTaskInfo();
    }

    // SQLite is working or not
    private void testTaskInfo() {
        db = mTaskDBHelper.getReadableDatabase();
        String[] projection = {
                TaskEntry._ID,
                TaskEntry.COLUMN_TASK_NAME,
                TaskEntry.COLUMN_TASK_DESCRIPTION,
                TaskEntry.COLUMN_TASK_DATE_CREATED,
                TaskEntry.COLUMN_TASK_DATE_UPDATED
        };
        Cursor taskCursor = db.query(TaskEntry.TABLE_NAME, projection, null, null, null, null, null);
        taskCursor.close();
    }
}
