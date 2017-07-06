Keep My Task – Sample note keeping app for Android




This is a sample app, provided to show developers how to make a simple application using SQLite database with content provider.

This application has –
       Two activities:
1. TaskListActivity
2. TaskEditorActivity

One Cursor Adapter:
1. TaskCursorAdapter

One Content Provider:
1. TaskProvider

One SQLiteOpenHelper Class:
1. TaskDBHelper

And, One Data Model
1. TaskContract





In TaskListActivity, it gets data of all tasks from SQLite Database using content provider. Then data is processed and showed in Grid View using Cursor Adapter.

This activity has a fab button, which is used to create a new task.

In grid View, we can click single task and go to the edit option.

In this activity, there has a menu item by which one can delete all tasks.




In TaskEditorActivity, it has two works.
	
1. Create a task: One can write the task name and give a description (optional). Then by clicking “✔” menu button one can create a task

2. View/Edit a task: By clicking a task of grid view in the main activity (TaskListActivity), one can enter an extended view of that task. In here, one can read his task details, modify, delete and save. After editing one, it will show a warning message if he wants to discard changes.  
 






3.0 Screenshots


1. First Screen				2. Add a Task			     3. After adding task 
(Empty Screen)   							         (Main Screen)




       
       4. Update a task		           5. Land Scape Mode		   6. Warning for discard
		

