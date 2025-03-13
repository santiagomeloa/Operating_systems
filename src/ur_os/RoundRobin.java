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
    
    RoundRobin(OS os){
        super(os);
        q = 4;
        cont=0;
        singlequeue = true;
    }
    
    RoundRobin(OS os, int q){
        this(os);
        this.q = q;
    }

    RoundRobin(OS os, int q, boolean singlequeue){
        this(os);
        this.q = q;
        this.singlequeue = singlequeue;
    }
    
    
    void resetCounter(){
        cont=0;
    }
   
    
    @Override
    public void getNext(boolean cpuEmpty) {
        if (!cpuEmpty) {
            cont += 1;
            if (cont == q) { // Quantum agotado
                if (this.singlequeue) {
                    addProcess(os.cpu.extractProcess());
                    cpuEmpty = true;            
                } else {
                    os.cpu.removeProcess();
                    addContextSwitch();
                }
                os.interrupt(InterruptType.SCHEDULER_CPU_TO_RQ, p);
                addContextSwitch();
                resetCounter();
            }
        }

        if (!processes.isEmpty() && cpuEmpty) {
            resetCounter(); // Reiniciar contador del quantum
            Process p = processes.get(0);
            processes.remove();

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
