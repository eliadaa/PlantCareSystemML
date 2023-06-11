package com.example.plantcaresystem;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

public class CurrentActivityUtils {
    public static String getCurrentActivityName(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        if (activityManager != null) {
            // Get the list of running tasks
            List<ActivityManager.RunningTaskInfo> runningTasks = activityManager.getRunningTasks(1);

            if (!runningTasks.isEmpty()) {
                // Get the top activity from the running tasks
                ActivityManager.RunningTaskInfo runningTask = runningTasks.get(0);

                // Get the top activity's class name
                String topActivityName = runningTask.topActivity.getClassName();
                return topActivityName;
            }
        }

        return null;
    }
}
