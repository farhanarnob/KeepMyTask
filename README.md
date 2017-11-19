# KeepMyTask
Created by Farhan Rahman Arnob on 5-Jul-2017. For coding task of INTELLIJ SYSTEM SOLUTION SDN BHD.

***Keep My Task – Sample note keeping app for Android***

It’s a Simple note keeping app for Android, using SQLite Database and
Content Provider.

This application has –
Two activities:
1.	TaskListActivity
2.	TaskEditorActivity

One Cursor Adapter:
1.	TaskCursorAdapter

One Content Provider:
1.	TaskProvider

One SQLiteOpenHelper Class:
1.	TaskDBHelper

And, One Data Model
1.	TaskContract


In **TaskListActivity**, it gets data of all tasks from SQLite Database using content provider. Then data is processed and showed in Grid View using Cursor Adapter.

This activity has a fab button, which is used to create a new task.

In grid View, we can click single task and go to the edit option.

In this activity, there has a menu item by which one can delete all tasks.


In **TaskEditorActivity**, it has two works.
	
1.	Create a task: One can write the task name and give a description (optional). Then by clicking “✔” menu button one can create a task

2.	View/Edit a task: By clicking a task of grid view in the main activity (TaskListActivity), one can enter an extended view of that task. In here, one can read his task details, modify, delete and save. After editing one, it will show a warning message if he wants to discard changes.  

<p align="center">
<img src="https://user-images.githubusercontent.com/19855097/27922059-a2700948-629b-11e7-9f6f-54c16e494555.png" width="350"/>
<img src="https://user-images.githubusercontent.com/19855097/27922091-ba247740-629b-11e7-8792-6ba4c068884f.png" width="350"/>
<img src="https://user-images.githubusercontent.com/19855097/27922116-d442a016-629b-11e7-9589-c8d0121f9119.png" width="350"/>
<img src="https://user-images.githubusercontent.com/19855097/27922157-ed16117c-629b-11e7-9980-993912e1b25a.png" width="350"/>
<img src="https://user-images.githubusercontent.com/19855097/27922187-089764dc-629c-11e7-9383-2f195f575ac5.png" width="350"/>
<img src="https://user-images.githubusercontent.com/19855097/27921960-482e18c6-629b-11e7-9f81-92f02175f487.png" width="350"/>
</p>

