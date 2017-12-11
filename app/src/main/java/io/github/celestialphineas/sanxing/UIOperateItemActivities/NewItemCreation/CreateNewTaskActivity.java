package io.github.celestialphineas.sanxing.UIOperateItemActivities.NewItemCreation;

import android.animation.Animator;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.SeekBar;
import android.widget.TimePicker;

import org.threeten.bp.LocalDateTime;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindInt;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import io.github.celestialphineas.sanxing.MyApplication;
import io.github.celestialphineas.sanxing.R;
import io.github.celestialphineas.sanxing.SanxingBackend.TaskRepo;
import io.github.celestialphineas.sanxing.UIOperateItemActivities.Base.OperateTaskActivityBase;
import io.github.celestialphineas.sanxing.sxObject.Task;

public class CreateNewTaskActivity extends OperateTaskActivityBase {
    private MyApplication myApplication;
    private Task task;
    @BindView(R.id.task_linear_layout)      LinearLayoutCompat linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        title = getString(R.string.create_new_task);

        setContentView(R.layout.activity_create_new_task);

        //////// Animation ////////
        overridePendingTransition(R.anim.none, R.anim.none);
        animationReveal(savedInstanceState);
        myApplication= (MyApplication) getApplication();
        task=new Task();
        super.onCreate(savedInstanceState);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will.
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_confirm_new_item) {
            // The verifyForm() method can be modified if necessary
            if(!verifyForm(linearLayout)) return true;
            ////////
            // Then register the changes in the database
            // Use "dueCalendar" for due date and time
            // Use "selectedImportance" for the the importance 0~4
            // Use inputTitle.getText().toString() to get the title
            // Use descriptionContent.getText().toString() to get the description
            LocalDateTime now=LocalDateTime.now();
            String s=now.toString().replace('T',' ');
            if (s.length()==16) s=s.concat(":00");
            if (s.length()>19) s=s.substring(0,19);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            task.create_task(inputTitle.getText().toString(),s,sdf.format(dueCalendar.getTime()).substring(0,16).concat(":00"),
                    descriptionContent.getText().toString(),selectedImportance);
            myApplication.getApplicationContext();
            myApplication.get_task_manager().addObject(task);
            Intent intent = new Intent();
            intent.putExtra("task_title",inputTitle.getText().toString());
            animationSubmit();

            //pass data to the home activity
            setResult(Activity.RESULT_OK, intent);
            finish();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
