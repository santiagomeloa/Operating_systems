/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ur_os;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author prestamour
 */
public class MFQ extends Scheduler{

    int currentScheduler;
    
    private ArrayList<Scheduler> schedulers;
    //This may be a suggestion... you may use the current sschedulers to create the Multilevel Feedback Queue, or you may go with a more tradicional way
    //based on implementing all the queues in this class... it is your choice. Change all you need in this class.
    
    MFQ(OS os){
        super(os);
        currentScheduler = -1;
        schedulers = new ArrayList();
    }
    
    MFQ(OS os, Scheduler... s){ //Received multiple arrays
        this(os);
        schedulers.addAll(Arrays.asList(s));
        if(s.length > 0)
            currentScheduler = 0;
    }
        
    @Override
    public void addProcess(Process p){
        //Overwriting the parent's addProcess(Process p) method may be necessary in order to decide what to do with process coming from the CPU.

        ProcessState ps = p.getState();
        int currentSchedulerProcess = p.getCurrentScheduler();
        if (ps == ProcessState.CPU){
            currentSchedulerProcess = Math.min(currentSchedulerProcess + 1, schedulers.size() - 1);
            p.setCurrentScheduler(currentSchedulerProcess);
        }
        else if (ps == ProcessState.IO){
            currentSchedulerProcess = 0;
            p.setCurrentScheduler(currentSchedulerProcess);
        }
        schedulers.get(currentSchedulerProcess).addProcess(p);
    }
    
    private void defineCurrentScheduler(){
        //This methos is siggested to help you find the scheduler that should be the next in line to provide processes... perhaps the one with process in the queue?
        
        for (int i = 0; i < schedulers.size(); i++) {
            Scheduler s = schedulers.get(i);
            if (!s.isEmpty()) {
                currentScheduler = i;
                return;
            }
        }
    }
    
   
    @Override
    public void getNext(boolean cpuEmpty) {
        //Suggestion: now that you know on which scheduler a process is, you need to keep advancing that scheduler. If it a preemptive one, you need to notice the changes
        //that it may have caused and verify if the change is coherent with the priority policy for the queues.
        if (cpuEmpty){
            defineCurrentScheduler();
        }
        Scheduler s = schedulers.get(this.currentScheduler);
        s.getNext(cpuEmpty);
        
        //waiting time
        //for(Scheduler s: schedulers){
        //    for(Process p: s.processes){
        //        p.setWaitingTime(p.getWaitingTime() + 1);
        //    }
        //}
    }
    
    @Override
    public void newProcess(boolean cpuEmpty) {} //Non-preemtive in this event

    @Override
    public void IOReturningProcess(boolean cpuEmpty) {} //Non-preemtive in this event
    
}
