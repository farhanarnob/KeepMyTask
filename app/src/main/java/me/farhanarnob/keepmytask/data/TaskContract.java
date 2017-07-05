package me.farhanarnob.keepmytask.data;

import android.provider.BaseColumns;

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

// API Contract for Keep my task app
public final class TaskContract {

    // preventing unexpected initializing
    private TaskContract() {
    }

    public static final class TaskEntry implements BaseColumns {
        public final static String TABLE_NAME = "tasks";
        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_TASK_NAME = "name";
        public final static String COLUMN_TASK_DESCRIPTION = "description";
        public final static String COLUMN_TASK_DATE_CREATED = "dateCreated";
        public final static String COLUMN_TASK_DATE_UPDATED = "dateUpdated";

    }


}
