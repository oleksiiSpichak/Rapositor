package ua.sumdu.j2se.AleksiiSpichak.tasks.Controller;

import ua.sumdu.j2se.AleksiiSpichak.tasks.model.ArrayTaskList;
import ua.sumdu.j2se.AleksiiSpichak.tasks.model.Task;
import ua.sumdu.j2se.AleksiiSpichak.tasks.model.TaskIO;
import ua.sumdu.j2se.AleksiiSpichak.tasks.view.TaskManagerView;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.Date;
import java.util.Timer;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * Created by o.spichak on 10.08.2017.
 */
public class TaskManagerController {
    static File file = new File("Kalendar.txt");

    static final String path = "resources/log4j.properties";
    private static final Logger logger = Logger.getLogger(TaskManagerController.class);

    static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    static ArrayTaskList tasksNew = new ArrayTaskList();

    public static void runNen() throws IOException, ParseException,ArrayIndexOutOfBoundsException
    {
        PropertyConfigurator.configure(path);

        TaskManagerView viewer = new TaskManagerView();
        if (file.length() > 0) {
            runSignal();
        } else {
            file.createNewFile();
        }

        while (true) {
            logger.debug(" we are here!!");
            TaskManagerView.menu(tasksNew);
            String numa = in.readLine();
            int input = Integer.parseInt(numa);
            switch (input) {
                case 1:
                    String name = viewer.readName(TaskManagerView.addNameTask());
                    Date date = viewer.getDate(TaskManagerView.addDateTask());
                    TaskManagerView.question();
                    String a = in.readLine();
                    int inp = Integer.parseInt(a);

                    if (inp == 9) {
                        TaskManagerView.addIntervalTask();
                        String b = in.readLine();
                        int inter = Integer.parseInt(b);
                        Date endDate = viewer.getDate(TaskManagerView.addEndTask());

                        Task task = new Task(name, date, endDate, inter);

                        tasksNew.add(task);
                    } else {
                        Task task = new Task(name, date);
                        tasksNew.add(task);
                    }
                    break;
                case 2:
                    if (tasksNew.size() >= 0) {
                        TaskManagerView.listTask(tasksNew);
                        TaskManagerView.nomer();
                        String x = in.readLine();
                        int i = Integer.parseInt(x);
                        if(tasksNew.size() > i ){
                            Task task3 = tasksNew.getTask(i);
                            TaskManagerView.edit();
                            String b = in.readLine();
                            int input1 = Integer.parseInt(b);
                            switch (input1) {
                                case 1:
                                    String name1 = viewer.readName(TaskManagerView.addNameTask());
                                    task3.setTitle(name1);
                                    break;
                                case 2:
                                    Date c = viewer.getDate(TaskManagerView.addDateTask());
                                    task3.setTime(c);
                                    break;
                                case 3:
                                    TaskManagerView.addIntervalTask();
                                    String w = in.readLine();
                                    task3.setInterval(Integer.parseInt(w));
                                    break;
                                case 4:
                                    Date d = viewer.getDate(TaskManagerView.addEndTask());
                                    task3.setTime(d);
                                    break;
                                case 5:
                                    TaskManagerView.exit();
                                    in.readLine();
                                    break;
                            }
                        } else {
                            TaskManagerView.noTask();
                        }
                    }
                    break;

                // metod udaleniya zadaci
                case 3:
                    boolean l = false;
                    if (tasksNew.size() != 0) {
                        TaskManagerView.listTask(tasksNew);
                        TaskManagerView.nomer();
                        String c = in.readLine();
                        for (int i = 0; i < tasksNew.size(); ++i) {
                            int s = Integer.parseInt(c);
                            if (i == s)
                                l = true;
                            tasksNew.remove(tasksNew.getTask(i));
                        }
                    }
                    if (l) {
                        TaskManagerView.delite();
                    } else {
                        TaskManagerView.noTask();
                    }
                    break;

                /* sostoyanie zadachi*/
                case 4:
                    TaskManagerView.statusTask(tasksNew);
                    break;
                //listName zadach
                case 5:
                    TaskManagerView.calendar(tasksNew, TaskManagerView.addDateTask());
                    break;
                case 6:
                    TaskIO.writeText(tasksNew, file);
                    System.exit(0);
                    break;
            }
        }
    }


    public static void  runSignal() throws IOException, ParseException {
        Timer time = new Timer();
        TaskIO.readText(tasksNew, file);
        for(Task task: tasksNew){
            if(task.isRepeated()){
                time.schedule(task, task.getStartTime(), task.getRepeatInterval()*1000);
            }
            else {
                time.schedule(task, task.getTime());
            }
        }
    }

}