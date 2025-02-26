/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ur_os;

/**
 *
 * @author prestamour
 */
public class SJF_P extends Scheduler{

    
    SJF_P(OS os){
        super(os);
    }
    
    @Override
    public void newProcess(boolean cpuEmpty){// When a NEW process enters the queue, process in CPU, if any, is extracted to compete with the rest
        if(!cpuEmpty){
            Process p = os.cpu.extractProcess();
            addProcess(p);
        }
    } 

    @Override
    public void IOReturningProcess(boolean cpuEmpty){// When a process return from IO and enters the queue, process in CPU, if any, is extracted to compete with the rest
        if(!cpuEmpty){
            Process p = os.cpu.extractProcess();
            addProcess(p);
        }
    } 
    
   
    @Override
    public void getNext(boolean cpuEmpty) {
       if(!processes.isEmpty() && cpuEmpty){   
            Integer i = 0; //For checking the first min selected
            Process min = null; //Min selected

            for (Process p : processes){
                ProcessBurstList PBL = p.getPBL();
                if(i == 0){
                    if(PBL.isCurrentBurstCPU()){
                        min = p;
                        i++;
                    }

                }else{
                    if(PBL.isCurrentBurstCPU()){
                        if(p.getRemainingTimeInCurrentBurst() <= min.getRemainingTimeInCurrentBurst()){
                            
                            if(p.getRemainingTimeInCurrentBurst() == min.getRemainingTimeInCurrentBurst()){ //Criteria for same remaining cycles processes
                                min = tieBreaker(p, min);
                                
                            }else{ //p process is the new min
                                min = p;
                            }
                        }
                    }
                }
            }
            processes.remove(min);
            os.interrupt(InterruptType.SCHEDULER_RQ_TO_CPU, min);
            addContextSwitch();
        }
    }
    
}
