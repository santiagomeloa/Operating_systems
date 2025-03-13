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
public class PriorityQueue extends Scheduler{

    int currentScheduler;
    
    private ArrayList<Scheduler> schedulers;
    
    PriorityQueue(OS os){
        super(os);
        currentScheduler = -1;
        schedulers = new ArrayList();
    }
    
    PriorityQueue(OS os, Scheduler... s){ //Received multiple arrays
        this(os);
        schedulers.addAll(Arrays.asList(s));
        if(s.length > 0)
            currentScheduler = 0;
    }
    
    
    @Override
    public void addProcess(Process p){
       if (!schedulers.isEmpty()) {
            schedulers.get(p.getPriority()).addProcess(p); 
        }
        
    }
    
    private int defineCurrentScheduler() {
        if (!os.isCPUEmpty()) {
            Process cpuProcess = os.cpu.getProcess();
            return cpuProcess.getPriority(); // Return the priority of the process on CPU
        } else {
            for (int i = 0; i < schedulers.size(); i++) {
                if (!schedulers.get(i).isEmpty()) {
                    return i; // Return the index of the first non-empty scheduler
                }
            }
        }
        return -1; // If all schedulers are empty, return -1
    }
   
    
   
    @Override
    public void getNext(boolean cpuEmpty) {
        
        int currentSchedulerIndex = defineCurrentScheduler();
        
        if (currentSchedulerIndex != -1) {
            Scheduler currentScheduler = schedulers.get(currentSchedulerIndex);
            
            if (!os.isCPUEmpty()) {
                Process cpuProcess = os.cpu.getProcess();                
                if (cpuProcess.getPriority() == currentSchedulerIndex) {
                    currentScheduler.getNext(os.isCPUEmpty());
                    if (os.isCPUEmpty()) {
                        addProcess(cpuProcess); // Add the process back to its respective queue
                        addContextSwitch();
                        currentSchedulerIndex = defineCurrentScheduler(); // Re-evaluate the current scheduler
                        if (currentSchedulerIndex != -1) {
                            currentScheduler = schedulers.get(currentSchedulerIndex);
                            currentScheduler.getNext(os.isCPUEmpty());
                        }
                    }
                }
            } else {
                currentScheduler.getNext(os.isCPUEmpty());
                addContextSwitch();
            }
        }
        
        // Update waiting time for all processes
        for (Scheduler s : schedulers) {
            for (Process p : s.processes) {
                p.setWaitingTime(p.getWaitingTime() + 1);
            }
        }
    }
    
    
    @Override
    public void newProcess(boolean cpuEmpty) {} //Non-preemtive in this event

    @Override
    public void IOReturningProcess(boolean cpuEmpty) {} //Non-preemtive in this event
}
