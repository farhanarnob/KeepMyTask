package me.farhanarnob.keepmytask;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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

class TaskCursorAdapter extends CursorAdapter {

    TaskCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_list_task, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView tvName = (TextView) view.findViewById(R.id.text_view_name);
        TextView tvDescription = (TextView) view.findViewById(R.id.text_view_description);
        TextView tvDateCreated = (TextView) view.findViewById(R.id.text_view_date_created);
        TextView tvDateUpdated = (TextView) view.findViewById(R.id.text_view_date_updated);

        String cName = cursor.getString(cursor.getColumnIndex(TaskEntry.COLUMN_TASK_NAME));
        String cDescription = cursor.getString(cursor.getColumnIndex(
                TaskEntry.COLUMN_TASK_DESCRIPTION));

        long cDateCreatedUnix = cursor.getLong(cursor.getColumnIndex(
                TaskEntry.COLUMN_TASK_DATE_CREATED));
        long cDateUpdatedUnix = cursor.getLong(cursor.getColumnIndex(
                TaskEntry.COLUMN_TASK_DATE_UPDATED));

        String cDateCreated = timeFormat(cDateCreatedUnix);
        String cDateUpdated;
        if (cDateUpdatedUnix != 0) {
            cDateUpdated = timeFormat(cDateUpdatedUnix);
        } else {
            cDateUpdated = context.getString(R.string.never_updated);
        }

        tvName.setText(cName);
        tvDescription.setText(cDescription);
        tvDateCreated.setText(cDateCreated);
        tvDateUpdated.setText(cDateUpdated);


    }

    private String timeFormat(Long unixTime) {
        Date date = new Date(unixTime * 1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("mm:HH, dd-MM-yy",
                Locale.getDefault());
        return sdf.format(date);
    }
}
