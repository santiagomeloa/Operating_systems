/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ur_os;

/**
 *
 * @author prestamour
 */
public class RoundRobin extends Scheduler{

    int q;
    int cont;
    boolean singlequeue;
    boolean processEndedStatus;
    Process processEnded;
    
    RoundRobin(OS os){
        super(os);
        q = 4;
        cont=0;
        singlequeue = true;
        processEndedStatus = false;
    }
    
    RoundRobin(OS os, int q){
        this(os);
        this.q = q;
        processEndedStatus = false;
    }

    RoundRobin(OS os, int q, boolean singlequeue){
        this(os);
        this.q = q;
        this.singlequeue = singlequeue;
        processEndedStatus = false;
    }
    
    
    void resetCounter(){
        cont=0;
    }
   
    
    @Override
    public void getNext(boolean cpuEmpty) {
        if (!cpuEmpty) {
            cont += 1;
            if (cont >= q) { // Quantum agotado
                
                Process p = null;
                if(!processes.isEmpty()){
                    p = processes.remove();
                }
                os.interrupt(InterruptType.SCHEDULER_CPU_TO_RQ, p);
                addContextSwitch();
                resetCounter();
            }
        }else if (!processes.isEmpty()) {
            Process p = processes.remove();
            os.interrupt(InterruptType.SCHEDULER_RQ_TO_CPU, p);
            addContextSwitch();
            resetCounter();
        }
    }


    
    @Override
    public void newProcess(boolean cpuEmpty) {} //Non-preemtive in this event

    @Override
    public void IOReturningProcess(boolean cpuEmpty) {} //Non-preemtive in this event
    
}
