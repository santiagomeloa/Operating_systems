/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ur_os;

import java.util.ArrayList;

/**
 *
 * @author super
 */
public class ReadyQueue {
    
    Scheduler s;
    OS os;
    
    
    public ReadyQueue(OS os){
        this.os = os;

        switch(os.SCHEDULER_TYPE){
            
            case FCFS:
                s = new FCFS(os);
                break;
            
            case SJF_NP:
                s = new SJF_NP(os);
                break;
            
            case SJF_P:
                s = new SJF_P(os);
                break;
            
            case RR:
                s = new RoundRobin(os,4);
                break;
            
            case PRIORITY:
                s = new PriorityQueue(os,new RoundRobin(os,9,false),new RoundRobin(os,6,false),new RoundRobin(os,3,false),new RoundRobin(os,2,false));
                break;
            
            case MFQ:
                s = new MFQ(os,new RoundRobin(os,3),new RoundRobin(os,6),new FCFS(os));
                break;
                
            case FAIR:
                break;
        }
    }
    
    public ReadyQueue(OS os, Scheduler s){
        this.os = os;
        this.s = s;
    }
    
    public void addProcess(Process p){
        s.addProcess(p);
    }
    
    public Process removeProcess(Process p){
        return s.removeProcess(p);
    }
    
    public void update(){
        s.update();
    }
        
    @Override
    public String toString(){
        
        return s.toString();
    }
    
    public int getTotalContextSwitches(){
        return s.getTotalContextSwitches();
    }
}
